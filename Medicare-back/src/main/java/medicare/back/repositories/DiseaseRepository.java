package medicare.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    boolean existsByNom(String nom);

    Optional<Disease> findByNom(String nom);
}

