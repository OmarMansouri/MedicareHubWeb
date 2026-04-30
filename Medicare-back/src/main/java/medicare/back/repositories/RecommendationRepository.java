package medicare.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByAntecedentNomAndNiveauRisque(String antecedentNom, String niveauRisque);
    
    List<Recommendation> findByProfil(String Profil);
    
    List<Recommendation> findByDiseaseIdAndNiveauRisque(Long diseaseId, String niveauRisque);
}