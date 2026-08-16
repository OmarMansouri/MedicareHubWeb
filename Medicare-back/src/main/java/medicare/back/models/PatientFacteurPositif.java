package medicare.back.models;
 
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "patient_facteur_positif")
public class PatientFacteurPositif {

    @EmbeddedId
    private PatientFacteurPositifId id;

    public PatientFacteurPositif(){}

    public PatientFacteurPositifId getId () {
        return id;
    }
    public void setId (PatientFacteurPositifId id)
    {
        this.id = id;
    }
}