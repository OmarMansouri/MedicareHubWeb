package medicare.back.services;

import medicare.back.dto.RiskDto;
import org.springframework.stereotype.Service;

@Service
public class RiskService {

    private final PollutionRiskService pollutionRiskService;
    private final MeteoRiskService meteoRiskService;
    private final IndustryRiskService industryRiskService;

    public RiskService(PollutionRiskService pollutionRiskService,
                       MeteoRiskService meteoRiskService,
                       IndustryRiskService industryRiskService) {
        this.pollutionRiskService = pollutionRiskService;
        this.meteoRiskService = meteoRiskService;
        this.industryRiskService = industryRiskService;
    }

    
    public RiskDto computeRisk(double lat, double lon, double radiusKm) {
        RiskDto dto = new RiskDto();
        int pollutionScore = pollutionRiskService.computePollutionScore(lat, lon);
        int meteoScore = meteoRiskService.computeMeteoScore(lat, lon);
        int industryScore = industryRiskService.computeIndustryScore(lat, lon, radiusKm);
        dto.setPollutionScore(pollutionScore);
        dto.setMeteoScore(meteoScore);
        dto.setIndustryScore(industryScore);
        int total = pollutionScore + meteoScore + industryScore; // min 3, max 9
        dto.setTotalScore(total);
        String level;
        if (total <= 4) level = "Faible";
        else if (total <= 6) level = "Modéré";
        else level = "Élevé";
        dto.setLevel(level);
        return dto;
    }
}