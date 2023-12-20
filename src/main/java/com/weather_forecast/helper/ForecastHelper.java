package com.weather_forecast.helper;


import com.weather_forecast.model.CityWeatherForecast;
import com.weather_forecast.model.WeatherForecast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ForecastHelper {

    public static CityWeatherForecast computeTheAverageForWindAndTemperature(WeatherForecast weatherForecast) {
        CityWeatherForecast cityWeatherForecast = new CityWeatherForecast("", "", "");
        if (!weatherForecast.getForecast().isEmpty()) {
            Float temperatureSum = 0.0F;
            Float windSum = 0.0F;
            for (WeatherForecast.ForecastDay forecastDay : weatherForecast.getForecast()) {
                try {
                    temperatureSum += Integer.parseInt(forecastDay.getTemperature());
                } catch (NumberFormatException e) {
                    temperatureSum += 0;
                }

                try {
                    windSum += Integer.parseInt(forecastDay.getWind());
                } catch (NumberFormatException e) {
                    windSum += 0;
                }
            }
            cityWeatherForecast.setWind(String.valueOf(windSum / weatherForecast.getForecast().size()));
            cityWeatherForecast.setTemperature(String.valueOf(temperatureSum / weatherForecast.getForecast().size()));
        }
        return cityWeatherForecast;
    }

    public static void writeToCsvFile(List<CityWeatherForecast> data) {
        Path path = Path.of("src/main/resources/csv_data/cities_forecast.csv");
        try (BufferedWriter writer = new BufferedWriter(Files.newBufferedWriter(path, StandardOpenOption.CREATE))) {
            // Write header
            writer.write("Name,Temperature,Wind");
            writer.newLine();

            // Write data
            for (CityWeatherForecast cityWeather : data) {
                writer.write(String.format("%s,%s,%s", cityWeather.name, cityWeather.temperature, cityWeather.wind));
                writer.newLine();
            }

            System.out.println("CSV file written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
