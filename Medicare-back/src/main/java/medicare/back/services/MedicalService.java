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

    public MedicalService(DiseaseRepository diseaseRepo, SymptomRepository symptomRepo) {
        this.diseaseRepo = diseaseRepo;
        this.symptomRepo = symptomRepo;
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


    public Symptom getNextQuestion(List<Long> symptomsPresents, List<Long> symptomsAbsents) {

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


        //S'arreter si les symptomes présents correspondent entièrement aux symptomes d'une maladie possible
        for (Disease d : possibles) {
            boolean tousLesSymptomesPresent = true;

            for (DiseaseSymptom ds : d.getSymptoms()) {
                if (!symptomsPresents.contains(ds.getSymptom().getId())) {
                    tousLesSymptomesPresent = false;
                    break;
                }
            }

            if (tousLesSymptomesPresent) {
                log.info("Arrêt : tous les symptômes de la maladie '{}' sont présents", d.getNom());
                return null;
            }
        }
        
        if (possibles.size() <= 1) {
            log.info("Arrêt : nombre de maladies ≤ 1");
            return null;
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
            return null;
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

            
            if (score < 1) {
                continue;
            }

            // Choisir le symptôme avec le meilleur score
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurSymptome = symptome;
            }
        }

        // Si aucun symptôme pertinent trouvé
        if (meilleurSymptome == null) {
            log.info("Aucun symptôme suffisamment pertinent trouvé");
            return null;
        }

        log.info("Prochaine question : {}", meilleurSymptome.getNom());
        log.info("Fin de la génération de questions");

        return meilleurSymptome;
    }
}