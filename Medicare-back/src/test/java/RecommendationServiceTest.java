import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import medicare.back.models.Antecedent;
import medicare.back.models.PatientAntecedent;
import medicare.back.models.PatientAntecedentId;
import medicare.back.models.ProfilPatient;
import medicare.back.models.Recommendation;
import medicare.back.repositories.AntecedentRepository;
import medicare.back.repositories.DiagnosticSessionRepository;
import medicare.back.repositories.PatientAntecedentRepository;
import medicare.back.repositories.ProfilPatientRepository;
import medicare.back.repositories.RecommendationRepository; 
import medicare.back.services.RecommendationService;

class RecommendationServiceTest {
    private RecommendationService service;
    private RecommendationRepository recommendationRepository;
    private ProfilPatientRepository profilPatientRepository;
    private PatientAntecedentRepository patientAntecedentRepository;
    private AntecedentRepository antecedentRepository;
    private DiagnosticSessionRepository diagnosticSessionRepository;

    @BeforeEach
    void setUp() {
        recommendationRepository = mock(RecommendationRepository.class);
        profilPatientRepository =mock(ProfilPatientRepository.class);
        patientAntecedentRepository =mock(PatientAntecedentRepository.class);
        antecedentRepository = mock(AntecedentRepository.class);
        diagnosticSessionRepository =mock(DiagnosticSessionRepository.class);

        service = new RecommendationService(recommendationRepository,profilPatientRepository,patientAntecedentRepository,antecedentRepository,diagnosticSessionRepository);
    }

      
    @Test
    void patientFumeur_retourneRecoFumeur() {
        int idPatient =1;
        ProfilPatient profil = new ProfilPatient(30, 22.0, true, "modérée");
        when(profilPatientRepository.findById(idPatient)).thenReturn(Optional.of(profil));

        Recommendation reco = new Recommendation();
        reco.setContenu("Arrêtez de fumer");
        reco.setCategorie("santé");
        reco.setNiveauRisque("élevé");
        reco.setProfil("fumeur");

        when(recommendationRepository.findByProfil("fumeur")).thenReturn(List.of(reco));

        Map<String, Object> response =service.genererRecommandations(idPatient);
        assertEquals(1, response.get("totalRecommandations"));
    }

      
    @Test
    void antecedentFamilial_chercheRecoNiveauMoyen() {
        int idPatient = 1;
        PatientAntecedent pa= new PatientAntecedent();
        pa.setId(new PatientAntecedentId(idPatient, 10));
        pa.setTypeRelation("familial");

        when(patientAntecedentRepository.findByIdIdPatient(idPatient)).thenReturn(List.of(pa));

        Antecedent ant = new Antecedent("diabète", "type1");
        when(antecedentRepository.findById(10)).thenReturn(Optional.of(ant));
        Recommendation reco = new Recommendation();
        reco.setContenu("Surveillance régulière");
        reco.setCategorie("prévention");
        reco.setNiveauRisque("moyen");
        reco.setAntecedentNom("diabète");
        when(recommendationRepository.findByAntecedentNomAndNiveauRisque("diabète", "moyen")).thenReturn(List.of(reco));
        Map<String, Object> response =service.genererRecommandations(idPatient);
        assertEquals(1, response.get("totalRecommandations"));
    }

    @Test
    void patientSansDonnees_retourneListeVide() {
        //(aucun mock)
        int idPatient = 999;
        Map<String, Object> response =service.genererRecommandations(idPatient);
        assertEquals(0, response.get("totalRecommandations"));
    }
}