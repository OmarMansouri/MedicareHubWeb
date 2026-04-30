package medicare.back.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import medicare.back.models.Antecedent;
import medicare.back.models.DiagnosticSession;
import medicare.back.models.PatientAntecedent;
import medicare.back.models.ProbableDiseaseResult;
import medicare.back.models.ProfilPatient;
import medicare.back.models.Recommendation;
import medicare.back.repositories.AntecedentRepository;
import medicare.back.repositories.DiagnosticSessionRepository;
import medicare.back.repositories.PatientAntecedentRepository;
import medicare.back.repositories.ProfilPatientRepository;
import medicare.back.repositories.RecommendationRepository;

@Service
public class RecommendationService {
    private static final Logger log =LoggerFactory.getLogger(RecommendationService.class);

    private final RecommendationRepository recommendationRepository;
    private final ProfilPatientRepository profilPatientRepository;
    private final PatientAntecedentRepository patientAntecedentRepository;
    private final AntecedentRepository antecedentRepository;
    private final DiagnosticSessionRepository diagnosticSessionRepository;

    public RecommendationService(RecommendationRepository recommendationRepository,
            ProfilPatientRepository profilPatientRepository,
            PatientAntecedentRepository patientAntecedentRepository,
            AntecedentRepository antecedentRepository,
            DiagnosticSessionRepository diagnosticSessionRepository) {
        this.recommendationRepository = recommendationRepository;
        this.profilPatientRepository = profilPatientRepository;
        this.patientAntecedentRepository =patientAntecedentRepository;
        this.antecedentRepository = antecedentRepository;
        this.diagnosticSessionRepository = diagnosticSessionRepository;
    }
        
    public Map<String, Object> genererRecommandations(int idPatient){
        log.info("Génération des recommandations pour le patient{}", idPatient);
         
        List<Recommendation> resultats = new ArrayList<>();

        List<Recommendation> recoAntecedents =getRecommandationsParAntecedents(idPatient);
        resultats.addAll(recoAntecedents);

        List<Recommendation> recoProfil =getRecommandationsParProfil(idPatient);
        resultats.addAll(recoProfil);

        List<Recommendation> recoDiag = getRecommandationsParDiagnostic(idPatient);
        resultats.addAll(recoDiag);

        List<Map<String, Object>> listeReco = new ArrayList<>();
        for (Recommendation r : resultats) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("contenu", r.getContenu());
            map.put("categorie", r.getCategorie());
            map.put("niveauRisque", r.getNiveauRisque());

             if (r.getDisease() != null) {
                map.put("source", "diagnostic");
                map.put("detail", r.getDisease().getNom());
            } else if (r.getAntecedentNom() != null) {
                map.put("source", "antecedent");
                map.put("detail", r.getAntecedentNom());
            } else {
                map.put("source", "profil");
                map.put("detail", r.getProfil());
            }

            listeReco.add(map);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("idPatient", idPatient);
        response.put("totalRecommandations", listeReco.size());
        response.put("recommandations", listeReco);

        return response;
    }

    private List<Recommendation> getRecommandationsParAntecedents(int idPatient) {
        List<Recommendation> reco = new ArrayList<>();

        List<PatientAntecedent> antecedents =patientAntecedentRepository.findByIdIdPatient(idPatient);

        for (PatientAntecedent pa : antecedents) {
            Antecedent ant = antecedentRepository.findById(pa.getId().getIdAntecedent()).orElse(null);
            if (ant == null) continue;

            String typeRelation = pa.getTypeRelation() != null ? pa.getTypeRelation().toLowerCase() : "";
            String niveau;
            if (typeRelation.contains("fam")) {
                niveau = "moyen";
            } else {
                niveau = "élevé";
            }

            log.info("Antécédent {} ({}) niveau {}", ant.getNom(),typeRelation, niveau);

            List<Recommendation> recoAnt = recommendationRepository.findByAntecedentNomAndNiveauRisque(ant.getNom(), niveau);
            reco.addAll(recoAnt);
        }


        return reco;
    }

    private List<Recommendation> getRecommandationsParProfil(int idPatient) {
        List<Recommendation> reco = new ArrayList<>();

        ProfilPatient profil =profilPatientRepository.findById(idPatient).orElse(null);
        if (profil == null) return reco;

        if (Boolean.TRUE.equals(profil.getFumeur())) {
            reco.addAll(recommendationRepository.findByProfil("fumeur"));
        }

        if (profil.getImc() != null && profil.getImc() >= 30) {
            reco.addAll(recommendationRepository.findByProfil("obesite"));
        }

        String activite = profil.getActivitePhysique() != null ? profil.getActivitePhysique().toLowerCase() : "";
        if (activite.contains("faible") ||activite.contains("sedent")) {
            reco.addAll(recommendationRepository.findByProfil("sedentaire"));
        }

        if (profil.getAge() != null && profil.getAge() >= 60) {
            reco.addAll(recommendationRepository.findByProfil("age_60_plus"));
        }

        return reco;
    }

    private List<Recommendation> getRecommandationsParDiagnostic(int idPatient) {
        List<Recommendation> reco = new ArrayList<>();

        // Récupérer toutes les sessions du patient
        List<DiagnosticSession> toutesLesSessions = diagnosticSessionRepository.findAll();
        List<DiagnosticSession> sessionsPatient = new ArrayList<>();
        for (DiagnosticSession s : toutesLesSessions) {
            if (s.getPatient() != null && s.getPatient().getIdPatient() == idPatient) {
              sessionsPatient.add(s);
            }
        }

        if (sessionsPatient.isEmpty()) {
            return reco;
        }

        // Pour une diagnostiquée en rang 1 : compter combien de fois, calculer le score moyen, noter la dernière date
        Map<Long, Integer> compteur = new HashMap<>();
        Map<Long, Double> totalscore = new HashMap<>();
        Map<Long, LocalDateTime> derniereDate = new HashMap<>();

        for (DiagnosticSession session : sessionsPatient) {
            List<ProbableDiseaseResult> resultatsSession = session.getResults();
            for (ProbableDiseaseResult r : resultatsSession) {
                if (r.getRang() != 1) continue;

                Long diseaseId = r.getDisease().getId();
                double score = r.getScore() != null ? r.getScore() : 0;
                LocalDateTime dateSession = session.getDateDiagnostic();

                compteur.merge(diseaseId, 1, Integer::sum);
                totalscore.merge(diseaseId, score, Double::sum);
                derniereDate.merge(diseaseId, dateSession,(ancienne, nouvelle) -> nouvelle.isAfter(ancienne) ? nouvelle : ancienne);
            }
        }

          // Pour chacune, déterminer le niveau et chercher la reco
        for (Long diseaseId : compteur.keySet()) {
            int count = compteur.get(diseaseId);
            double scoreMoyen = totalscore.get(diseaseId) / count;
            LocalDateTime derniere = derniereDate.get(diseaseId);

            String niveau = determinerNiveau(count, scoreMoyen, derniere);

            log.info("Diagnostic maladie id={} : {} fois, score moyen={}, niveau={}", diseaseId, count, scoreMoyen, niveau);

            List<Recommendation> recosMaladie = recommendationRepository.findByDiseaseIdAndNiveauRisque(diseaseId, niveau);
            reco.addAll(recosMaladie);
        }

        return reco;
    }

     private String determinerNiveau(int count, double scoreMoyen, LocalDateTime derniereDate) {
        long joursDepuis = ChronoUnit.DAYS.between(derniereDate, LocalDateTime.now());

        //élevé :récent (< 30 jours) et (+3 fois ou (+2 fois avec score > 0.80))
        if (joursDepuis <= 30 && (count >= 3 || (count >= 2 && scoreMoyen > 0.80))) {
            return "élevé";
        }

        //moyen : +2 fois ou (score > 0.70, et pas tres ancien (< 90 jours))
        if (joursDepuis <= 90 && (count >= 2 || scoreMoyen > 0.70)) {
            return "moyen";
        }
        return "faible";
    }

}