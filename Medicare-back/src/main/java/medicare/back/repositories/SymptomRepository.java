package medicare.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.Symptom;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    boolean existsByNom(String nom);

    Optional<Symptom> findByNom(String nom);
}
