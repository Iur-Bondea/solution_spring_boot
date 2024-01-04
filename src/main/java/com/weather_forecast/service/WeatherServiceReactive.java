package com.weather_forecast.service;

import com.weather_forecast.model.WeatherForecast;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface WeatherServiceReactive {
    Mono<Map<String, WeatherForecast>> getForecastForCitiesReactive(List<String> cities);
}
