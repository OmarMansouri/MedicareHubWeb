package medicare.back.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "patient_antecedent")
public class PatientAntecedent {

@EmbeddedId 
private PatientAntecedentId id;

@Column(name = "typerelation")
private String typeRelation;

public PatientAntecedent() {}
public PatientAntecedentId getId() { 
  return id; 
 }
public void setId(PatientAntecedentId id) { 
 this.id = id; 
     }

public String getTypeRelation() { 
 return typeRelation;
 }
public void setTypeRelation(String typeRelation) { 
   this.typeRelation = typeRelation; 
}
}
