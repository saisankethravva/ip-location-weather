package com.weatherapi.weather_service.models;

import lombok.*;

/**
 * Represents a weather location with various attributes such as name, region, country, latitude, longitude, timezone ID, and local time.
 * This class uses Lombok annotations to generate boilerplate code like getters, setters, constructors, etc.
 *
 * Example usage:
 * WeatherLocation location = new WeatherLocation("San Francisco", "California", "USA", 37.7749, -122.4194, "America/Los_Angeles", "2023-10-10 10:00");
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherLocation {
    private String name;       // Name of the location
    private String region;     // Region of the location
    private String country;    // Country of the location
    private double lat;        // Latitude of the location
    private double lon;        // Longitude of the location
    private String tz_id;      // Timezone ID of the location
    private String localtime;  // Local time at the location
}
