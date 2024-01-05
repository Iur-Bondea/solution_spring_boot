package com.weather_forecast.controller;

import com.weather_forecast.helper.ForecastHelper;
import com.weather_forecast.mapper.ForecastMapper;
import com.weather_forecast.model.GetWeatherForecastResponse;
import com.weather_forecast.model.WeatherForecast;
import com.weather_forecast.service.WeatherService;
import com.weather_forecast.service.WeatherServiceReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class WeatherController {


    private final WeatherService weatherService;

    private final ForecastMapper forecastMapper;

    private final WeatherServiceReactive weatherServiceReactive;

    @Autowired
    public WeatherController(WeatherService weatherService, ForecastMapper forecastMapper, WeatherServiceReactive weatherServiceReactive) {
        this.weatherService = weatherService;
        this.forecastMapper = forecastMapper;
        this.weatherServiceReactive = weatherServiceReactive;
    }

    @GetMapping("/weather")
    public ResponseEntity<GetWeatherForecastResponse> getForecastForCities(@RequestParam(name = "city")  List<String> cities) {
        Map<String, WeatherForecast> weatherForecastMap = weatherService.getForecastForCities(cities);
        GetWeatherForecastResponse getWeatherForecastResponse = new GetWeatherForecastResponse(forecastMapper.toCityWeatherForecastList(weatherForecastMap));
        ForecastHelper.writeToCsvFile(getWeatherForecastResponse.result);
       return ResponseEntity.ok(getWeatherForecastResponse);
    }

    @GetMapping("/weather/reactive")
    public Mono<ResponseEntity<GetWeatherForecastResponse>> getForecastForCitiesReactive(@RequestParam(name = "city")  List<String> cities) {
        Mono<Map<String, Mono<WeatherForecast>>> weatherForecast = weatherServiceReactive.getForecastForCitiesReactive(cities);
        return weatherForecast
                .flatMap(element ->
                        Flux.fromIterable(element.entrySet())
                                .flatMap(entry ->
                                        entry.getValue().map(weatherForecastValue ->
                                                Map.entry(entry.getKey(), weatherForecastValue)
                                        )
                                )
                                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                )
                .flatMap(mappedValues -> {
                    GetWeatherForecastResponse response = new GetWeatherForecastResponse(forecastMapper.toCityWeatherForecastList(mappedValues));
                    ForecastHelper.writeToCsvFile(response.result);
                    return Mono.just(ResponseEntity.ok(response));
                });
    }
}
