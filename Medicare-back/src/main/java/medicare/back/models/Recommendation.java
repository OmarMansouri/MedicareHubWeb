package medicare.back.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recommendation")
public class Recommendation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "disease_id") private Disease disease;
  @Column(name = "antecedent_nom") 
  private String antecedentNom;

  @Column(name = "categorie", nullable = false)
  private String categorie;

  @Column(name = "niveau_risque", nullable = false)
  private String niveauRisque;

  @Column(name = "profil")
  private String profil;

  @Column(name = "contenu", nullable = false)
  private String contenu;

  public Recommendation() {}

  public Long getId() {
    return id; 
  }

  public void setId(Long id) { 
    this.id = id; 
  }

  public String getAntecedentNom() { 
    return antecedentNom; 
  }

  public void setAntecedentNom(String antecedentNom) {
    this.antecedentNom = antecedentNom; 
  }

  public String getCategorie() { 
    return categorie; 
  }

  public void setCategorie(String categorie) { 
    this.categorie = categorie; 
  }

  public String getNiveauRisque() { 
    return niveauRisque; 
  }

  public void setNiveauRisque(String niveauRisque) { 
    this.niveauRisque = niveauRisque; 
  }

  public String getProfil() { 
    return profil; 
  }

  public void setProfil(String Profil) { 
    this.profil = Profil; 
  }

  public String getContenu() { 
    return contenu; 
  }
  public void setContenu(String contenu) { 
    this.contenu = contenu; 
  }

  public Disease getDisease() { 
    return disease; 
  }

  public void setDisease(Disease disease) {
    this.disease = disease; 
  }


}


      