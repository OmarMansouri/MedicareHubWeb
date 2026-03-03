package medicare.back.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class DiagnosticSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateDiagnostic;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "session" , cascade = CascadeType.ALL)
    private List<ProbableDiseaseResult> results = new ArrayList<>();

    public LocalDateTime getDateDiagnostic() {return dateDiagnostic;}

    public void setDateDiagnostic (LocalDateTime dateDiagnostic){
        this.dateDiagnostic = dateDiagnostic;
    }
    public Patient getPatient (){return patient;}
    public void setPatient (Patient patient){
        this.patient = patient;
    }


    public List<ProbableDiseaseResult> getResults() { return results; }
    public void setResults(List<ProbableDiseaseResult> results) { 
        this.results = results; 
    }

    

}
