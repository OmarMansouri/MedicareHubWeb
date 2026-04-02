package medicare.back.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clicked_point_risk")
public class ClickedPointRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public LocalDate date;

    public Double latitude;
    public Double longitude;

    public Integer pollutionScore;
    public Integer meteoScore;
    public Integer industryScore;
    public Integer totalScore;

    public String level;
}