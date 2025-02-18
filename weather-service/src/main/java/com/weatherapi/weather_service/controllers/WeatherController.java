package com.weatherapi.weather_service.controllers;

import com.weatherapi.weather_service.services.LocationWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final LocationWeatherService locationWeatherService;

// Constructor for dependency injection
    public WeatherController(LocationWeatherService locationWeatherService) {
        this.locationWeatherService = locationWeatherService;
    }

    // Endpoint to get weather by IP
    @GetMapping("/weather-by-ip")
    public ResponseEntity<?> getWeather(@RequestParam(name = "ip", required = false) String ip, HttpServletRequest request) {
        try {
            logger.info("In WeatherController - getWeather: IP = {}", ip);
            return ResponseEntity.ok(locationWeatherService.getWeatherByIp(ip, request));
        } catch (Exception e) {
            logger.error("Error in WeatherController - getWeather: {}", e.getMessage());
            throw e; // Propagate the exception to be handled by GlobalExceptionHandler
        }
    }
}
