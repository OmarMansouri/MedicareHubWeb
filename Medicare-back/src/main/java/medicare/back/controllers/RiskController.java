package medicare.back.controllers;

import medicare.back.dto.RiskDto;
import medicare.back.services.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = "*")
public class RiskController {
    private final RiskService riskService;
    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/coords")
    public ResponseEntity<RiskDto> getRisk(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5") double radiusKm
    ) {
        RiskDto dto = riskService.computeRisk(lat, lon, radiusKm);
        return ResponseEntity.ok(dto);
    }
}