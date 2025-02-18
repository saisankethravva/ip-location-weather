package com.weatherapi.weather_service.models;
import lombok.*;
/**
 * Represents the weather response containing temperature, humidity, and description.
 * This class uses Lombok annotations to generate boilerplate code like getters, setters, constructors, etc.
 *
 * Example usage:
 * <pre>
 *     WeatherResponse weather = new WeatherResponse(25.5, 60, "Sunny");
 *     System.out.println(weather.getTemperature()); // 25.5
 *     System.out.println(weather.getHumidity()); // 60
 *     System.out.println(weather.getDescription()); // Sunny
 * </pre>
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherResponse {
    /**
     * The temperature in degrees Celsius.
     */
    private double temperature;

    /**
     * The humidity percentage.
     */
    private int humidity;

    /**
     * A brief description of the weather.
     */
    private String description;
}
