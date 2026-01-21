package medicare.back.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "profil_patient")
public class ProfilPatient {

    @Id
    @Column(name = "idpatient")

    private int idPatient;
    @Column(name = "age")
    private Integer age;
    @Column(name = "imc")
    private Double imc;
        @Column(name = "fumeur")
    private Boolean fumeur;
    @Column(name = "activitephysique")
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