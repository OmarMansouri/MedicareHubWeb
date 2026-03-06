package medicare.back.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MeteoRiskService {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";
    public int computeMeteoScore(double lat, double lon) {
        int score = 1;
        try {

            String url = BASE_URL
                    + "?latitude=" + lat
                    + "&longitude=" + lon
                    + "&current=temperature_2m,wind_speed_10m,weather_code";
            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                return 1;
            }

            Map current = (Map) response.get("current");

            if (current == null) {
                return 1;
            }

            double wind = 0.0;
            Object windObject = current.get("wind_speed_10m");

            if (windObject != null) {
                wind = Double.parseDouble(windObject.toString());
            }

            int code = 0;
            Object codeObject = current.get("weather_code");
            if (codeObject != null) {
                code = Integer.parseInt(codeObject.toString());
            }

            if (wind < 2.0) {
                score = 3;
            } else if (wind < 5.0) {
                score = 2;
            } else {
                score = 1;
            }

            if (code == 61 || code == 63 || code == 65 || code == 80) {
                score = score - 1;
                if (score < 1) {
                    score = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            score = 1;
        }
        return score;
    }
}