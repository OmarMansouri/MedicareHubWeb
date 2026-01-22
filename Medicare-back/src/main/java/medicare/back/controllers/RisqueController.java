package medicare.back.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(RisqueController.class);


    private RisqueService risqueService;

    public RisqueController(RisqueService risqueService) {
        this.risqueService = risqueService;
    }

    @GetMapping("/patient/{id}")
    public Map<String, Object> calculerRisque(@PathVariable int id) {
    log.info("RISQUE - Endpoint appelé /risque/patient/{}", id);

    return risqueService.calculerRisque(id);
    }
}