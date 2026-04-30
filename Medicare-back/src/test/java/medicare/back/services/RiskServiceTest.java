package medicare.back.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiskServiceTest {

    String calculerNiveau(int total) {
        if (total <= 4)
            return "Faible";
        else if (total <= 6)
            return "Modéré";
        else
            return "Élevé";
    }

    @Test
    public void testNiveauFaible() {
        assertEquals("Faible", calculerNiveau(3));
    }

    @Test
    public void testNiveauModere() {
        assertEquals("Modéré", calculerNiveau(6));
    }

    @Test
    public void testNiveauEleve() {
        assertEquals("Élevé", calculerNiveau(9));
    }
}