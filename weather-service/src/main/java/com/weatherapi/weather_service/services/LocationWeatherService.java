package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.LocationResponse;
import com.weatherapi.weather_service.models.LocationWeatherResponse;
import com.weatherapi.weather_service.models.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Service
public class LocationWeatherService {

    private final LocationService locationService;
    private final WeatherService weatherService;
    private final RedisService redisService;
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LocationWeatherService.class);

    @Value("${local.ip.url}")
    private String localIpUrl;

    public LocationWeatherService(LocationService locationService, WeatherService weatherService, RedisService redisService, RestTemplate restTemplate) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.redisService = redisService;
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves weather information based on the provided IP address.
     * If the IP address is not provided, it extracts the client's IP from the request.
     * It first checks the cache for weather data, if not found, fetches from external services.
     *
     * @param ip the IP address to look up
     * @param request the HTTP request to extract client IP if needed
     * @return LocationWeatherResponse containing weather and location details
     */
    public LocationWeatherResponse getWeatherByIp(String ip, HttpServletRequest request) {
        try {
            // Extract client IP if not provided
            if (ip == null || ip.isEmpty()) {
                ip = extractClientIp(request);
            }

            // Validate the extracted or provided IP address
            if (!isValidIp(ip)) {
                logger.warn("Invalid IP address: {}", ip);
                throw new IllegalArgumentException("Invalid IP address provided in the request: " + ip);
            }

            // Resolve public IP if the IP is a local address
            ip = resolvePublicIp(ip);
            logger.info("Processing weather request for IP: {}", ip);

            // Check cache for weather data
            LocationWeatherResponse cachedWeather = redisService.getWeatherFromCache(ip);
            if (cachedWeather != null) {
                logger.info("Returning cached weather data for IP: {}", ip);
                return cachedWeather;
            }

            // Fetch location and weather data from external services
            logger.info("Weather data not in Redis, fetching from external services");
            LocationResponse location = locationService.getLocationByIp(ip);
            WeatherResponse weather = weatherService.getWeatherByCity(location.getCity());

            // Build the response object
            LocationWeatherResponse response = LocationWeatherResponse.builder()
                    .ip(ip)
                    .weather(weather)
                    .location(location)
                    .build();

            // Save the response to cache
            redisService.saveWeatherToCache(ip, response);
            return response;
        } catch (IllegalArgumentException e) {
            logger.error("Error in getWeatherByIp: Invalid IP address", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in getWeatherByIp: Failed to retrieve weather data", e);
            throw new RuntimeException("Failed to retrieve weather data", e);
        }
    }

    /**
     * Extracts the client's IP address from the HTTP request headers.
     * It checks various headers that might contain the client's IP address.
     *
     * @param request the HTTP request to extract client IP
     * @return the extracted IP address
     */
    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * Resolves the public IP address if the provided IP is a local address.
     *
     * @param ip the IP address to resolve
     * @return the resolved public IP address
     */
    public String resolvePublicIp(String ip) {
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            try {

                String publicIp = restTemplate.getForObject(localIpUrl, String.class);
                return publicIp;
            } catch (Exception e) {
                logger.error("Failed to retrieve public IP", e);
                throw new RuntimeException("Failed to retrieve public IP", e);
            }
        }
        return ip;
    }

    /**
     * Validates if the provided IP address is valid.
     *
     * @param ip the IP address to validate
     * @return true if the IP address is valid, false otherwise
     */
    private boolean isValidIp(String ip) {
        return ip != null && (isIPv6(ip) || isIPv4(ip));
    }

    /**
     * Checks if the provided IP address is IPv6.
     *
     * @param ip the IP address to check
     * @return true if the IP address is IPv6, false otherwise
     */
    private boolean isIPv6(String ip) {
        String ipv6Pattern = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,7}:|" +
                "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
                "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
                "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
                "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
                ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
                "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +
                "::(ffff(:0{1,4}){0,1}:){0,1}" +
                "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3,3}" +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)|" +
                "([0-9a-fA-F]{1,4}:){1,4}:" +
                "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3,3}" +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?))";
        return Pattern.compile(ipv6Pattern).matcher(ip).matches();
    }

    /**
     * Checks if the provided IP address is IPv4.
     *
     * @param ip the IP address to check
     * @return true if the IP address is IPv4, false otherwise
     */
    private boolean isIPv4(String ip) {
        String ipv4Pattern = "^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return Pattern.compile(ipv4Pattern).matcher(ip).matches();
    }
}

