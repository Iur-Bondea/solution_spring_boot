package com.weather_forecast.service.impl;

import com.weather_forecast.model.WeatherForecast;
import com.weather_forecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String API_URL = "https://998d8129-2264-4a98-a92e-ba8bde4a4d1c.mock.pstmn.io/{city_name}";

    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("fixedThreadPool")
    private ExecutorService executorService;

    @Autowired
    public WeatherServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, WeatherForecast> getForecastForCities(List<String> cities) {
        long startTime = System.currentTimeMillis();

        Map<String, CompletableFuture<WeatherForecast>> futures = new HashMap<>();

        for (String cityName : cities) {
            String apiUrl = API_URL.replace("{city_name}", cityName);

            CompletableFuture<WeatherForecast> future = CompletableFuture.supplyAsync(() -> {
                try {
                    ResponseEntity<WeatherForecast> responseEntity = restTemplate.getForEntity(apiUrl, WeatherForecast.class);
                    return responseEntity.getBody();
                } catch (RuntimeException e) {
                    System.err.println("Error fetching data for " + cityName + ": " + e.getMessage());
                    return new WeatherForecast("", "", "", Collections.emptyList());
                }
            }, executorService);
            futures.put(cityName, future);
        }

        Map<String, WeatherForecast> results = futures.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().join()));

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Total execution time: " + executionTime + " milliseconds");

        return results;

        /**
         Metrics for 6 requests:
         Total execution time: 1567 milliseconds
         Total execution time: 761 milliseconds
         Total execution time: 611 milliseconds
         Total execution time: 517 milliseconds
         Total execution time: 988 milliseconds
         Total execution time: 729 milliseconds
         */

    }

/**
    @Override        Sequential way
    public Map<String, WeatherForecast> getForecastForCities(List<String> cities) {
        long startTime = System.currentTimeMillis(); // let's measure the execution
        Map<String, WeatherForecast> weatherForecastMap = new HashMap<>();
        for (String cityName : cities) {
            String apiUrl = API_URL.replace("{city_name}", cityName);
           try {
               ResponseEntity<WeatherForecast> response = restTemplate.getForEntity(apiUrl, WeatherForecast.class);
               weatherForecastMap.put(cityName, response.getBody());
           } catch (RuntimeException e) {
               weatherForecastMap.put(cityName, new WeatherForecast("","","", Collections.emptyList()));
           }
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Total execution time: " + executionTime + " milliseconds"); // Total execution time: (aprox) 4134 milliseconds

         Metrics for 6 requests:
         Total execution time: 4134 milliseconds
         Total execution time: 3292 milliseconds
         Total execution time: 3028 milliseconds
         Total execution time: 2498 milliseconds
         Total execution time: 2567 milliseconds
         Total execution time: 3568 milliseconds

        return weatherForecastMap;
    }
 */
}
