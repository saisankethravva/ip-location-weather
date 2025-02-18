package com.weatherapi.weather_service.models;

import lombok.*;

/**
 * Data Transfer Object (DTO) for location response.
 * This class represents the response containing location details such as city and country.
 *
 * Example usage:
 * <pre>
 * {@code
 * LocationResponse locationResponse = LocationResponse.builder()
 *     .city("San Francisco")
 *     .country("USA")
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
public class LocationResponse {
    private String city;    // The city of the location
    private String country; // The country of the location
}