package medicare.back;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import medicare.back.models.Disease;
import medicare.back.models.DiseaseSymptom;
import medicare.back.models.Symptom;
import medicare.back.models.SymptomType;
import medicare.back.repositories.DiseaseRepository;
import medicare.back.repositories.SymptomRepository;

@Component
public class CsvLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CsvLoader.class);

    private final DiseaseRepository diseaseRepo;
    private final SymptomRepository symptomRepo;

    public CsvLoader(DiseaseRepository diseaseRepo, SymptomRepository symptomRepo) {
        this.diseaseRepo = diseaseRepo;
        this.symptomRepo = symptomRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // On vide les tables avant le chargement
        diseaseRepo.deleteAll();
        symptomRepo.deleteAll();
        logger.info("Nombre de maladies après deleteAll : {}", diseaseRepo.count());
        logger.info("Nombre de symptômes après deleteAll : {}", symptomRepo.count());


        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new ClassPathResource("Diseases.csv").getInputStream())
        );

        reader.readLine(); 
        String line;
        while ((line = reader.readLine()) != null) {
            
            String[] parts = line.split(",");
            if (parts.length < 3) continue; 

            String diseaseName = parts[0];

            String commonSymptomsStr = parts[1].replace("\"", "");
            String[] commonSymptoms = commonSymptomsStr.split(";");
            
            String discriminantSymptomsStr = parts[2].replace("\"", "");
            String[] discriminantSymptoms = discriminantSymptomsStr.split(";");


            // Récupérer ou créer la maladie
            Disease disease = diseaseRepo.findByNom(diseaseName).orElse(new Disease(diseaseName));

            // Ajouter les symptômes communs
            for (String symptomName : commonSymptoms) {
                symptomName = symptomName.trim();
                Symptom symptom = symptomRepo.findByNom(symptomName).orElse(null);
                if (symptom == null) {
                    symptom = new Symptom(symptomName);
                    symptom = symptomRepo.save(symptom);
                }
                DiseaseSymptom ds = new DiseaseSymptom();
                ds.setDisease(disease);
                ds.setSymptom(symptom);
                ds.setType(SymptomType.COMMON);
                disease.getSymptoms().add(ds);
            }

            // Ajouter les symptômes discriminants
            for (String symptomName : discriminantSymptoms) {
                symptomName = symptomName.trim();
                Symptom symptom = symptomRepo.findByNom(symptomName).orElse(null);
                if (symptom == null) {
                    symptom = new Symptom(symptomName);
                    symptom = symptomRepo.save(symptom);
                }
                DiseaseSymptom ds = new DiseaseSymptom();
                ds.setDisease(disease);
                ds.setSymptom(symptom);
                ds.setType(SymptomType.DISCRIMINANT);
                disease.getSymptoms().add(ds);
            }

            diseaseRepo.save(disease);
            logger.info("Maladie '{}' sauvegardée avec {} symptômes", diseaseName, disease.getSymptoms().size());
        }

        logger.info("Nombre final de maladies en base : {}", diseaseRepo.count());
        logger.info("Nombre final de symptômes en base : {}", symptomRepo.count());


        reader.close();
        logger.info("Chargement CSV terminé ");
    }
}
