package medicare.back.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Antecedent")
public class Antecedent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAntecedent;
    
    private String nom;
    private String type;
    
    public Antecedent() {
    }
    
    public Antecedent(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }

    public int getIdAntecedent() {
        return idAntecedent;
    }

    public void setIdAntecedent(int idAntecedent) {
        this.idAntecedent = idAntecedent;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
} 