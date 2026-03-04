package medicare.back.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PatientAntecedentId implements Serializable {

@Column(name = "idpatient")
private int idPatient;

@Column(name = "idantecedent")
private int idAntecedent;

public PatientAntecedentId() {}

public PatientAntecedentId(int idPatient, int idAntecedent) {
 this.idPatient = idPatient;
  this.idAntecedent = idAntecedent;
    }

 public int getIdPatient() { 
  return idPatient; 
    }
public int getIdAntecedent() { 
    return idAntecedent; 
    }

@Override
public boolean equals(Object o) {
if (o instanceof PatientAntecedentId) {
     PatientAntecedentId autre = (PatientAntecedentId) o;
// les deux clés sont égales si les deux ids sont identiques
    return this.idPatient == autre.idPatient && this.idAntecedent == autre.idAntecedent;
    }
  return false;
  }

@Override
public int hashCode() {
return Objects.hash(idPatient, idAntecedent);
}
}
