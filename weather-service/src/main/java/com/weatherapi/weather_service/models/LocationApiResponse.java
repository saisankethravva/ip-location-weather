package com.weatherapi.weather_service.models;

import lombok.*;

/**
 * Data Transfer Object (DTO) for location API response.
 * This class represents the response containing location details such as IP address, city, country, etc.
 *
 * Example usage:
 * <pre>
 * {@code
 * LocationApiResponse locationApiResponse = LocationApiResponse.builder()
 *     .ip("192.168.1.1")
 *     .hostname("example.com")
 *     .city("San Francisco")
 *     .region("CA")
 *     .country("USA")
 *     .loc("37.7749,-122.4194")
 *     .org("Example Org")
 *     .postal("94103")
 *     .timezone("PST")
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
public class LocationApiResponse {
    private String ip;         // The IP address of the location
    private String hostname;   // The hostname associated with the IP address
    private String city;       // The city of the location
    private String region;     // The region/state of the location
    private String country;    // The country of the location
    private String loc;        // The latitude and longitude of the location, e.g., "42.4734,-83.2219"
    private String org;        // The organization associated with the IP address
    private String postal;     // The postal code of the location
    private String timezone;   // The timezone of the location
}

