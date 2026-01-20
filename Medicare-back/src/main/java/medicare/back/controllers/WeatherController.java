package medicare.back.controllers.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://172.31.250.86:3000") 
public class WeatherController {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    @GetMapping("/coords")
    public ResponseEntity<?> getWeatherByCoords(
            @RequestParam double lat,
            @RequestParam double lon) {
        try {
            String url = BASE_URL
                    + "?latitude=" + lat
                    + "&longitude=" + lon
                    + "&current=temperature_2m,wind_speed_10m,weather_code"
                    + "&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m";

            RestTemplate restTemplate = new RestTemplate();
            Object response = restTemplate.getForObject(url, Object.class);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
           
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erreur lors de la récupération météo : " + e.getMessage());
        }
    }
}