package medicare.back.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(DiseaseSymptom.DiseaseSymptomId.class)
public class DiseaseSymptom {

    @Id
    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Disease disease;

    @Id
    @ManyToOne
    @JoinColumn(name = "symptom_id")
    private Symptom symptom;

    @Enumerated(EnumType.STRING)
    private SymptomType type;

    public DiseaseSymptom() {}

    public DiseaseSymptom(Disease d, Symptom s, SymptomType t) {
        this.disease = d;
        this.symptom = s;
        this.type = t;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Symptom getSymptom() {
        return symptom;
    }

    public void setSymptom(Symptom symptom) {
        this.symptom = symptom;
    }

    public SymptomType getType() {
        return type;
    }

    public void setType(SymptomType type) {
        this.type = type;
    }

    public static class DiseaseSymptomId implements Serializable {
        
        private Long disease;
        private Long symptom;

        public DiseaseSymptomId() {}

        public DiseaseSymptomId(Long disease, Long symptom) {
            this.disease = disease;
            this.symptom = symptom;
        }

        public Long getDisease() {
            return disease;
        }

        public void setDisease(Long disease) {
            this.disease = disease;
        }

        public Long getSymptom() {
            return symptom;
        }

        public void setSymptom(Long symptom) {
            this.symptom = symptom;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DiseaseSymptomId that = (DiseaseSymptomId) o;
            return Objects.equals(disease, that.disease) && 
                   Objects.equals(symptom, that.symptom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(disease, symptom);
        }
    }
}