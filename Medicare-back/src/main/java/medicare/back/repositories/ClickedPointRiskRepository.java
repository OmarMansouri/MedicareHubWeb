package medicare.back.repositories;

import medicare.back.models.ClickedPointRisk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickedPointRiskRepository extends JpaRepository<ClickedPointRisk, Long> {

    List<ClickedPointRisk> findAllByOrderByIdDesc();

    List<ClickedPointRisk> findByLatitudeBetweenAndLongitudeBetween(
            Double latMin, Double latMax,
            Double lonMin, Double lonMax
    );
}