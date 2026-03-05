package medicare.back.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import medicare.back.models.Patient;
import medicare.back.repositories.PatientRepository;



@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*") 
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostMapping("/connexion")
    public Patient connexion(@RequestBody Map<String, String> body) {

        Patient patient = patientRepository
                .findByEmail(body.get("email"))
                .orElseThrow(()-> new RuntimeException("Email incorrect"));

        if (!patient.getMotDePasse().equals(body.get("motDePasse"))) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return patient;
    }
}

