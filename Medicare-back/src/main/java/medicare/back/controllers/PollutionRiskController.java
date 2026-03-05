package medicare.back.controllers;

import medicare.back.services.PollutionRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pollution-risk")
@CrossOrigin(origins = "*")
public class PollutionRiskController {

    @Autowired
    private PollutionRiskService pollutionRiskService;

    @GetMapping("/coords")
    public int getPollutionRisk(
            @RequestParam double lat,
            @RequestParam double lon) {

        int score = pollutionRiskService.computePollutionScore(lat, lon);
        return score;
    }
}