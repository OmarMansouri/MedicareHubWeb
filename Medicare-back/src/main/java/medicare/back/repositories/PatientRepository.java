package medicare.back.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.Patient;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByEmail(String email);
}
