package medicare.back.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriorisationServiceTest {

    @Test
    void risqueEleveEtRecent_ScoreEleve() {
        // Given
        String niveau ="élevé";
        String source = "diagnostic";
        long jours = 10;
        int nbFacteurs = 2;

        // When
        int resultat = PriorisationService.calculerScorePriorite(niveau, source, jours, nbFacteurs);

        // Then
        assertEquals(185, resultat);
    }

    @Test
    void risqueMoyenEtAncien_ScoreIntermediaire() {
        String niveau = "moyen";
        String source = "antecedent";
        long jours = 60;
        int facteurs = 1;

        int resultat = PriorisationService.calculerScorePriorite(niveau, source, jours, facteurs);

        assertEquals(82, resultat);
    }

    @Test
    void risqueFaibleEtTresAncien_ScoreFaible() {
        String niveau = "faible";
        String source = "autre";
        long jours = 120;
        int facteurs = 1;

        int resultat = PriorisationService.calculerScorePriorite(niveau, source, jours, facteurs);

        assertEquals(30, resultat);
    }

    @Test
    void plusieursFacteurs_ScoreEleve() {
        String niveau = "moyen";
        String source = "diagnostic";
        long jours = 20;
        int facteurs = 3;

        int resultat = PriorisationService.calculerScorePriorite(niveau, source, jours, facteurs);

        assertEquals(140, resultat);
    }

}