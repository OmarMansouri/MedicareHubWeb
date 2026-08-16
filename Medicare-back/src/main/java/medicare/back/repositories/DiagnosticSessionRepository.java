package medicare.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import medicare.back.models.DiagnosticSession;

public interface DiagnosticSessionRepository extends JpaRepository<DiagnosticSession, Long> {

    List<DiagnosticSession> findByPatientIdPatient(int idPatient);
    List<DiagnosticSession> findByPatientIdPatientOrderByDateDiagnosticAsc(int idPatient);
}
