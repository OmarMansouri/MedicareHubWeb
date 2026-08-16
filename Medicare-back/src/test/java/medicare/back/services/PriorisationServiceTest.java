package medicare.back.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PriorisationServiceTest {

    @Test
    void diagnosticSeul_ScoreEleve(){
        // Given
        String niveau ="élevé";

        // When
        int resultat = PriorisationService.calculerScorePriorite(niveau, List.of("diagnostic"));

        // Then
        assertEquals(150, resultat);
    }

    @Test
    void antecedentSeul_ScoreIntermediaire(){
        String niveau = "moyen";

        int resultat = PriorisationService.calculerScorePriorite(niveau, List.of("antecedent"));

        assertEquals(80, resultat);
    }

    @Test
    void profilSeul_ScoreFaible() {
        String niveau = "faible";

        int resultat = PriorisationService.calculerScorePriorite(niveau, List.of("profil"));

        assertEquals(40, resultat);
    }

    @Test
    void plusieursSourcesDifferentes_ScoreEleve() {

        assertEquals(170, PriorisationService.calculerScorePriorite("élevé", List.of("diagnostic", "antecedent")));
        assertEquals(160, PriorisationService.calculerScorePriorite("élevé", List.of("diagnostic", "profil")));
    }

    @Test
    void prioriser_trieParNiveauPuisParScore() {
        List<Map<String, Object>> liste = new ArrayList<>();
        liste.add(creerReco(1L, "moyen",  List.of("diagnostic", "antecedent", "profil")));  // 140 -> cle 1140
        liste.add(creerReco(2L, "élevé",  List.of("profil")));  // 110 -> cle 2110                            
        liste.add(creerReco(3L, "faible", List.of("diagnostic"))); //  80 -> cle   80

        new PriorisationService().prioriser(liste);

        // le "élevé" passe devant le "moyen", meme s'il a un score plus petit
        assertEquals(2L, liste.get(0).get("id"));
        assertEquals(1L, liste.get(1).get("id"));
        assertEquals(3L, liste.get(2).get("id"));
    }

    private Map<String, Object> creerReco(Long id, String niveau, List<String> sources) {
        Map<String, Object> reco = new HashMap<>();
        reco.put("id", id);
        reco.put("niveauRisque", niveau);
        reco.put("sources", sources);

        return reco;
    }

}