package medicare.back.controllers;

import medicare.back.models.ClickedPointRisk;
import medicare.back.services.ClickedPointRiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/click-risk")
@CrossOrigin(origins = "*")
public class ClickedPointRiskController {

    private final ClickedPointRiskService clickedPointRiskService;

    public ClickedPointRiskController(ClickedPointRiskService clickedPointRiskService) {
        this.clickedPointRiskService = clickedPointRiskService;
    }

    @PostMapping("/save")
    public ResponseEntity<ClickedPointRisk> savePoint(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return ResponseEntity.ok(clickedPointRiskService.savePoint(lat, lon));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ClickedPointRisk>> getHistory() {
        return ResponseEntity.ok(clickedPointRiskService.getHistory());
    }

    @GetMapping("/average")
    public ResponseEntity<?> getAverageRisk(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return ResponseEntity.ok(clickedPointRiskService.getAverageRiskForPoint(lat, lon));
    }
}