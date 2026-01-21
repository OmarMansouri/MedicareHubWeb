package medicare.back.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import medicare.back.services.RisqueService;

@RestController
@RequestMapping("/risque")
@CrossOrigin(origins = "*")
public class RisqueController {

    private RisqueService risqueService;

    public RisqueController(RisqueService risqueService) {
        this.risqueService = risqueService;
    }

    @GetMapping("/patient/{id}")
    public Map<String, Object> calculerRisque(@PathVariable int id) {
        return risqueService.calculerRisque(id);
    }
}