package medicare.back.models;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PatientFacteurPositifId implements Serializable {

@Column (name = "idpatient")
private int idPatient;

@Column (name = "idfacteur")
private int idFacteur;

public PatientFacteurPositifId () {}

public PatientFacteurPositifId (int idPatient, int idFacteur){
    this.idPatient = idPatient;
    this.idFacteur = idFacteur;
}
public int getIdPatient() {
    return idPatient;
}
public int getIdFacteur() {
    return idFacteur;
}

@Override
public boolean equals (Object o){
    if (o instanceof PatientFacteurPositifId){
        PatientFacteurPositifId autre = (PatientFacteurPositifId) o;
        return this.idPatient == autre.idPatient && this.idFacteur == autre.idFacteur;
    }
    return false;
}

@Override 
public int hashCode () {
    return Objects.hash (idPatient, idFacteur);
}
}