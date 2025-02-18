package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.WeatherApiResponse;
import com.weatherapi.weather_service.models.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves weather information for a given city.
     *
     * @param city the city to retrieve weather data for
     * @return the weather response containing temperature, humidity, and description
     */
    public WeatherResponse getWeatherByCity(String city) {
        String url = String.format("%s?key=%s&q=%s", weatherApiUrl, weatherApiKey, city);
        logger.info("Fetching weather data for city: {}", city);

        try {
            WeatherApiResponse apiResponse = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (apiResponse == null || apiResponse.getCurrent() == null) {
                logger.error("Failed to retrieve weather data for city: {}", city);
                throw new RuntimeException("Failed to retrieve weather data.");
            }

            // Map data to WeatherResponse DTO
            WeatherResponse weatherResponse = mapToWeatherResponse(apiResponse);
            logger.info("Successfully retrieved weather data for city: {}", city);
            return weatherResponse;

        } catch (Exception e) {
            logger.error("Error occurred while fetching weather data for city: {}", city, e);
            throw new RuntimeException("Error occurred while fetching weather data.", e);
        }
    }


        /**
         * Maps the WeatherApiResponse to WeatherResponse DTO.
         *
         * @param apiResponse the API response to map
         * @return the mapped WeatherResponse
         */
        private WeatherResponse mapToWeatherResponse(WeatherApiResponse apiResponse) {
            // Check if the API response is null and throw an exception if it is
            if (apiResponse == null) {
                throw new IllegalArgumentException("API response is null");
            }

            // Log a warning if the current weather data or condition is null
            if (apiResponse.getCurrent() == null || apiResponse.getCurrent().getCondition() == null) {
                logger.warn("Current weather data cannot be null");
            }
            if (apiResponse.getCurrent().getCondition() == null || apiResponse.getCurrent().getCondition().getText() == null) {
                logger.warn("Weather condition data cannot be null");
            }

            // Create a new WeatherResponse object
            WeatherResponse weatherResponse = new WeatherResponse();

            // Set temperature and humidity if current weather data is available
            if (apiResponse.getCurrent() != null) {
                weatherResponse.setTemperature(apiResponse.getCurrent().getTemp_c());
                weatherResponse.setHumidity(apiResponse.getCurrent().getHumidity());
            }

            // Set description if weather condition data is available
            if (apiResponse.getCurrent() != null && apiResponse.getCurrent().getCondition() != null && apiResponse.getCurrent().getCondition().getText() != null) {
                weatherResponse.setDescription(apiResponse.getCurrent().getCondition().getText());
            }

            // Return the mapped WeatherResponse object
            return weatherResponse;
        }
}