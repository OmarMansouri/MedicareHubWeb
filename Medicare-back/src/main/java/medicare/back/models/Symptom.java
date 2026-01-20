package medicare.back.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    public Symptom() {}

    public Symptom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    /*private int id;
    private String nom;
    
    public Symptom() {}
    public Symptom(int id, String nom) { this.id = id; this.nom = nom; }
    
    public int getId() { return id; }
    public String getNom() { return nom; }
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }*/
}
