package medicare.back.models;


import java.util.List;

public class Disease {
    private String nom;
    private List<Integer> symptomesCommuns;
    private List<Integer> symptomesDiscriminants;

    public Disease() {}
    public Disease(String nom, List<Integer> comm, List<Integer> disc) {
        this.nom = nom;
        this.symptomesCommuns = comm;
        this.symptomesDiscriminants = disc;
    }

    public String getNom() { return nom; }
    public List<Integer> getSymptomesCommuns() { return symptomesCommuns; }
    public List<Integer> getSymptomesDiscriminants() { return symptomesDiscriminants; }

    public void setNom(String nom) { this.nom = nom; }
    public void setSymptomesCommuns(List<Integer> comm) { this.symptomesCommuns = comm; }
    public void setSymptomesDiscriminants(List<Integer> disc) { this.symptomesDiscriminants = disc; }
}