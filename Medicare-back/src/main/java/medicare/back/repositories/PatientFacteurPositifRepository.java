package medicare.back.repositories;

import medicare.back.models.PatientFacteurPositif;
import medicare.back.models.PatientFacteurPositifId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientFacteurPositifRepository extends JpaRepository<PatientFacteurPositif,PatientFacteurPositifId>
{
    List<PatientFacteurPositif>findByIdIdPatient(int idPatient);
}
