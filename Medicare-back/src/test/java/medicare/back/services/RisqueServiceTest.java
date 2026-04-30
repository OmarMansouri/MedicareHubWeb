package medicare.back.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.jdbc.core.JdbcTemplate;

import medicare.back.models.ProfilPatient;
import medicare.back.repositories.AntecedentRepository;
import medicare.back.repositories.ClickedPointRiskRepository;
import medicare.back.repositories.DiagnosticSessionRepository;
import medicare.back.repositories.PatientAntecedentRepository;
import medicare.back.repositories.ProbableDiseaseResultRepository;
import medicare.back.repositories.ProfilPatientRepository;

class RisqueServiceTest {

    private ProfilPatientRepository profilPatientRepository;
    private PatientAntecedentRepository patientAntecedentRepository;
    private AntecedentRepository antecedentRepository;
    private DiagnosticSessionRepository diagnosticSessionRepository;
    private ProbableDiseaseResultRepository probableDiseaseResultRepository;
    private ClickedPointRiskRepository clickedPointRiskRepository;
    private JdbcTemplate jdbcTemplate;

    private RisqueService risqueService;

    @BeforeEach
    void setUp() {
        profilPatientRepository = mock(ProfilPatientRepository.class);
        patientAntecedentRepository = mock(PatientAntecedentRepository.class);
        antecedentRepository = mock(AntecedentRepository.class);
        diagnosticSessionRepository = mock(DiagnosticSessionRepository.class);
        probableDiseaseResultRepository = mock(ProbableDiseaseResultRepository.class);
        clickedPointRiskRepository = mock(ClickedPointRiskRepository.class);
        jdbcTemplate = mock(JdbcTemplate.class);

        risqueService = new RisqueService(
            profilPatientRepository,
            patientAntecedentRepository,
            antecedentRepository,
            diagnosticSessionRepository,
            probableDiseaseResultRepository,
            clickedPointRiskRepository,
            jdbcTemplate
        );
    }

    // Test 1 : si le patient n'existe pas en base renvoie une erreur
    @Test
    void testPatientInexistant() {
        when(profilPatientRepository.findById(99)).thenReturn(Optional.empty());

        Map<String, Object> res = risqueService.calculerRisque(99);

        assertTrue(res.containsKey("error"));
        assertEquals("Patient introuvable", res.get("error"));
    }

    // Test 2 : score_max
    @Test
    void testScoreMax() {
        ProfilPatient p = creerProfil(65, 35.0, true, "faible");

        when(profilPatientRepository.findById(1)).thenReturn(Optional.of(p));
        when(patientAntecedentRepository.findByIdIdPatient(1)).thenReturn(new ArrayList<>());
        when(diagnosticSessionRepository.findAll()).thenReturn(new ArrayList<>());
        when(clickedPointRiskRepository.findAllByOrderByIdDesc()).thenReturn(new ArrayList<>());

        Map<String, Object> res = risqueService.calculerRisque(1);
        double score = (Double) res.get("scoreProfil");

        assertEquals(100.0, score);
    }

    // Test 3 : score_min
    @Test
    void testScoreMin() {
        ProfilPatient p = creerProfil(20, 22.0, false, "active");

        when(profilPatientRepository.findById(5)).thenReturn(Optional.of(p));
        when(patientAntecedentRepository.findByIdIdPatient(5)).thenReturn(new ArrayList<>());
        when(diagnosticSessionRepository.findAll()).thenReturn(new ArrayList<>());
        when(clickedPointRiskRepository.findAllByOrderByIdDesc()).thenReturn(new ArrayList<>());

        Map<String, Object> res = risqueService.calculerRisque(5);
        double score = (Double) res.get("scoreProfil");

        assertEquals(0.0, score);
    }

    private ProfilPatient creerProfil(int age, double imc, boolean fumeur, String activite) {
        ProfilPatient p = new ProfilPatient();
        p.setAge(age);
        p.setImc(imc);
        p.setFumeur(fumeur);
        p.setActivitePhysique(activite);
        return p;
    }
}