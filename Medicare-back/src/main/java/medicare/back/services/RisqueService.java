package medicare.back.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import medicare.back.models.ProfilPatient;
import medicare.back.repositories.ProfilPatientRepository;

@Service
public class RisqueService {

    private ProfilPatientRepository profilPatientRepository;

    public RisqueService(ProfilPatientRepository profilPatientRepository) {
        this.profilPatientRepository = profilPatientRepository;
    }

    public Map<String, Object> calculerRisque(int idPatient) {

        // chercher le profil du patient
        ProfilPatient profil = profilPatientRepository.findById(idPatient).orElse(null);

        if (profil == null) {
            return Map.of("error", "Patient introuvable");
        }

        // protections contre les valeurs null
        int age = profil.getAge() != null ? profil.getAge() : 0;
        double imc = profil.getImc() != null ? profil.getImc() : 0;
        boolean fumeur = profil.getFumeur() != null && profil.getFumeur();
        String activite = profil.getActivitePhysique() != null
                ? profil.getActivitePhysique().toLowerCase() // transforme le texte en minuscules, sinon chaîne vide
                : "";

        int score = 0;
        List<String> details = new ArrayList<>();

        // calcul selon l'age
        if (age >= 60) {
            score += 25;
            details.add("Age >= 60 : +25");
        } else if (age >= 45) {
            score += 15;
            details.add("Age 45-59 : +15");
        } else if (age >= 30) {
            score += 5;
            details.add("Age 30-44 : +5");
        }

        // calcul selon l'IMC
        if (imc >= 30) {
            score += 20;
            details.add("IMC >= 30 : +20");
        } else if (imc >= 25) {
            score += 10;
            details.add("IMC 25-30 : +10");
        }

        // si le patient fume
        if (fumeur) {
            score += 20;
            details.add("Fumeur : +20");
        }

        // activite physique
        if (activite.contains("faible")) {
            score += 10;
            details.add("Activite faible : +10");
        }

        // determiner le niveau de risque
        String niveau;
        if (score <= 30) {
            niveau = "faible";
        } else if (score <= 60) {
            niveau = "moyen";
        } else {
            niveau = "eleve";
        }

        return Map.of(
                "idPatient", idPatient,
                "score", score,
                "niveau", niveau,
                "details", details
        );
    }
}
