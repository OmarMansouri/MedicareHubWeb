package medicare.back.models;

public class Symptom {
    private int id;
    private String nom;
    
    public Symptom() {}
    public Symptom(int id, String nom) { this.id = id; this.nom = nom; }
    
    public int getId() { return id; }
    public String getNom() { return nom; }
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
}
