package com.weather_forecast.model;

import java.util.List;

public class WeatherForecast {
    private String temperature;
    private String wind;
    private String description;
    private List<ForecastDay> forecast;

    public WeatherForecast(String temperature, String wind, String description, List<ForecastDay> forecast) {
        this.temperature = temperature;
        this.wind = wind;
        this.description = description;
        this.forecast = forecast;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ForecastDay> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastDay> forecast) {
        this.forecast = forecast;
    }

    public static class ForecastDay {
        private String day;
        private String temperature;
        private String wind;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        @Override
        public String toString() {
            return "ForecastDay{" +
                    "day='" + day + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", wind='" + wind + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "temperature='" + temperature + '\'' +
                ", wind='" + wind + '\'' +
                ", description='" + description + '\'' +
                ", forecast=" + forecast +
                '}';
    }
}
