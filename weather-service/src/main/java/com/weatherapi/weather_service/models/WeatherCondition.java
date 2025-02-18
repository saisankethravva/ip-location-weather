package com.weatherapi.weather_service.models;

import lombok.*;

/**
 * Data Transfer Object (DTO) for weather condition.
 * This class represents the weather condition details including text description, icon URL, and condition code.
 *
 * Example usage:
 * <pre>
 * {@code
 * WeatherCondition condition = new WeatherCondition("Sunny", "http://example.com/icon.png", 1000);
 * }
 * </pre>
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherCondition {
    private String text; // Text description of the weather condition
    private String icon; // URL to the icon representing the weather condition
    private int code;    // Code representing the weather condition
}
