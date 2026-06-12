package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Cache.AppCache;
import net.engineeringdigest.journalApp.constants.Placeholders;
import net.engineeringdigest.journalApp.externalAPIRespone.WeatherAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Value("${weather.api.key}")
    private String apiKey;


    public WeatherAPIResponse getWeather(String city) {
        String finalAPI = appCache.APP_CACHE.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, apiKey);
        ResponseEntity<WeatherAPIResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherAPIResponse.class);
        WeatherAPIResponse body = response.getBody();
        return body;
    }
}