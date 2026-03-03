package medicare.back.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.Patient;


public interface PatientRepository extends JpaRepository<Patient, Integer> {}
