import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import medicare.back.models.Disease;
import medicare.back.models.DiseaseSymptom;
import medicare.back.models.Symptom;
import medicare.back.models.SymptomType;
import medicare.back.services.BayesDiagnosisService;

public class BayesDiagnosisServiceTest {
    private BayesDiagnosisService service;

    @BeforeEach
    void setUp() {
        service = new BayesDiagnosisService(null);
    }

    @Test
    void aucunSymptome_distributionUniforme() {
        Disease maladieA = new Disease("A");
        maladieA.setId(1L);

        Disease maladieB = new Disease("B");
        maladieB.setId(2L);


        Map<Disease, Double> result = service.calculateProbabilities(List.of(),List.of(),List.of(maladieA, maladieB));
        assertEquals(0.5, result.get(maladieA), 0.0001);
        assertEquals(0.5, result.get(maladieB), 0.0001);
    }


    @Test
    void maladieAvecSymptomePresent_estPlusProbable() {
        Symptom symptome1 = new Symptom("fièvre");
        symptome1.setId(1L);

        Disease maladieA = new Disease("A");
        maladieA.setId(1L);

        maladieA.getSymptoms().add(new DiseaseSymptom(maladieA, symptome1, SymptomType.COMMON));

        Disease maladieB = new Disease("B");
        maladieB.setId(2L);
        Map<Disease, Double> result = service.calculateProbabilities(List.of(1L),List.of(),List.of(maladieA, maladieB));
        assertTrue(result.get(maladieA) >result.get(maladieB));
    }

    @Test
    void symptomeAbsent_reduitLaProbabilite() {
        Symptom symptome1 = new Symptom("toux");
        symptome1.setId(1L);
      
        Disease maladieA = new Disease("A");
        maladieA.setId(1L);
        maladieA.getSymptoms().add(new DiseaseSymptom(maladieA, symptome1, SymptomType.DISCRIMINANT));

        Disease maladieB = new Disease("B");
        maladieB.setId(2L);

        Map<Disease, Double> result = service.calculateProbabilities(List.of(),List.of(1L),List.of(maladieA, maladieB));
        assertTrue(result.get(maladieA)< result.get(maladieB));
    }

}
