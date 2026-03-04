package medicare.back.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.Antecedent;

public interface AntecedentRepository extends JpaRepository<Antecedent, Integer> {
Optional<Antecedent> findByDiseaseId(Long diseaseId);
}
