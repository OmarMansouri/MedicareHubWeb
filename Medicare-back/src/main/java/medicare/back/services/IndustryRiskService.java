package medicare.back.services;

import medicare.back.models.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustryRiskService {

    @Autowired
    private PlantService plantService;

    public int computeIndustryScore(double lat, double lon, double radiusKm) {
        int score = 1; 
        try {
            List<Plant> plants = plantService.getPlantsByCoords(lat, lon, radiusKm);
            double totalEmission = 0;
            for (int i = 0; i < plants.size(); i++) {
                Plant p = plants.get(i);

                if (p.emissionCo2Tons != null) {
                    totalEmission = totalEmission + p.emissionCo2Tons;
                }
            }

            if (totalEmission < 100000) {
                score = 1;
            } else if (totalEmission < 1000000) {
                score = 2;
            } else {
                score = 3;
            }

        } catch (Exception e) {
            e.printStackTrace();
            score = 1;
        }
        return score;
    }
}