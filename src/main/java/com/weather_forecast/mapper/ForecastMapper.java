package com.weather_forecast.mapper;

import com.weather_forecast.helper.ForecastHelper;
import com.weather_forecast.model.CityWeatherForecast;
import com.weather_forecast.model.WeatherForecast;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ForecastMapper {

    public List<CityWeatherForecast> toCityWeatherForecastList(Map<String, WeatherForecast> forecastMap) {
           List<CityWeatherForecast> cityWeatherForecastList = new ArrayList<>();
        for (Map.Entry<String, WeatherForecast> entry : forecastMap.entrySet()) {
             cityWeatherForecastList.add(toCityWeatherForecast(entry.getKey(), entry.getValue()));
        }
        return cityWeatherForecastList;
    }

    private CityWeatherForecast toCityWeatherForecast(String city, WeatherForecast weatherForecast) {
        CityWeatherForecast cityWeatherForecast = ForecastHelper.computeTheAverageForWindAndTemperature(weatherForecast);
        cityWeatherForecast.setName(city);
        return cityWeatherForecast;
    }
}
