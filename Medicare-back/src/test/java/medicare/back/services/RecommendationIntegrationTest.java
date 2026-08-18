package medicare.back.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

import medicare.back.MedicareBackApplication;

import medicare.back.models.ProfilPatient;
import medicare.back.models.Recommendation;
import medicare.back.repositories.ProfilPatientRepository;
import medicare.back.repositories.RecommendationRepository;

@SpringBootTest(classes = MedicareBackApplication.class)
@Transactional

class RecommendationIntegrationTest{
    @Autowired
    private RecommendationService service;

    @Autowired
    private ProfilPatientRepository profilPatientRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Test
    void recommendationsTrieesParNiveau()
    {
        //un patient fumeur 
        ProfilPatient profil = new ProfilPatient(30, 22.0, true ,"modérée");
        profil.setIdPatient(1);
        profilPatientRepository.save(profil);

        //2 recommandations désordonnées sont enregistrées
        recommendationRepository.save(creerReco("faible", "Arrêter de fumer"));
        recommendationRepository.save(creerReco("élevé" , "Condulter un pneumelogue"));

        Map<String, Object> response = service.genererRecommandations(1);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> liste = (List<Map<String,Object>>) response.get("recommandations");

        assertEquals(2, liste.size());
        assertEquals("élevé" , liste.get(0).get("niveauRisque"));
        assertEquals("faible" , liste.get(1).get("niveauRisque"));
    }

    private Recommendation creerReco(String niveau , String contenu){
        Recommendation r =new Recommendation();
        r.setNiveauRisque(niveau);
        r.setContenu(contenu);
        r.setCategorie("hygiene_de_vie");
        r.setProfil("fumeur");
        return r;
    }

}