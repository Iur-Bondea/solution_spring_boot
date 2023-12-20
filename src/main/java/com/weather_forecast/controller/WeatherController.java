package com.weather_forecast.controller;

import com.weather_forecast.helper.ForecastHelper;
import com.weather_forecast.mapper.ForecastMapper;
import com.weather_forecast.model.GetWeatherForecastResponse;
import com.weather_forecast.model.WeatherForecast;
import com.weather_forecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherController {


    private final WeatherService weatherService;

    private final ForecastMapper forecastMapper;

    @Autowired
    public WeatherController(WeatherService weatherService, ForecastMapper forecastMapper) {
        this.weatherService = weatherService;
        this.forecastMapper = forecastMapper;
    }

    @GetMapping("/weather")
    public ResponseEntity<GetWeatherForecastResponse> getForecastForCities(@RequestParam(name = "city")  List<String> cities) {
        Map<String, WeatherForecast> weatherForecastMap = weatherService.getForecastForCities(cities);
        GetWeatherForecastResponse getWeatherForecastResponse = new GetWeatherForecastResponse(forecastMapper.toCityWeatherForecastList(weatherForecastMap));
        ForecastHelper.writeToCsvFile(getWeatherForecastResponse.result);
       return ResponseEntity.ok(getWeatherForecastResponse);
    }
}
