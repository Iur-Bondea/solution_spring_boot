package com.weather_forecast.model;

public class CityWeatherForecast {
    public String name;
    public String temperature;
    public String wind;

    public CityWeatherForecast(String name, String temperature, String wind) {
        this.name = name;
        this.temperature = temperature;
        this.wind = wind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
