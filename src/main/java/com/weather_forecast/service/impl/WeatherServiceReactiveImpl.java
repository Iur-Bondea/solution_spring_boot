package com.weather_forecast.service.impl;

import com.weather_forecast.model.WeatherForecast;
import com.weather_forecast.service.WeatherServiceReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Service
public class WeatherServiceReactiveImpl implements WeatherServiceReactive {

    private static final String API_URL = "https://998d8129-2264-4a98-a92e-ba8bde4a4d1c.mock.pstmn.io/{city_name}";

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherServiceReactiveImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Mono<Map<String, Mono<WeatherForecast>>> getForecastForCitiesReactive(List<String> cities) {
        long startTime = System.currentTimeMillis();

        Map<String, Mono<WeatherForecast>> monoMap = new ConcurrentHashMap<>();
        cities.forEach(element -> {
            monoMap.put(element, getWeatherForCity(element));
        });
        return Mono.just(monoMap).doOnTerminate(() -> {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("Total execution time: " + executionTime + " milliseconds");
        });
    }

    private Mono<WeatherForecast> getWeatherForCity(String city) {
        String apiUrl = API_URL.replace("{city_name}", city);

        return Mono.fromCallable(() -> {
            try {
                ResponseEntity<WeatherForecast> responseEntity = restTemplate.getForEntity(apiUrl, WeatherForecast.class);
                return responseEntity.getBody();

            } catch (RuntimeException e) {
                System.err.println("Error fetching data for " + city + ": " + e.getMessage());
                return new WeatherForecast("", "", "", Collections.emptyList());
            }
        }).subscribeOn(Schedulers.parallel());
    }
}
