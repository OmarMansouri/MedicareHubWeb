package medicare.back.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Profil_Patient")
public class ProfilPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPatient;
    
    private Integer age;
    private Double imc;
    private Boolean fumeur;
    private String activitePhysique;
    
    public ProfilPatient() {
    }
    
    public ProfilPatient(Integer age, Double imc, Boolean fumeur, String activitePhysique) {
        this.age = age;
        this.imc = imc;
        this.fumeur = fumeur;
        this.activitePhysique = activitePhysique;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Boolean getFumeur() {
        return fumeur;
    }

    public void setFumeur(Boolean fumeur) {
        this.fumeur = fumeur;
    }

    public String getActivitePhysique() {
        return activitePhysique;
    }

    public void setActivitePhysique(String activitePhysique) {
        this.activitePhysique = activitePhysique;
    }
}