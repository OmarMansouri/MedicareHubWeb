package medicare.back.controllers;

import medicare.back.services.MeteoRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meteo-risk")
@CrossOrigin(origins = "*")
public class MeteoRiskController {

    @Autowired
    private MeteoRiskService meteoRiskService;

    @GetMapping("/coords")
    public int getMeteoRisk(
            @RequestParam double lat,
            @RequestParam double lon) {

        int score = meteoRiskService.computeMeteoScore(lat, lon);
        return score;
    }
}