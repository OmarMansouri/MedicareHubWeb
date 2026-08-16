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
import medicare.back.models.FacteurPositif;
import medicare.back.models.PatientFacteurPositif;
import medicare.back.models.PatientFacteurPositifId;
import medicare.back.repositories.FacteurPositifRepository;
import medicare.back.repositories.PatientFacteurPositifRepository;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping ("/facteurs")
public class FacteurPositifController {
private final FacteurPositifRepository facteurRepo;
private final PatientFacteurPositifRepository patientFacteurRepo;

public FacteurPositifController (FacteurPositifRepository facteurRepo, PatientFacteurPositifRepository patientFacteurRepo){
    this.facteurRepo = facteurRepo;
    this.patientFacteurRepo = patientFacteurRepo;
}
//liste des facteurs +
@GetMapping ("/liste")
public List<FacteurPositif> getFacteurs(){
    return facteurRepo.findAll();
}

//enregistre les facteurs +
@PostMapping("/patient/{idPatient}")
public Map<String, Object> saveFacteurs (@PathVariable int idPatient,@RequestBody Map<String,Object> body){

System.out.println("Enregistrement des facteurs positifs pour le patient " + idPatient);
List <Integer> facteursIds = (List) body.get ("facteurIds");

int saved = 0;
for (Integer fId : facteursIds){
    PatientFacteurPositif pf = new PatientFacteurPositif();
    pf.setId(new PatientFacteurPositifId(idPatient,fId));
patientFacteurRepo.save(pf);
saved++;
}
return Map.of("ok", true, "saved",saved);
}
}

