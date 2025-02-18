package com.weatherapi.weather_service.controller;

import com.weatherapi.weather_service.controllers.WeatherController;
import com.weatherapi.weather_service.models.LocationWeatherResponse;
import com.weatherapi.weather_service.services.LocationWeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherControllerTest {


    @Mock
    private LocationWeatherService locationWeatherService;


    private WeatherController weatherController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
       weatherController= new WeatherController(locationWeatherService);
    }

    @Test
    void getWeather_ReturnsOkResponse_WhenIpIsProvided() {
        String ip = "123.123.123.123";
        LocationWeatherResponse response = new LocationWeatherResponse();
        when(locationWeatherService.getWeatherByIp(eq(ip), any(HttpServletRequest.class))).thenReturn(response);

        ResponseEntity<?> result = weatherController.getWeather(ip, mock(HttpServletRequest.class));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getWeather_ReturnsOkResponse_WhenIpIsNotProvided() {
        LocationWeatherResponse response = new LocationWeatherResponse();
        when(locationWeatherService.getWeatherByIp(eq(null), any(HttpServletRequest.class))).thenReturn(response);

        ResponseEntity<?> result = weatherController.getWeather(null, mock(HttpServletRequest.class));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getWeather_ThrowsException_WhenServiceThrowsException() {
        String ip = "123.123.123.123";
        when(locationWeatherService.getWeatherByIp(eq(ip), any(HttpServletRequest.class))).thenThrow(new RuntimeException("Service error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherController.getWeather(ip, mock(HttpServletRequest.class));
        });

        assertEquals("Service error", exception.getMessage());
    }
}