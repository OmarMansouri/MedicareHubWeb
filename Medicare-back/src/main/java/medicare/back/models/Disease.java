package medicare.back.models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    @OneToMany(mappedBy = "disease", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DiseaseSymptom> symptoms = new ArrayList<>();

    public Disease() {}

    public Disease(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public List<DiseaseSymptom> getSymptoms() {
        return symptoms;
    }
    /*private String nom;
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
    public void setSymptomesDiscriminants(List<Integer> disc) { this.symptomesDiscriminants = disc; }*/
}