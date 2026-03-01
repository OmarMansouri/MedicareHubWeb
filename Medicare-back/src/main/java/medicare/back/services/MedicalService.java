package medicare.back.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import medicare.back.models.Disease;
import medicare.back.models.DiseaseSymptom;
import medicare.back.models.Symptom;
import medicare.back.models.SymptomType;
import medicare.back.repositories.DiseaseRepository;
import medicare.back.repositories.SymptomRepository;

@Service
public class MedicalService {

    private static final Logger log = LoggerFactory.getLogger(MedicalService.class);

    private DiseaseRepository diseaseRepo;
    private SymptomRepository symptomRepo;
    private final BayesDiagnosisService bayesDiagnosisService;
    private static final double seuil = 0.85;

    public MedicalService(DiseaseRepository diseaseRepo, SymptomRepository symptomRepo,BayesDiagnosisService bayesDiagnosisService) {
        this.diseaseRepo = diseaseRepo;
        this.symptomRepo = symptomRepo;
        this.bayesDiagnosisService = bayesDiagnosisService;
    }

    public List<Symptom> getAllSymptoms() {
        return symptomRepo.findAll();
    }

    private String getSymptomNames(List<Long> ids) {
        List<String> noms = new ArrayList<>();
        for (Long id : ids) {
            Symptom s = symptomRepo.findById(id).orElse(null);
            if (s != null) {
                noms.add(s.getNom());
            }
        }
        return String.join(", ", noms);
    }


    
    public Map<String, Object> getNextQuestion(List<Long> symptomsPresents, List<Long> symptomsAbsents) {

        log.info("DÉBUT de la génération de questions");
        log.info("Symptômes présents : {}", getSymptomNames(symptomsPresents));
        log.info("Symptômes absents  : {}", getSymptomNames(symptomsAbsents));


        List<Disease> possibles = new ArrayList<>();
        List<Disease> diseases = diseaseRepo.findAll();

        for (Disease d : diseases) {
            boolean compatible = true;
            boolean aSymptomeCommun = false;

            log.debug("Analyse de la maladie : {}", d.getNom());

            // Vérifier qu'au moins un symptôme commun est présent
            for (DiseaseSymptom ds : d.getSymptoms()) {
                if (ds.getType() == SymptomType.COMMON &&
                    symptomsPresents.contains(ds.getSymptom().getId())) {
                    aSymptomeCommun = true;
                    break;
                }
            }

            if (!aSymptomeCommun) {
                log.debug("Rejetée (aucun symptôme commun présent)");
                compatible = false;
            }

            // Vérifier qu'aucun symptôme discriminant de la maladie n'est absent 
            if (compatible) {
                for (DiseaseSymptom ds : d.getSymptoms()) {
                    if (ds.getType() == SymptomType.DISCRIMINANT &&
                        symptomsAbsents.contains(ds.getSymptom().getId())) {
                        log.debug("Rejetée (symptôme discriminant absent : {})",
                                ds.getSymptom().getNom());
                        compatible = false;
                        break;
                    }
                }
            }

            
            if (compatible) {
                for (DiseaseSymptom ds : d.getSymptoms()) {
                    if (ds.getType() == SymptomType.COMMON &&
                        symptomsAbsents.contains(ds.getSymptom().getId())) {
                        log.debug("Rejetée (symptôme commun absent : {})",
                                ds.getSymptom().getNom());
                        compatible = false;
                        break;
                    }
                }
            }

            if (compatible) {
                log.debug("Maladie possible");
                possibles.add(d);
            }
        }

        log.info("Maladies possibles restantes : {} {}", possibles.size(),possibles.stream().map(Disease::getNom).toList());
        
        Map<Disease, Double> probabilities = new HashMap<>();
        if (!possibles.isEmpty()) {
            probabilities = bayesDiagnosisService.calculateProbabilities(
            symptomsPresents, symptomsAbsents, possibles);

            log.info("Probabilités actuelles :");
            probabilities.forEach((d, p) ->log.info("{} -> {}%", d.getNom(), String.format("%.2f", p * 100)));
        }

        //Vérifier si une maladie a une probabilité≥ 85% 
        for (Map.Entry<Disease, Double> entry : probabilities.entrySet()) {
            if (entry.getValue() >= seuil) {
                log.info("Arrêt : '{}' atteint {}% de probabilité (seuil = {}%)", 
                    entry.getKey().getNom(), 
                    String.format("%.2f", entry.getValue() * 100),
                    String.format("%.0f", seuil * 100));
                return TriResult(probabilities);
            }
        }

        //S'arreter si les symptomes présents correspondent entièrement aux symptomes d'une maladie possible
        for (Disease d : possibles) {
            boolean tousLesSymptomesPresent = true;

            for (DiseaseSymptom ds : d.getSymptoms()) {
                 Long symptomeId = ds.getSymptom().getId();
                if (!symptomsPresents.contains(symptomeId)) {
                    tousLesSymptomesPresent = false;
                    break;
                }
            }

            if (tousLesSymptomesPresent) {
                log.info("Arrêt : tous les symptômes de la maladie '{}' sont présents", d.getNom());
                return TriResult(probabilities);

            }
        }
        
        if (possibles.size() <= 1) {
            log.info("Arrêt : nombre de maladies ≤ 1");
            return TriResult(probabilities);

        }

        
        Map<Symptom, Integer> compteur = new HashMap<>();
        List<Long> dejaTraites = new ArrayList<>();
        dejaTraites.addAll(symptomsPresents);
        dejaTraites.addAll(symptomsAbsents);

        for (Disease d : possibles) {
            for (DiseaseSymptom ds : d.getSymptoms()) {
                if (ds.getType() == SymptomType.DISCRIMINANT &&
                    !dejaTraites.contains(ds.getSymptom().getId())) {

                    compteur.merge(ds.getSymptom(), 1, Integer::sum);
                }
            }
        }

        log.info("Symptômes discriminants candidats : {} ", compteur.keySet().stream().map(Symptom::getNom).toList());

        
        if (compteur.isEmpty()) {
            log.info("Plus de symptômes discriminants, recherche de symptômes COMMON");
            
            for (Disease d : possibles) {
                for (DiseaseSymptom ds : d.getSymptoms()) {
                    if (ds.getType() == SymptomType.COMMON &&
                        !dejaTraites.contains(ds.getSymptom().getId())) {

                        compteur.merge(ds.getSymptom(), 1, Integer::sum);
                    }
                }
            }
            
            log.info("Symptômes communs candidats : {} ", compteur.keySet().stream().map(Symptom::getNom).toList());
        }

        if (compteur.isEmpty()) {
            log.info("Arrêt : aucun symptôme disponible");
            return TriResult(probabilities);
        }

        
        Symptom meilleurSymptome = null;
        int meilleurScore = -1;

        for (Map.Entry<Symptom, Integer> entry : compteur.entrySet()) {
            Symptom symptome = entry.getKey();
            int nombreAvec = entry.getValue();
            int nombreSans = possibles.size() - nombreAvec;

            int score = Math.min(nombreAvec, nombreSans);

            log.debug("Symptôme '{}' → avec: {}, sans: {}, score: {}",
                    symptome.getNom(), nombreAvec, nombreSans, score);

            if (score < 1) {continue;}

            // Choisir le symptôme avec le meilleur score
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurSymptome = symptome;
            }
        }

        // Si aucun symptôme pertinent trouvé
        if (meilleurSymptome == null) {
            log.info("Aucun symptôme suffisamment pertinent trouvé");
            return TriResult(probabilities);
        }

        log.info("Prochaine question : {}", meilleurSymptome.getNom());
        log.info("Fin de la génération de questions");

        return Map.of ("type", "QUESTION",
                       "question" , Map.of("symptomeId" , meilleurSymptome.getId(),
                       "texte", "Avez-vous "+ meilleurSymptome.getNom() + "?")
        );
    }

    public Map<String, Object> TriResult (Map<Disease, Double> probabilities){

        List<Map.Entry<Disease, Double>> liste = new ArrayList<>(probabilities.entrySet());
        liste.sort((a,b) -> Double.compare(b.getValue(),a.getValue()));

        List<Map<String, Object>> top = new ArrayList<>();
        for (int i = 0; i< Math.min(3, liste.size()); i++ ){
            top.add(Map.of("nom", liste.get(i).getKey().getNom() , "probabilite" , liste.get(i).getValue()));  
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("type", "RESULTAT");
        result.put("resultats", top);
        return result;

          
    }
    
    


            
}