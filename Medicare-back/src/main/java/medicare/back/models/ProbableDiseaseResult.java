package medicare.back.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class ProbableDiseaseResult {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;
    private int rang;
    
    @ManyToOne 
    @JoinColumn(name = "session_id")
    private DiagnosticSession session;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Disease Disease;

    public void setSession(DiagnosticSession session){
        this.session = session;
    }

    public void setDisease (Disease disease){
        this.Disease = disease;
    } 

    public void setScore(Double score){
        this.score = score;
    }

    public void setRang(int rang){
        this.rang = rang ;
    }

   public int getRang(){ return rang; }

   public Double getScore() { 
    return score; }

   public DiagnosticSession getSession() { 
    return session; }

   public Disease getDisease() { return Disease; }

}
