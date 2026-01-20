package medicare.back.controllers.sample;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/air")
@CrossOrigin(origins = "http://172.31.250.86:3000") 
public class AirQualityController {

    private static final String TOKEN = "c94133c79e52620ebfe541a6e18d662a936ec346"; 
    private static final String BASE_URL = "https://api.waqi.info/feed";

    @GetMapping("/coords")
    public ResponseEntity<?> getAirQuality(@RequestParam double lat, @RequestParam double lon) {
        String url = String.format("%s/geo:%f;%f/?token=%s", BASE_URL, lat, lon, TOKEN);

        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject(url, Object.class);

        return ResponseEntity.ok(response);
    }
}