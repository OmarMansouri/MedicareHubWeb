package medicare.back.services;

public class PriorisationService {

    public static int calculerScorePriorite(String niveauRisque, String source, long joursDepuis, int nbFacteurs){
        double score = 0;
        if (niveauRisque.equals("élevé")){
            score=100;
        } else if (niveauRisque.equals("moyen")) {
            score = 60;
        } else if (niveauRisque.equals("faible")) {
            score= 30;
        }


        if (source.equals("diagnostic")){
            score = score * 1.5;
        } else if (source.equals("antecedent")) {
            score = score * 1.2;
        } 

        if (joursDepuis<= 30){
            score = score + 20;
        } else if (joursDepuis<= 90) {
            score = score + 10;
        } 

        score = score + 15 * (nbFacteurs - 1);
        return (int) Math.round(score);
    }     

}


