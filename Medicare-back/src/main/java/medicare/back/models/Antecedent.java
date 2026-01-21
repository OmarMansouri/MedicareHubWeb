package medicare.back.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "antecedent")
public class Antecedent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idantecedent")
    private int idAntecedent;
    
    @Column(name = "nom")
    private String nom;
    @Column(name = "type")
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