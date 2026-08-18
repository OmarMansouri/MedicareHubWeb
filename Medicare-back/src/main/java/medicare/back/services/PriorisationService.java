package medicare.back.services;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PriorisationService {

    public static int calculerScorePriorite(String niveauRisque, List<String> sources){
        int score = 0;
        if (niveauRisque.equals("élevé")){
            score=100;
        } else if (niveauRisque.equals("moyen")) {
            score = 60;
        } else if (niveauRisque.equals("faible")) {
            score= 30;
        }


        if (sources.contains("diagnostic")) {
        score = score + 50;
        }
        if (sources.contains("antecedent")) {
            score = score + 20;
        }
        if (sources.contains("profil")) {
            score = score + 10;
        }
        return score;
    }
    
    public static void prioriser(List<Map<String, Object>> liste) {

        for (Map<String, Object> reco : liste) {
            String niveau = (String) reco.get("niveauRisque");
            List<String> sources = (List<String>) reco.get("sources");

            reco.put("score", calculerScorePriorite(niveau, sources));
        }

        Collections.sort(liste, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> a, Map<String, Object> b) {
                int cleA = cleTri(a);
                int cleB = cleTri(b);
                return cleB - cleA;  
            }
        });
    }
    
    // Génèrer une clé de tri : priorité au niveau de risque, puis au score.
    private static int cleTri(Map<String, Object> reco) {
        String niveau = (String) reco.get("niveauRisque");
        int score = (int) reco.get("score");

        return rangNiveau(niveau) * 1000 + score;
    }

    private static int rangNiveau(String niveau) {
        if ("élevé".equals(niveau)) {
            return 2;
        }
        if ("moyen".equals(niveau)) {
            return 1;
        }
        return 0;  
    }

}


