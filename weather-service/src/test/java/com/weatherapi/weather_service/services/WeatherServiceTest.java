package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.WeatherApiResponse;
import com.weatherapi.weather_service.models.WeatherCondition;
import com.weatherapi.weather_service.models.WeatherCurrent;
import com.weatherapi.weather_service.models.WeatherResponse;
import com.weatherapi.weather_service.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

    private RestTemplate restTemplate;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // Mocking RestTemplate
        restTemplate = Mockito.mock(RestTemplate.class);
        // Initializing WeatherService with mocked RestTemplate
        weatherService = new WeatherService(restTemplate);
    }

    @Test
    void getWeatherByCity_ReturnsWeatherResponse_WhenApiResponseIsValid() {
        // Setting up a valid WeatherCurrent object
        WeatherCurrent weatherCurrent = WeatherCurrent.builder()
                .temp_c(25.0)
                .humidity(60)
                .condition(new WeatherCondition("Sunny", "http://example.com/icon.png", 1000))
                .build();

        // Setting up a valid WeatherApiResponse object
        WeatherApiResponse apiResponse = WeatherApiResponse.builder()
                .current(weatherCurrent)
                .build();

        // Mocking the RestTemplate response
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(apiResponse);

        // Calling the method under test
        WeatherResponse response = weatherService.getWeatherByCity("San Francisco");

        // Asserting the response
        assertNotNull(response);
        assertEquals(25.0, response.getTemperature());
        assertEquals(60, response.getHumidity());
        assertEquals("Sunny", response.getDescription());
    }

    @Test
    void getWeatherByCity_ThrowsRuntimeException_WhenApiResponseIsNull() {
        // Mocking the RestTemplate response to return null
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(null);

        // Asserting that the method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherByCity("San Francisco");
        });

        assertEquals("Error occurred while fetching weather data.", exception.getMessage());
    }

    @Test
    void getWeatherByCity_ThrowsRuntimeException_WhenCurrentWeatherIsNull() {
        // Setting up an empty WeatherApiResponse object
        WeatherApiResponse apiResponse = WeatherApiResponse.builder().build();

        // Mocking the RestTemplate response
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(apiResponse);

        // Asserting that the method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherByCity("San Francisco");
        });

        assertEquals("Error occurred while fetching weather data.", exception.getMessage());
    }

    @Test
    void getWeatherByCity_ThrowsRuntimeException_WhenRestTemplateThrowsException() {
        // Mocking the RestTemplate to throw an exception
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenThrow(new RuntimeException("API error"));

        // Asserting that the method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherByCity("San Francisco");
        });

        assertEquals("Error occurred while fetching weather data.", exception.getMessage());
    }
}