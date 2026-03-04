package medicare.back.controllers;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import medicare.back.models.Antecedent;
import medicare.back.models.Disease;
import medicare.back.models.PatientAntecedent;
import medicare.back.models.PatientAntecedentId;
import medicare.back.repositories.AntecedentRepository;
import medicare.back.repositories.DiseaseRepository;
import medicare.back.repositories.PatientAntecedentRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/antecedents")
public class AntecedentController {

 private final DiseaseRepository diseaseRepo;
 private final AntecedentRepository antecedentRepo;
  private final PatientAntecedentRepository paRepo;

  public AntecedentController(DiseaseRepository diseaseRepo, AntecedentRepository antecedentRepo, PatientAntecedentRepository paRepo)
 {
  this.diseaseRepo = diseaseRepo;
  this.antecedentRepo = antecedentRepo;
  this.paRepo = paRepo;
    }

    // Renvoie la liste de toutes les maladies pour afficher les cases à cocher
    @GetMapping("/diseases")
    public List<Disease> listDiseases() {
    return diseaseRepo.findAll();
    }

    // Enregistre les antécédents sélectionnés par un patient
    @PostMapping("/patient/{idPatient}")
    public Map<String, Object> saveAntecedents(@PathVariable int idPatient, @RequestBody Map<String, Object> body) {

    List<Integer> diseaseIds = (List) body.get("diseaseIds");
      String typeRelation = body.get("typeRelation").toString();

    int saved = 0;
    for (Integer disId : diseaseIds) {
    // on cherche si un antécédent existe déjà pour cette maladie
      Antecedent ant = antecedentRepo.findByDiseaseId((long) disId).orElse(null);

    // si aucun antécédent trouvé on en crée un nouveau
   if (ant == null) {
    Disease d = diseaseRepo.findById((long) disId).orElse(null);
    if (d == null) continue;

   Antecedent nouvelAnt = new Antecedent();
    nouvelAnt.setNom(d.getNom());
    nouvelAnt.setDiseaseId(d.getId());
    ant = antecedentRepo.save(nouvelAnt);
     }

    // on crée le lien entre le patient et l'antécédent
    PatientAntecedent pa = new PatientAntecedent();
    pa.setId(new PatientAntecedentId(idPatient, ant.getIdAntecedent()));
    pa.setTypeRelation(typeRelation);
    paRepo.save(pa);
      saved++; }

    return Map.of("ok", true, "saved", saved);
    } 
  }