package medicare.back.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import medicare.back.models.Symptom;
import medicare.back.services.MedicalService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class MedicalController {
    private MedicalService service;

    public MedicalController(MedicalService service) {
        this.service = service;
    }

    @GetMapping("/symptomes")
    public List<Symptom> getSymptomes() {
        return service.getAllSymptoms();
    }

    @PostMapping("/prochaineQuestion")
    public Map<String, Object> prochaineQuestion(
            @RequestBody Map<String, List<Long>> body) {

        List<Long> presents = body.get("symptomesPresents");
        List<Long> absents = body.get("symptomesAbsents");
        
        if (presents == null) presents = new ArrayList<>();
        if (absents == null) absents = new ArrayList<>();

        Symptom next = service.getNextQuestion(presents, absents);

        Map<String, Object> response = new HashMap<>();

        if (next == null) {
            response.put("question", null);
            return response;
        }    

        Map<String, Object> question = new HashMap<>();
        question.put("symptomeId", next.getId());
        question.put("texte", "Avez-vous " + next.getNom() + " ?");

        return Map.of("question", question);
    }    


    /*private final List<Symptom> symptomes = Arrays.asList(
        new Symptom(1, "Fièvre"),
        new Symptom(2, "Toux"),
        new Symptom(3, "Fatigue"),
        new Symptom(4, "Maux de tête"),
        new Symptom(5, "Courbatures"),
        new Symptom(6, "Perte de goût"),
        new Symptom(7, "Perte d'odorat"),
        new Symptom(8, "Mal de gorge"),
        new Symptom(9, "Congestion nasale")
    );

    private final List<Disease> maladies = Arrays.asList(
        new Disease("Grippe", Arrays.asList(1,2,3), Arrays.asList(5)),
        new Disease("COVID-19", Arrays.asList(1,2,3), Arrays.asList(6,7)),
        new Disease("Rhume", Arrays.asList(2,8), Arrays.asList(9)),
        new Disease("Allergie saisonnière", Arrays.asList(2,9), Arrays.asList(7))
    );

    @GetMapping("/symptomes")
    public List<Symptom> getSymptomes() {
        return symptomes;
    }

    @PostMapping("/prochaineQuestion")
    public Map<String, Object> prochaineQuestion(@RequestBody Map<String, List<Integer>> body) {
        List<Integer> symptomesPoses = body.getOrDefault("symptomesPoses", new ArrayList<>());
        Map<Integer, Integer> frequence = new HashMap<>();

        List<Disease> possibles = new ArrayList<>();
        for (Disease m : maladies) {
            for (Integer id : m.getSymptomesCommuns()) {
                if (symptomesPoses.contains(id)) {
                    possibles.add(m);
                    break;
                }
            }
        }

        // Calculer la fréquence des symptômes discriminants non encore posés
        for (Disease m : possibles) {
            for (Integer id : m.getSymptomesDiscriminants()) {
                if (!symptomesPoses.contains(id)) {
                    frequence.put(id, frequence.getOrDefault(id, 0) + 1);
                }
            }
        }

        // S'il n'y a plus de questions à poser
        if (frequence.isEmpty()) {
            return Map.of("question", null);
        }

        // Choisir le symptome le plus fréquent de façon sécurisée
        Integer nextId = frequence.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)   
                .orElse(null);

        if (nextId == null) {
            return Map.of("question", null);
        }

        Symptom s = symptomes.stream()
                .filter(sym -> sym.getId() == nextId)
                .findFirst()
                .orElse(null);

        if (s == null) {
            return Map.of("question", null);
        }

        
        Map<String, Object> question = new HashMap<>();
        question.put("symptomeId", s.getId());
        question.put("texte", "Avez-vous " + s.getNom().toLowerCase() + " ?");

        return Map.of("question", question);
    }*/
}
