package medicare.back.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.ProbableDiseaseResult;


public interface ProbableDiseaseResultRepository extends JpaRepository<ProbableDiseaseResult, Long> {
}
