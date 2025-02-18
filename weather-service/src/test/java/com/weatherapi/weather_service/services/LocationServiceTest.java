package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.LocationApiResponse;
import com.weatherapi.weather_service.models.LocationResponse;
import com.weatherapi.weather_service.services.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class LocationServiceTest {

    private RestTemplate restTemplate;
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        // Mocking RestTemplate
        restTemplate = Mockito.mock(RestTemplate.class);
        // Initializing LocationService with mocked RestTemplate
        locationService = new LocationService(restTemplate);
    }

    @Test
    void getLocationByIp_ReturnsLocationResponse_WhenApiResponseIsValid() {
        // Setting up a valid API response
        LocationApiResponse apiResponse = LocationApiResponse.builder()
            .ip("123.123.123.123")
            .hostname("localhost")
            .city("San Jose")
            .region("California")
            .country("US")
            .loc("37.7749,-122.4194")
            .org("Test Org")
            .postal("94103")
            .timezone("PST")
            .build();

        // Mocking the RestTemplate response
        when(restTemplate.getForObject(Mockito.any(), eq(LocationApiResponse.class))).thenReturn(apiResponse);

        // Calling the method under test
        LocationResponse response = locationService.getLocationByIp("123.123.123.123");

        // Asserting the response
        assertNotNull(response);
        assertEquals("San Jose", response.getCity());
        assertEquals("US", response.getCountry());
    }

    @Test
    void getLocationByIp_ReturnsLocationResponse_WhenApiResponseIsValid3() {
        // Setting up a valid API response with minimal fields
        LocationApiResponse apiResponse = LocationApiResponse.builder()
                .ip("123.123.123.123")
                .city("San Jose")
                .country("US")
                .build();

        // Mocking the RestTemplate response
        when(restTemplate.getForObject(any(), eq(LocationApiResponse.class))).thenReturn(apiResponse);

        // Calling the method under test
        LocationResponse response = locationService.getLocationByIp("123.123.123.123");

        // Asserting the response
        assertNotNull(response);
        assertEquals("San Jose", response.getCity());
        assertEquals("US", response.getCountry());
    }

    @Test
    void testGetLocationByIp_Success() {
        // Setting up a valid API response
        LocationApiResponse apiResponse = new LocationApiResponse();
        apiResponse.setCity("Test City");
        apiResponse.setCountry("Test Country");

        // Mocking the RestTemplate response
        when(restTemplate.getForObject(Mockito.any(), eq(LocationApiResponse.class))).thenReturn(apiResponse);

        // Calling the method under test
        LocationResponse response = locationService.getLocationByIp("127.0.0.1");

        // Asserting the response
        assertNotNull(response);
        assertEquals("Test City", response.getCity());
        assertEquals("Test Country", response.getCountry());
    }

    @Test
    void testGetLocationByIp_ApiResponseNull() {
        // Mocking the RestTemplate response to return null
        when(restTemplate.getForObject(any(), eq(LocationApiResponse.class))).thenReturn(null);

        // Asserting that the method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            locationService.getLocationByIp("127.0.0.1");
        });

        assertEquals("Error retrieving location data", exception.getMessage());
    }

    @Test
    void testGetLocationByIp_ExceptionThrown() {
        // Mocking the RestTemplate to throw an exception
        when(restTemplate.getForObject(anyString(), eq(LocationApiResponse.class))).thenThrow(new RuntimeException("API error"));

        // Asserting that the method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            locationService.getLocationByIp("127.0.0.1");
        });

        assertEquals("Error retrieving location data", exception.getMessage());
    }

    @Test
    void testGetLocationByIp_InvalidIp() {
        // Mocking the RestTemplate response to return null for an invalid IP
        when(restTemplate.getForObject(any(), eq(LocationApiResponse.class))).thenReturn(null);

        // Asserting that the method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            locationService.getLocationByIp("invalid_ip");
        });

        assertEquals("Error retrieving location data", exception.getMessage());
    }

    @Test
    void testMapToLocationResponse_NullApiResponse() {
        // Asserting that the method throws an IllegalArgumentException for null API response
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            locationService.mapToLocationResponse(null);
        });

        assertEquals("Location API response is null", exception.getMessage());
    }

    @Test
    void testMapToLocationResponse_NullCity() {
        // Setting up an API response with null city
        LocationApiResponse apiResponse = new LocationApiResponse();
        apiResponse.setCountry("Test Country");

        // Asserting that the method throws an IllegalArgumentException for null city
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            locationService.mapToLocationResponse(apiResponse);
        });

        assertEquals("City in Location API response is null", exception.getMessage());
    }

    @Test
    void testIsIPv6_True() {
        // Asserting that the method correctly identifies a valid IPv6 address
        assertTrue(locationService.isIPv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
    }

    @Test
    void testIsIPv6_False() {
        // Asserting that the method correctly identifies an invalid IPv6 address
        assertFalse(locationService.isIPv6("127.0.0.1"));
    }
}