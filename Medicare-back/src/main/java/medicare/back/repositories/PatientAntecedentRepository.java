package medicare.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.PatientAntecedent;
import medicare.back.models.PatientAntecedentId;

public interface PatientAntecedentRepository extends JpaRepository<PatientAntecedent, PatientAntecedentId> {

    List<PatientAntecedent> findByIdIdPatient(int idPatient);
}
