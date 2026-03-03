package medicare.back.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import medicare.back.models.ProfilPatient;
import medicare.back.repositories.ProfilPatientRepository;


@Service
public class RisqueService {
    private static final Logger log = LoggerFactory.getLogger(RisqueService.class);


    private ProfilPatientRepository profilPatientRepository;

    public RisqueService(ProfilPatientRepository profilPatientRepository) {
        this.profilPatientRepository = profilPatientRepository;
    }

    public Map<String, Object> calculerRisque(int idPatient) {
    log.info("Début du calcul du score de risque");
    log.info("Patient sélectionné : identifiant = " + idPatient);


        // chercher le profil du patient
        ProfilPatient profil = profilPatientRepository.findById(idPatient).orElse(null);

        if (profil == null) {
            log.warn("Profil introuvable en base pour idPatient={}", idPatient);

            return Map.of("error", "Patient introuvable");
        }
        
        // protections contre les valeurs null
        int age = profil.getAge() != null ? profil.getAge() : 0;
        double imc = profil.getImc() != null ? profil.getImc() : 0;
        boolean fumeur = profil.getFumeur() != null && profil.getFumeur();
        String activite = profil.getActivitePhysique() != null
                ? profil.getActivitePhysique().toLowerCase() // transforme le texte en minuscules, sinon chaîne vide
                : "";
    log.info("Profil médical récupéré depuis la base de données");
    log.info("Âge du patient : " + age + " ans");
    log.info("Indice de masse corporelle (IMC) : " + imc);
    log.info("Statut tabagique : " + (fumeur ? "fumeur" : "non fumeur"));
    log.info("Niveau d'activité physique : " + activite);

        int score = 0;
        List<String> details = new ArrayList<>();

        // calcul selon l'age
        log.info("Évaluation du critère : âge");

        if (age >= 60) {
            score += 25;
            details.add("Age >= 60 : +25");
            log.info("Âge élevé détecté (>= 60 ans) : +25 points");

        } else if (age >= 45) {
            score += 15;
            details.add("Age 45-59 : +15");
            log.info("Âge intermédiaire détecté (45 à 59 ans) : +15 points");

        } else if (age >= 30) {
            score += 5;
            details.add("Age 30-44 : +5");
            log.info("Âge modéré détecté (30 à 44 ans) : +5 points");

        }
    log.info("Score après prise en compte de l'âge : " + score);
    log.info("Évaluation du critère : indice de masse corporelle (IMC)");

        // calcul selon l'IMC
        if (imc >= 30) {
            score += 20;
            details.add("IMC >= 30 : +20");
            log.info("IMC élevé (obésité) : +20 points");

        } else if (imc >= 25) {
            score += 10;
            details.add("IMC 25-30 : +10");
            log.info("IMC en surpoids : +10 points");

        }
    log.info("Score après prise en compte de l'IMC : " + score);
    log.info("Évaluation du critère : tabagisme");

        // si le patient fume
        if (fumeur) {
            score += 20;
            details.add("Fumeur : +20");
            log.info("Score après prise en compte du tabac : " + score);

        }
    log.info("Évaluation du critère : activité physique");

        // activite physique
        if (activite.contains("faible")) {
            score += 10;
            details.add("Activite faible : +10");
            log.info("Activité physique faible détectée : +10 points");

        }
    log.info("Score après prise en compte de l'activité physique : " + score);
    log.info("Calcul du niveau de risque à partir du score final");

        // determiner le niveau de risque
        String niveau;
        if (score <= 30) {
            niveau = "faible";
        } else if (score <= 60) {
            niveau = "moyen";
        } else {
            niveau = "eleve";
        }
    log.info("Niveau de risque évalué : " + niveau);
    log.info("Score final calculé : " + score);
    log.info("Fin du calcul du score de risque");

        return Map.of(
                "idPatient", idPatient,
                "score", score,
                "niveau", niveau,
                "details", details
        );
    }
}
