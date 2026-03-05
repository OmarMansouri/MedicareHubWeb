package medicare.back.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Service
public class PollutionRiskService {
    private static final String TOKEN = "c94133c79e52620ebfe541a6e18d662a936ec346";
    private static final String BASE_URL = "https://api.waqi.info/feed";
    public int computePollutionScore(double lat, double lon) {

        int score = 1; 
        try {
            String url = BASE_URL 
                    + "/geo:" + lat + ";" + lon
                    + "/?token=" + TOKEN;

            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                return 1;
            }
            Object statusObject = response.get("status");
            if (statusObject == null || !statusObject.toString().equals("ok")) {
                return 1;
            }
            Map data = (Map) response.get("data");
            if (data == null) {
                return 1;
            }
            Object aqiObject = data.get("aqi");

            if (aqiObject == null) {
                return 1;
            }
            int aqi = Integer.parseInt(aqiObject.toString());
            if (aqi <= 50) {
                score = 1;
            } else if (aqi <= 100) {
                score = 2;
            } else {
                score = 3;
            }

        } catch (Exception e) {
            e.printStackTrace();
            score = 1;
        }

        return score;
    }
}