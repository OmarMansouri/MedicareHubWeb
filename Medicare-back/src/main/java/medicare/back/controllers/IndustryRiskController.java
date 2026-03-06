package medicare.back.controllers;

import medicare.back.services.IndustryRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/industry-risk")
@CrossOrigin(origins = "*")
public class IndustryRiskController {

    @Autowired
    private IndustryRiskService industryRiskService;

    @GetMapping("/coords")
    public int getIndustryRisk(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5") double radiusKm) {

        int score = industryRiskService.computeIndustryScore(lat, lon, radiusKm);
        return score;
    }
}