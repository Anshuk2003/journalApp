package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.externalAPIRespone.WeatherAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final String apiKey = "13cec79bd2a39d71b66f3680cf8cc82f";

    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherAPIResponse getWeather(String city) {
        String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);
        ResponseEntity<WeatherAPIResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherAPIResponse.class);
        WeatherAPIResponse body = response.getBody();
        return body;
    }
}