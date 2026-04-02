package medicare.back.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import medicare.back.models.Antecedent;
import medicare.back.models.DiagnosticSession;
import medicare.back.models.PatientAntecedent;
import medicare.back.models.ProbableDiseaseResult;
import medicare.back.models.ProfilPatient;
import medicare.back.repositories.AntecedentRepository;
import medicare.back.repositories.DiagnosticSessionRepository;
import medicare.back.repositories.PatientAntecedentRepository;
import medicare.back.repositories.ProbableDiseaseResultRepository;
import medicare.back.repositories.ProfilPatientRepository;

@Service
public class RisqueService {
private static final Logger log = LoggerFactory.getLogger(RisqueService.class);

private ProfilPatientRepository profilPatientRepository;
private PatientAntecedentRepository patientAntecedentRepository;
private AntecedentRepository antecedentRepository;
private DiagnosticSessionRepository diagnosticSessionRepository;
private ProbableDiseaseResultRepository probableDiseaseResultRepository;
private JdbcTemplate jdbcTemplate;

 public RisqueService(ProfilPatientRepository profilPatientRepository, 
 PatientAntecedentRepository patientAntecedentRepository, 
  AntecedentRepository antecedentRepository,DiagnosticSessionRepository diagnosticSessionRepository,
  ProbableDiseaseResultRepository probableDiseaseResultRepository,JdbcTemplate jdbcTemplate) {
 this.profilPatientRepository = profilPatientRepository;
  this.patientAntecedentRepository = patientAntecedentRepository;
  this.antecedentRepository = antecedentRepository; 
 this.diagnosticSessionRepository = diagnosticSessionRepository;
  this.probableDiseaseResultRepository = probableDiseaseResultRepository;
this.jdbcTemplate = jdbcTemplate;}


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
    int age = 0;
    if (profil.getAge() != null) {
     age = profil.getAge();
         }

    double imc = 0;
    if (profil.getImc() != null) {
    imc = profil.getImc();
        }

    boolean fumeur = false;
    if (profil.getFumeur() != null) {
    fumeur = profil.getFumeur();
    }

    String activite = "";
    if (profil.getActivitePhysique() != null) {
    activite = profil.getActivitePhysique().toLowerCase(); } // transforme le texte en minuscules, sinon chaîne vide
                
    log.info("Profil médical récupéré depuis la base de données");
    log.info("Âge du patient : " + age + " ans");
    log.info("Indice de masse corporelle (IMC) : " + imc);
    log.info("Statut tabagique : " + (fumeur ? "fumeur" : "non fumeur"));
    log.info("Niveau d'activité physique : " + activite);

    List<String> details = new ArrayList<>();

        //  Âge (poids 35%) 
        double scoreAge;
        if (age >= 60) {
        scoreAge = 100;
        details.add("Âge ≥ 60 ans : score âge : 100/100");
        } 
        else if (age >= 45) {
        scoreAge = 65;
        details.add("Âge 45–59 ans : score âge : 65/100");
        } 
        else if (age >= 30) {
        scoreAge = 30;
        details.add("Âge 30–44 ans : score âge : 30/100");
        } 
        else { scoreAge = 0;
            details.add("Âge < 30 ans : score âge : 0/100"); 
        }

        // IMC (poids 25%) 
        double scoreImc;
        if (imc >= 30) {
        scoreImc = 100;
        details.add("IMC ≥ 30 (obésité) : score IMC : 100/100");
        } 
        else if (imc >= 25) {
        scoreImc = 50;
        details.add("IMC 25–29.9 (surpoids) : score IMC : 50/100");
        } 
        else {
        scoreImc = 0;
        details.add("IMC < 25 (poids normal) : score IMC : 0/100");
        }

        // Tabac (poids 25%) 
        double scoreTabac;
        if (fumeur) {
        scoreTabac = 100;
        details.add("Fumeur : score tabac : 100/100");
        } else {
        scoreTabac = 0;
        details.add("Non-fumeur : score tabac : 0/100");
        }

        // Activité physique (poids 15%) 
        double scoreActivite;
        if (activite.contains("faible") || activite.contains("sedent")) {
        scoreActivite = 100;
        details.add("Activité physique faible/sédentaire : score activité : 100/100");
        } 
        else if (activite.contains("modere") || activite.contains("modéré")) {
        scoreActivite = 50;
        details.add("Activité physique modérée : score activité : 50/100");
        } 
        else {
         scoreActivite = 0;
        details.add("Activité physique active : score activité : 0/100");
        }

        // Score final 
        double scoreProfil = (scoreAge * 0.35) + (scoreImc * 0.25) + (scoreTabac * 0.25) + (scoreActivite * 0.15);

        // borné entre 0 et 100
        scoreProfil = Math.min(100.0, Math.max(0.0, scoreProfil));
        
    
        double scoreAntecedents = 0;
        List<PatientAntecedent> antecedents = patientAntecedentRepository.findByIdIdPatient(idPatient);
        for (PatientAntecedent pa : antecedents) {
            Antecedent ant = antecedentRepository.findById(pa.getId().getIdAntecedent()).orElse(null);
             if (ant != null) {

            String nom = ant.getNom().toLowerCase();
            String relation = "";
            if (pa.getTypeRelation() != null) {
             relation = pa.getTypeRelation().toLowerCase();
            }

            // score de base selon la maladie (sur 100)
            double scoreBase = 0;

          if (nom.contains("hyperten")) {
            scoreBase = 60;
        } else if (nom.contains("hypotensi")) {
            scoreBase = 30;
        } else if (nom.contains("asth")) {
            scoreBase = 40;
        } else if (nom.contains("diab")) {
            scoreBase = 75;
        } else if (nom.contains("stress")) {
            scoreBase = 30;
        } else if (nom.contains("anxiet") || nom.contains("anxiét")) {
            scoreBase = 30;
        } else if (nom.contains("depress") || nom.contains("dépres")) {
                scoreBase = 30;
        }

        // personnel = poids plein, familial = moitié
        if (relation.contains("fam")) {
         scoreBase = scoreBase / 2;
            }

         // on garde le score le plus élevé parmi tous les antécédents
        if (scoreBase > scoreAntecedents) {
         scoreAntecedents = scoreBase;
            }

       details.add("Antécédent " + ant.getNom() + " (" + pa.getTypeRelation() + ") : score : " + scoreBase + "/100");
        }
       }
        

        // score de risque par maladie
        // on récupère les 3 dernières sessions du patient
        List<DiagnosticSession> toutesLesSessions = diagnosticSessionRepository.findAll();
        List<DiagnosticSession> sessionsPatient = new ArrayList<>();
        for (DiagnosticSession s : toutesLesSessions) {
        if (s.getPatient() != null && s.getPatient().getIdPatient() == idPatient) {
            sessionsPatient.add(s);
        }
          }

        // on garde les 3 dernières sessions seulement
        int debut = Math.max(0, sessionsPatient.size() - 3);
        List<DiagnosticSession> derniersSessions = sessionsPatient.subList(debut, sessionsPatient.size());

        // on collecte les scores du prédiagnostic par maladie
        Map<String, Double> scoresPrediag = new HashMap<>();
        Map<String, Integer> compteur = new HashMap<>();

        for (DiagnosticSession session : derniersSessions) {
        List<ProbableDiseaseResult> resultats = probableDiseaseResultRepository.findAll();
        for (ProbableDiseaseResult r : resultats) {
        if (r.getSession() == null || r.getSession().getId() == null) continue;
        if (!r.getSession().getId().equals(session.getId())) continue;

        String nomMaladie = r.getDisease().getNom();
        double scorePrediag = 0;
        if (r.getScore() != null) {
          scorePrediag = r.getScore() * 100;
         }

         double ancienScore = 0;
        if (scoresPrediag.containsKey(nomMaladie)) {
            ancienScore = scoresPrediag.get(nomMaladie);
            }
        scoresPrediag.put(nomMaladie, ancienScore + scorePrediag);

        int ancienCompteur = 0;
        if (compteur.containsKey(nomMaladie)) {
        ancienCompteur = compteur.get(nomMaladie);
            }
        compteur.put(nomMaladie, ancienCompteur + 1);
            }
        }

        // on calcule la moyenne des scores du prédiagnostic
        for (String nom : scoresPrediag.keySet()) {
        double moyenne = scoresPrediag.get(nom) / compteur.get(nom);
        scoresPrediag.put(nom, moyenne);
        }

        // on récupère les antécédents par maladie
        Map<String, Double> scoresAntecedentsParMaladie = new HashMap<>();
        List<PatientAntecedent> antecedents2 = patientAntecedentRepository.findByIdIdPatient(idPatient);
        for (PatientAntecedent pa : antecedents2) {
        Antecedent ant = antecedentRepository.findById(pa.getId().getIdAntecedent()).orElse(null);
        if (ant == null) continue;

        String nom = ant.getNom().toLowerCase();
        String relation = "";
        if (pa.getTypeRelation() != null) {
        relation = pa.getTypeRelation().toLowerCase();
        }

        double scoreBase = 0;
        if (nom.contains("hyperten")) {scoreBase = 60;
        }
        else if (nom.contains("hypotensi")) { scoreBase = 30;
        } else if (nom.contains("asth")) { scoreBase = 40;
        }else if (nom.contains("diab")) { scoreBase = 75;
        } else if (nom.contains("stress")) { scoreBase = 30;
        }else if (nom.contains("anxiet") || nom.contains("anxiét")) { scoreBase = 30;
        }else if (nom.contains("depress") || nom.contains("dépres")) { scoreBase = 30;}

            if (relation.contains("fam")) {
            scoreBase = scoreBase / 2;
            }
            scoresAntecedentsParMaladie.put(ant.getNom(), scoreBase);
           }

        // on fusionne les deux listes de maladies
          List<String> toutesLesMaladies = new ArrayList<>();
        for (String nom : scoresPrediag.keySet()) {
        toutesLesMaladies.add(nom);
        }
        for (String nom : scoresAntecedentsParMaladie.keySet()) {
        if (!toutesLesMaladies.contains(nom)) {
        toutesLesMaladies.add(nom);
        }
       }

        // score final pour chaque maladie : prédiagnostic (50%) + profil (30%) + antécédent (20%)
        List<Map<String, Object>> podium = new ArrayList<>();

        for (String nomMaladie : toutesLesMaladies) {
         double sp = 0;
        if (scoresPrediag.containsKey(nomMaladie)) {
        sp = scoresPrediag.get(nomMaladie);
        }
        double sa = 0;
        if (scoresAntecedentsParMaladie.containsKey(nomMaladie)) {
         sa = scoresAntecedentsParMaladie.get(nomMaladie);
        }

        double scoreRisque = (sp * 0.50) + (scoreProfil * 0.30) + (sa * 0.20);
        scoreRisque = Math.round(Math.min(100.0, scoreRisque) * 10.0) / 10.0;

        String niveauMaladie;
        if (scoreRisque <= 30) { niveauMaladie = "faible";}
        else if (scoreRisque <= 60) { niveauMaladie = "moyen";}
        else { niveauMaladie = "élevé";}

        Map<String, Object> entree = new HashMap<>();
        entree.put("maladie", nomMaladie);
        entree.put("score", scoreRisque);
        entree.put("niveau", niveauMaladie);
        podium.add(entree);
        }


    // on récupère les risques environnementaux depuis la base 
    //(zone 1 = Paris pour tester le temps que omar finisse sa partie)
    String sql = "SELECT d.nom, r.score_risque FROM risque_maladie_zone r JOIN disease d ON d.id = r.disease_id WHERE r.zone_id = 1";
    List<Map<String, Object>> resultatsEnv = jdbcTemplate.queryForList(sql);

     for (Map<String, Object> ligne : resultatsEnv) {
        String nomMaladie = (String) ligne.get("nom");
         double scoreEnv = Double.parseDouble(ligne.get("score_risque").toString());

    // on cherche si la maladie est déjà dans le podium
    boolean trouvee = false;

    for (Map<String, Object> entree : podium) {
    if (entree.get("maladie").equals(nomMaladie)) {

    //  ajustement du score avec la contribution environnementale
    double ancienScore = (Double) entree.get("score");
     double nouveauScore = Math.round(Math.min(100.0, (ancienScore * 0.90) + (scoreEnv * 0.10)) * 10.0) / 10.0;
     entree.put("score", nouveauScore);
     details.add("Facteur environnemental " + nomMaladie + " (zone Paris) : score : " + scoreEnv + "/100");
     trouvee = true;
      break;
      }
        }

    // si la maladie nest pas encore dans le podium on lajoute
    if (!trouvee) {
    double scoreRisque = Math.round(Math.min(100.0, (scoreProfil * 0.30) + (scoreEnv * 0.10)) * 10.0) / 10.0;
    String niveauMaladie;

    if (scoreRisque <= 30) {
    niveauMaladie = "faible";
    } else if (scoreRisque <= 60) {
    niveauMaladie = "moyen";
    } else {
    niveauMaladie = "élevé";
    }

    Map<String, Object> entree = new HashMap<>();
    entree.put("maladie", nomMaladie);
    entree.put("score", scoreRisque);
    entree.put("niveau", niveauMaladie);
    podium.add(entree);
    }
     }


 //  trie par score décroissant : on garde les 3 premiers

    for (int i = 0; i < podium.size() - 1; i++) {
     for (int j = i + 1; j < podium.size(); j++) {
        double scoreI = (Double) podium.get(i).get("score");
         double scoreJ = (Double) podium.get(j).get("score");
    if (scoreJ > scoreI) {
     Map<String, Object> temp = podium.get(i);
        podium.set(i, podium.get(j));
        podium.set(j, temp);
        }
       }
    }   
    List<Map<String, Object>> top3 = podium.subList(0, Math.min(3, podium.size()));
         log.info("Score profil={} | Podium : {} maladies", scoreProfil, top3.size());


        return Map.of(
                "idPatient", idPatient,
                "scoreProfil", Math.round(scoreProfil * 10.0) / 10.0,
                "details", details,
                "podium", top3
        );
    }


    public Map<String, Object> enregistrerEvaluation(int idPatient, List<Map<String, Object>> top3) {
     String sqlInsertEval = "INSERT INTO evaluation_risque (patient_id, date_calcul) VALUES (?, NOW())";
      jdbcTemplate.update(sqlInsertEval, idPatient);

    String sqlGetId = "SELECT id FROM evaluation_risque WHERE patient_id = ? ORDER BY date_calcul DESC LIMIT 1";
     List<Map<String, Object>> evalResult = jdbcTemplate.queryForList(sqlGetId, idPatient);
       int idEvaluation = Integer.parseInt(evalResult.get(0).get("id").toString());

    int rang = 1;
     for (Map<String, Object> maladie : top3) {
      String nomMaladie = (String) maladie.get("maladie");
       double score = Double.parseDouble(maladie.get("score").toString());
     String niveau = (String) maladie.get("niveau");

        String sqlDisease = "SELECT id FROM disease WHERE nom = ?";
         List<Map<String, Object>> resultatDisease = jdbcTemplate.queryForList(sqlDisease, nomMaladie);

        if (!resultatDisease.isEmpty()) {
          int diseaseId = Integer.parseInt(resultatDisease.get(0).get("id").toString());
        String sqlDetail = "INSERT INTO evaluation_risque_detail (evaluation_id, disease_id, score, niveau, rang) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlDetail, idEvaluation, diseaseId, score, niveau, rang);
            }
          rang++;
         }

    log.info("Évaluation enregistrée avec id=" + idEvaluation);
    return Map.of("ok", true, "idEvaluation", idEvaluation);
  }

    public Map<String, Object> getDerniereEvaluation(int idPatient) {
     String sql = "SELECT date_calcul FROM evaluation_risque WHERE patient_id = ? ORDER BY date_calcul DESC LIMIT 1";
      List<Map<String, Object>> resultats = jdbcTemplate.queryForList(sql, idPatient);
    if (resultats.isEmpty()) {
     return Map.of("message", "Aucune évaluation enregistrée");
     }
     return Map.of("dateCalcul", resultats.get(0).get("date_calcul").toString());
    }

  }
        
            