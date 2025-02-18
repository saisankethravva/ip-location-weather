package com.weatherapi.weather_service.models;

import lombok.*;



/**
 * Data Transfer Object (DTO) for location and weather response.
 * This class represents the response containing the client's IP address, location details, and weather details.
 *
 * Example usage:
 * <pre>
 * {@code
 * LocationWeatherResponse response = LocationWeatherResponse.builder()
 *     .ip("192.168.1.1")
 *     .location(LocationResponse.builder()
 *         .city("San Francisco")
 *         .country("USA")
 *         .build())
 *     .weather(WeatherResponse.builder()
 *         .temperature(68.0)
 *         .humidity(60)
 *         .build())
 *     .build();
 * }
 * </pre>
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationWeatherResponse {
    private String ip; // IP address of the client
    private LocationResponse location; // Location details of the client
    private WeatherResponse weather; // Weather details for the location
}