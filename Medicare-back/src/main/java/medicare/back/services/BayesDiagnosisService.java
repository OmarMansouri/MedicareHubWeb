package medicare.back.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import medicare.back.models.Disease;
import medicare.back.models.DiseaseSymptom;
import medicare.back.models.SymptomType;
import medicare.back.repositories.DiseaseRepository;

@Service
public class BayesDiagnosisService {

    private static final Logger log = LoggerFactory.getLogger(BayesDiagnosisService.class);
    private final DiseaseRepository diseaseRepo;

    public BayesDiagnosisService(DiseaseRepository diseaseRepo) {
        this.diseaseRepo = diseaseRepo;
    }

    public Map<Disease, Double> calculateProbabilities(List<Long> symptomsPresent,List<Long> symptomsAbsent,List<Disease> possibleDiseases) {

        

        Map<Disease, Double> scores = new HashMap<>();
        double totalScore = 0.0;

        for (Disease disease : possibleDiseases) {

            // Probabilité a priori (toutes avec la meme chance au départ)
            double probability = 1.0 / possibleDiseases.size();

            List<Long> symptomsDeLaMaladie = disease.getSymptoms()
                .stream()
                .map(ds -> ds.getSymptom().getId())
                .toList();
            
            for (DiseaseSymptom ds : disease.getSymptoms()) {

                Long symptomId = ds.getSymptom().getId();

                // Probabilités selon type(0.7 pour C ou 0.9 pour D)
                double pSymptomGivenDisease;

                if (ds.getType() == SymptomType.COMMON) {
                    pSymptomGivenDisease = 0.7;
                } else {
                    pSymptomGivenDisease = 0.9;
                }

                if (symptomsPresent.contains(symptomId)) {
                    probability *= pSymptomGivenDisease;
                }

                if (symptomsAbsent.contains(symptomId)) {
                    probability *= (1 - pSymptomGivenDisease);
                }
            }

            for (Long symptomId : symptomsPresent) {
                if (!symptomsDeLaMaladie.contains(symptomId)) {
                    probability *= 0.1;
                    log.debug("Symptôme parasite {} pour {}", symptomId, disease.getNom());
                }
            }

            scores.put(disease, probability);
            totalScore += probability;
            log.debug("Maladie {} → score brut = {}", disease.getNom(), probability);
        }

        if (totalScore == 0) {
            log.warn("Total score = 0, distribution uniforme");
            Map<Disease, Double> normalized = new HashMap<>();
            for (Disease disease : possibleDiseases) {
                normalized.put(disease, 1.0 / possibleDiseases.size());
            }
            return normalized;
        }

        // Normalisation
        Map<Disease, Double> normalized = new HashMap<>();

        for (Map.Entry<Disease, Double> entry : scores.entrySet()) {
            normalized.put(entry.getKey(), entry.getValue() / totalScore);
        }

        return normalized;
    }
}
