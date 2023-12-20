package com.weather_forecast.service;

import com.weather_forecast.model.WeatherForecast;

import java.util.List;
import java.util.Map;

public interface WeatherService {

    Map<String, WeatherForecast> getForecastForCities(List<String> cities);
}
