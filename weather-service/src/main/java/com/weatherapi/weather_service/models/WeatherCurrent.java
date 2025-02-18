package com.weatherapi.weather_service.models;

import lombok.*;

/**
 * Data Transfer Object (DTO) for current weather details.
 * This class represents the current weather details including temperature, humidity, wind, and condition.
 *
 * Example usage:
 * <pre>
 * {@code
 * WeatherCondition condition = new WeatherCondition("Sunny", "http://example.com/icon.png", 1000);
 * WeatherCurrent current = new WeatherCurrent(
 *     "2023-10-10 14:00", 20.0, 68.0, 1, 50, 21.0, 69.8, 10.0, 16.1, "NW", 315, condition
 * );
 * }
 * </pre>
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherCurrent {
    private String last_updated; // Last updated time of the weather data
    private double temp_c;       // Temperature in Celsius
    private double temp_f;       // Temperature in Fahrenheit
    private int is_day;          // Indicator if it is day (1) or night (0)
    private int humidity;        // Humidity percentage
    private double feelslike_c;  // Feels like temperature in Celsius
    private double feelslike_f;  // Feels like temperature in Fahrenheit
    private double wind_mph;     // Wind speed in miles per hour
    private double wind_kph;     // Wind speed in kilometers per hour
    private String wind_dir;     // Wind direction as a string
    private int wind_degree;     // Wind direction in degrees
    private WeatherCondition condition; // Weather condition details
}