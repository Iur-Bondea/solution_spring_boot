package com.weather_forecast.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetWeatherForecastResponse {
    public List<CityWeatherForecast> result = new ArrayList<>();

    public GetWeatherForecastResponse(List<CityWeatherForecast> resultToMap) {
        Collections.sort(resultToMap, Comparator.comparing(city -> city.name));
        this.result.addAll(resultToMap);
    }
}
