package medicare.back.services;

import medicare.back.models.Plant;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {
    private static final String CSV_PATH = "Data/global_power_plant_database.csv";
    private List<Plant> loadAllPlants() {
        List<Plant> plants = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(CSV_PATH);
            InputStream inputStream = resource.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );
            plants = br.lines()
                    .skip(1) 
                    .map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                    .filter(cols -> cols.length > 7)
                    .map(cols -> {
                        Plant p = new Plant();
                        try { p.country = cols[0]; } catch (Exception e) { p.country = ""; }
                        try { p.country_long = cols[1]; } catch (Exception e) { p.country_long = ""; }
                        try { p.name = cols[2]; } catch (Exception e) { p.name = ""; }
                        try { p.capacityMw = Double.parseDouble(cols[4]); } catch (Exception e) { p.capacityMw = 0.0; }
                        try { p.latitude = Double.parseDouble(cols[5]); } catch (Exception e) { p.latitude = null; }
                        try { p.longitude = Double.parseDouble(cols[6]); } catch (Exception e) { p.longitude = null; }
                        try { p.primaryFuel = cols[7]; } catch (Exception e) { p.primaryFuel = ""; }
                        double factor;
                        String fuel = p.primaryFuel == null ? "" : p.primaryFuel.toLowerCase();
                        switch (fuel) {
                            case "coal":      factor = 3.0; break;
                            case "oil":       factor = 2.5; break;
                            case "gas":       factor = 1.8; break;
                            case "biomass":   factor = 1.0; break;
                            case "geothermal":factor = 0.6; break;
                            case "hydro":
                            case "solar":
                            case "wind":
                            case "nuclear":   factor = 0.1; break;
                            default:          factor = 0.5; break;
                        }
                        p.emissionCo2Tons = p.capacityMw * factor * 1000;

                        return p;
                    })
                    .filter(p -> p.latitude != null && p.longitude != null)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return plants;
    }

    public List<Plant> getPlantsByCoords(double lat, double lon, double radiusKm) {
        List<Plant> all = loadAllPlants();
        return all.stream()
                .filter(p -> distanceKm(lat, lon, p.latitude, p.longitude) <= radiusKm)
                .collect(Collectors.toList());
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; 
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}