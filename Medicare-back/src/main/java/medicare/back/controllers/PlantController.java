package medicare.back.controllers;

import medicare.back.models.Plant;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = {
    "http://localhost:3000",          
    "http://172.31.250.86:3000"       
})public class PlantController {

    private static final String FILE_PATH = "src/main/resources/Data/global_power_plant_database.csv";

    @GetMapping
    public ResponseEntity<List<Plant>> getPlants(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String fuel,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false, defaultValue = "5") Double radiusKm  
    ) throws IOException {

        List<Plant> plants = Files.lines(Paths.get(FILE_PATH))
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
                    switch (p.primaryFuel == null ? "" : p.primaryFuel.toLowerCase()) {
                        case "coal": factor = 3.0; break;
                        case "oil": factor = 2.5; break;
                        case "gas": factor = 1.8; break;
                        case "biomass": factor = 1.0; break;
                        case "geothermal": factor = 0.6; break;
                        case "hydro":
                        case "solar":
                        case "wind":
                        case "nuclear": factor = 0.1; break;
                        default: factor = 0.5; break;
                    }
                    p.emissionCo2Tons = p.capacityMw * factor * 1000;
                    return p;
                })
                .filter(p -> p.latitude != null && p.longitude != null)
                .collect(Collectors.toList());

        if (lat != null && lon != null) {
            final double latRef = lat;
            final double lonRef = lon;
            plants = plants.stream()
                    .filter(p -> distanceKm(latRef, lonRef, p.latitude, p.longitude) <= radiusKm)
                    .collect(Collectors.toList());
        }

        if (country != null && !country.isBlank()) {
            plants = plants.stream()
                    .filter(p -> (p.country != null && p.country.equalsIgnoreCase(country))
                            || (p.country_long != null && p.country_long.equalsIgnoreCase(country)))
                    .collect(Collectors.toList());
        }

        if (fuel != null && !fuel.isBlank()) {
            plants = plants.stream()
                    .filter(p -> p.primaryFuel != null && p.primaryFuel.equalsIgnoreCase(fuel))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(plants);
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