package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.LocationApiResponse;
import com.weatherapi.weather_service.models.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

@Service
public class LocationService {

    @Value("${location.ipv4.api.url}")
    public String locationApiUrlV4;
    @Value("${location.ipv6.api.url}")
    public String locationApiUrlV6;

    @Value("${location.api.key}")
    public String locationApiKey;

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final RestTemplate restTemplate;
    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocationResponse getLocationByIp(String ip) {
        String url = buildUrl(ip);
        logger.info("Requesting location data from URL: {}", url);

        try {
            URI uri = new URI(url);
            LocationApiResponse apiResponse = restTemplate.getForObject(uri, LocationApiResponse.class);
            if (apiResponse == null) {
                throw new RuntimeException("Failed to retrieve location data.");
            }
            logger.info("Received response: {}", apiResponse);

            return mapToLocationResponse(apiResponse);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error retrieving location data for IP: {}", ip, e);
            throw new RuntimeException("Error retrieving location data", e);
        }
    }

    public String buildUrl(String ip) {
        String url;
        if (isIPv6(ip)) {
            url = String.format("%s/%s?token=%s", locationApiUrlV6, ip, locationApiKey);
        } else {
            url = String.format("%s/%s?token=%s", locationApiUrlV4, ip, locationApiKey);
        }
        return url;
    }

    public LocationResponse mapToLocationResponse(LocationApiResponse apiResponse) {
        if (apiResponse == null) {
            throw new IllegalArgumentException("Location API response is null");
        }
        if (apiResponse.getCity() == null) {
            throw new IllegalArgumentException("City in Location API response is null");
        }
        if (apiResponse.getCountry() == null) {
            logger.warn("Country in Location API response is null");
        }
        LocationResponse locationResponse = new LocationResponse();
        if (apiResponse.getCity() != null) {
            locationResponse.setCity(apiResponse.getCity());
        }
        if (apiResponse.getCountry() != null) {
            locationResponse.setCountry(apiResponse.getCountry());
        }
        return locationResponse;
    }

    public boolean isIPv6(String ip) {
        String ipv6Pattern = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,7}:|" +
                "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
                "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
                "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
                "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
                ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
                "::(ffff(:0{1,4}){0,1}:){0,1}" +
                "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3,3}" +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)|" +
                "([0-9a-fA-F]{1,4}:){1,4}:" +
                "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3,3}" +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?))";
        return Pattern.compile(ipv6Pattern).matcher(ip).matches();
    }
}