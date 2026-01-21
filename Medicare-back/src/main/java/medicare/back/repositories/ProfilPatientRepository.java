package medicare.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import medicare.back.models.ProfilPatient;

@Repository
public interface ProfilPatientRepository extends JpaRepository<ProfilPatient, Integer> {
}