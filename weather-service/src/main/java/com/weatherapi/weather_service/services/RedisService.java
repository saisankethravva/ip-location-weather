package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.LocationWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_PREFIX = "weather:";

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Retrieves weather data from the cache for the given IP address.
     *
     * @param ip the IP address to retrieve weather data for
     * @return the cached LocationWeatherResponse or null if not found
     */
    public LocationWeatherResponse getWeatherFromCache(String ip) {
        String key = CACHE_PREFIX + ip;
        try {
            LocationWeatherResponse cachedWeather = (LocationWeatherResponse) redisTemplate.opsForValue().get(key);
            if (cachedWeather != null) {
                logger.info("Cache hit for IP: {}", ip);
            } else {
                logger.info("Cache miss for IP: {}", ip);
            }
            return cachedWeather;
        } catch (Exception e) {
            logger.error("Error retrieving weather data from cache for IP: {}", ip, e);
            return null;
        }
    }

    /**
     * Saves weather data to the cache with a TTL of 3 minutes.
     *
     * @param ip the IP address to associate with the weather data
     * @param weatherResponse the weather data to cache
     */
    public void saveWeatherToCache(String ip, LocationWeatherResponse weatherResponse) {
        String key = CACHE_PREFIX + ip;
        try {
            redisTemplate.opsForValue().set(key, weatherResponse, 10, TimeUnit.MINUTES);
            logger.info("Saved weather data to cache for IP: {} with TTL 10 minutes", ip);
        } catch (Exception e) {
            logger.error("Error saving weather data to cache for IP: {}", ip, e);
        }
    }
}


