package com.weatherapi.weather_service.services;

import com.weatherapi.weather_service.models.LocationResponse;
import com.weatherapi.weather_service.models.LocationWeatherResponse;
import com.weatherapi.weather_service.models.WeatherResponse;
import com.weatherapi.weather_service.services.LocationService;
import com.weatherapi.weather_service.services.LocationWeatherService;
import com.weatherapi.weather_service.services.RedisService;
import com.weatherapi.weather_service.services.WeatherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class LocationWeatherServiceTest {

    @Mock
    private LocationService locationService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private RedisService redisService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RestTemplate restTemplate;

    private LocationWeatherService locationWeatherService;

    private String localIpUrl = "https://api.ipify.org";

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        restTemplate = Mockito.mock(RestTemplate.class);
        locationWeatherService = new LocationWeatherService(locationService, weatherService, redisService, restTemplate);
    }

    @Test
    void testGetWeatherByIp_ValidIp() {
        String ip = "68.49.0.196";

        // Setting up a valid WeatherResponse object
        WeatherResponse weatherResponse = WeatherResponse.builder()
                .temperature(25.0)
                .humidity(60)
                .description("Clear sky")
                .build();

        // Setting up a valid LocationResponse object
        LocationResponse locationResponse = LocationResponse.builder()
                .city("San Jose")
                .country("USA")
                .build();

        // Setting up the expected LocationWeatherResponse object
        LocationWeatherResponse expectedResponse = LocationWeatherResponse.builder()
                .ip(ip)
                .location(locationResponse)
                .weather(weatherResponse)
                .build();

        // Mocking the service responses
        when(locationService.getLocationByIp(ip)).thenReturn(locationResponse);
        when(weatherService.getWeatherByCity("San Jose")).thenReturn(weatherResponse);
        when(redisService.getWeatherFromCache(ip)).thenReturn(null);

        // Calling the method under test
        LocationWeatherResponse actualResponse = locationWeatherService.getWeatherByIp(ip, request);

        // Asserting the response
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(redisService).saveWeatherToCache(ip, expectedResponse);
    }

    @Test
    void testGetWeatherByIp_InvalidIp() {
        String ip = "invalid_ip";

        // Asserting that the method throws an IllegalArgumentException for an invalid IP
        assertThrows(IllegalArgumentException.class, () -> locationWeatherService.getWeatherByIp(ip, request));
    }

    @Test
    void testLocalIpUrlInjection() {
        // Asserting that the localIpUrl is correctly injected
        assertNotNull(localIpUrl, "localIpUrl should not be null");
        assertEquals("https://api.ipify.org", localIpUrl, "localIpUrl should be correctly injected");
    }

    @Test
    void testGetWeatherByIp_LocalIp() {
        String testIp = "68.43.0.196";

        // Setting up a valid WeatherResponse object
        WeatherResponse weatherResponse = WeatherResponse.builder()
                .temperature(25.0)
                .humidity(60)
                .description("Clear sky")
                .build();

        // Setting up a valid LocationResponse object
        LocationResponse locationResponse = LocationResponse.builder()
                .city("San Jose")
                .country("USA")
                .build();

        // Mocking the service responses
        Mockito.when(locationWeatherService.resolvePublicIp("127.0.0.1")).thenReturn(testIp);
        Mockito.when(redisService.getWeatherFromCache(testIp)).thenReturn(null);
        Mockito.when(locationService.getLocationByIp(testIp)).thenReturn(locationResponse);
        Mockito.when(weatherService.getWeatherByCity("San Jose")).thenReturn(weatherResponse);

        // Calling the method under test
        LocationWeatherResponse result = assertDoesNotThrow(() -> locationWeatherService.getWeatherByIp("127.0.0.1", request));

        // Asserting the response
        assertNotNull(result);
        assertNotNull(result.getLocation(), "Location should not be null");
        assertEquals("San Jose", result.getLocation().getCity());
        assertEquals(25.0, result.getWeather().getTemperature());
        verify(redisService).saveWeatherToCache(eq(testIp), any(LocationWeatherResponse.class));
    }

    @Test
    void testGetWeatherByIp_CachedData() {
        String ip = "192.168.1.1";
        LocationWeatherResponse cachedResponse = new LocationWeatherResponse();

        // Mocking the RedisService response to return cached data
        when(redisService.getWeatherFromCache(ip)).thenReturn(cachedResponse);

        // Calling the method under test
        LocationWeatherResponse actualResponse = locationWeatherService.getWeatherByIp(ip, request);

        // Asserting the response
        assertEquals(cachedResponse, actualResponse);
        verify(redisService, never()).saveWeatherToCache(anyString(), any(LocationWeatherResponse.class));
    }
}