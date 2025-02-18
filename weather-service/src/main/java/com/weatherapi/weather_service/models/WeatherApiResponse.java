package com.weatherapi.weather_service.models;
import lombok.*;

/**
 * Data Transfer Object (DTO) for weather API response.
 * This class represents the response containing weather location and current weather details.
 *
 * Example usage:
 * <pre>
 * {@code
 * WeatherApiResponse response = new WeatherApiResponse(
 *     new WeatherLocation("San Francisco", "USA"),
 *     new WeatherCurrent(68.0, 60)
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
public class WeatherApiResponse {
    private WeatherLocation location; // Location details of the weather
    private WeatherCurrent current;   // Current weather details
}