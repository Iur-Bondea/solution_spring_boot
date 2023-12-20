package com.weather_forecast.model;

import java.util.ArrayList;
import java.util.List;

public class GetWeatherForecastResponse {
    public List<CityWeatherForecast> result = new ArrayList<>();

    public GetWeatherForecastResponse(List<CityWeatherForecast> resultToMap) {
        this.result.addAll(resultToMap);
    }
}
