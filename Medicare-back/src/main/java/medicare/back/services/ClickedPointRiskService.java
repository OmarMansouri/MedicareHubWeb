package medicare.back.services;

import medicare.back.dto.RiskDto;
import medicare.back.models.ClickedPointRisk;
import medicare.back.repositories.ClickedPointRiskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClickedPointRiskService {

    private final ClickedPointRiskRepository clickedPointRiskRepository;
    private final RiskService riskService;

    public ClickedPointRiskService(ClickedPointRiskRepository clickedPointRiskRepository,
            RiskService riskService) {
        this.clickedPointRiskRepository = clickedPointRiskRepository;
        this.riskService = riskService;
    }

    public ClickedPointRisk savePoint(double lat, double lon) {
        double latRounded = Math.round(lat * 1000.0) / 1000.0;
        double lonRounded = Math.round(lon * 1000.0) / 1000.0;

        RiskDto risk = riskService.computeRisk(latRounded, lonRounded, 5);

        ClickedPointRisk point = new ClickedPointRisk();
        point.date = LocalDate.now();
        point.latitude = latRounded;
        point.longitude = lonRounded;
        point.pollutionScore = risk.getPollutionScore();
        point.meteoScore = risk.getMeteoScore();
        point.industryScore = risk.getIndustryScore();
        point.totalScore = risk.getTotalScore();
        point.level = risk.getLevel();

        return clickedPointRiskRepository.save(point);
    }

    public List<ClickedPointRisk> getHistory() {
        return clickedPointRiskRepository.findAllByOrderByIdDesc();
    }

    public java.util.Map<String, Object> getAverageRiskForPoint(double lat, double lon) {
        double latRounded = Math.round(lat * 1000.0) / 1000.0;
        double lonRounded = Math.round(lon * 1000.0) / 1000.0;

        double delta = 0.0005;

        List<ClickedPointRisk> points = clickedPointRiskRepository
                .findByLatitudeBetweenAndLongitudeBetween(
                        latRounded - delta, latRounded + delta,
                        lonRounded - delta, lonRounded + delta);

        java.util.Map<String, Object> result = new java.util.HashMap<>();

        if (points.isEmpty()) {
            result.put("count", 0);
            result.put("averageScore", 0.0);
            result.put("level", "Aucune donnée");
            result.put("latitude", latRounded);
            result.put("longitude", lonRounded);
            return result;
        }

        double sum = 0;
        for (ClickedPointRisk point : points) {
            sum += point.totalScore;
        }

        double average = sum / points.size();

        String level;
        if (average <= 4) {
            level = "Faible";
        } else if (average <= 6) {
            level = "Modéré";
        } else {
            level = "Élevé";
        }

        result.put("count", points.size());
        result.put("averageScore", average);
        result.put("level", level);
        result.put("latitude", latRounded);
        result.put("longitude", lonRounded);

        return result;
    }
}