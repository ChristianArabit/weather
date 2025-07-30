package com.cvarabit.weather.service.Weather.impl;

import com.cvarabit.weather.model.OpenWeatherMap.WeatherResponse;
import com.cvarabit.weather.model.WeatherReading;
import com.cvarabit.weather.model.Weatherstack.CurrentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static com.cvarabit.weather.model.Constants.WEATHER_READING_CACHE_EXPIRATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl weatherServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    private static final String CITY_MELBOURNE = "Melbourne";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(weatherServiceImpl, "weatherstackAccessKey", "dummyKey");
        ReflectionTestUtils.setField(weatherServiceImpl, "openWeatherMapAPIKey", "dummyKey");
    }

    @Test
    void getWeather_ThroughWeatherstack_Success() {
        int mockedResponseWindSpeed = 15;
        int mockedResponseTemp = 25;

        when(restTemplate.getForObject(anyString(), eq(CurrentResponse.class)))
                .thenReturn(mockCurrentResponse(mockedResponseWindSpeed, mockedResponseTemp));

        WeatherReading reading = weatherServiceImpl.getWeatherReading(CITY_MELBOURNE);
        assertNotNull(reading);
        assertEquals(mockedResponseWindSpeed, reading.getWindSpeed());
        assertEquals(mockedResponseTemp, reading.getTemperature());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CurrentResponse.class));
    }

    @Test
    void getWeather_ThroughFallbackOpenWeatherMap_Success() {
        int mockedResponseTemp = 30;
        int mockedResponseWindSpeed = 10;

        when(restTemplate.getForObject(anyString(), eq(CurrentResponse.class)))
                .thenThrow(new RuntimeException());

        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class)))
                .thenReturn(mockWeatherResponse(mockedResponseWindSpeed, mockedResponseTemp));

        WeatherReading reading = weatherServiceImpl.getWeatherReading(CITY_MELBOURNE);
        assertNotNull(reading);
        assertEquals(mockedResponseWindSpeed, reading.getWindSpeed());
        assertEquals(mockedResponseTemp, reading.getTemperature());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CurrentResponse.class));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(WeatherResponse.class));
    }

    @Test
    void getWeather_ThroughCache_Success() {
        int mockedResponseTemp = 25;
        int mockedResponseWindSpeed = 15;

        when(restTemplate.getForObject(anyString(), eq(CurrentResponse.class)))
                .thenReturn(mockCurrentResponse(mockedResponseWindSpeed, mockedResponseTemp));

        WeatherReading reading = weatherServiceImpl.getWeatherReading(CITY_MELBOURNE);
        assertNotNull(reading);
        assertEquals(mockedResponseWindSpeed, reading.getWindSpeed());
        assertEquals(mockedResponseTemp, reading.getTemperature());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CurrentResponse.class));

        try {
            Thread.sleep(WEATHER_READING_CACHE_EXPIRATION - 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WeatherReading cachedReading = weatherServiceImpl.getWeatherReading(CITY_MELBOURNE);

        assertEquals(reading.getWindSpeed(), cachedReading.getWindSpeed());
        assertEquals(reading.getTemperature(), cachedReading.getTemperature());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(CurrentResponse.class));
    }

    @Test
    void getWeather_ExpiredCache_Success() {
        int mockedResponseTemp = 25;
        int mockedResponseWindSpeed = 15;

        when(restTemplate.getForObject(anyString(), eq(CurrentResponse.class)))
                .thenReturn(mockCurrentResponse(mockedResponseWindSpeed, mockedResponseTemp));

        WeatherReading reading = weatherServiceImpl.getWeatherReading(CITY_MELBOURNE);
        assertNotNull(reading);
        assertEquals(mockedResponseWindSpeed, reading.getWindSpeed());
        assertEquals(mockedResponseTemp, reading.getTemperature());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CurrentResponse.class));

        try {
            Thread.sleep(WEATHER_READING_CACHE_EXPIRATION + 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WeatherReading cachedReading = weatherServiceImpl.getWeatherReading(CITY_MELBOURNE);
        assertEquals(reading.getWindSpeed(), cachedReading.getWindSpeed());
        assertEquals(reading.getTemperature(), cachedReading.getTemperature());
        verify(restTemplate, times(2)).getForObject(anyString(), eq(CurrentResponse.class));
    }

    private CurrentResponse mockCurrentResponse(int windSpeed, int temp) {
        CurrentResponse response = new CurrentResponse();
        response.setCurrent(new CurrentResponse.Current());
        response.getCurrent().setWindSpeed(windSpeed);
        response.getCurrent().setTemperature(temp);

        return response;
    }

    private WeatherResponse mockWeatherResponse(int windSpeed, int temp) {
        WeatherResponse response = new WeatherResponse();
        response.setMain(new WeatherResponse.Main());
        response.setWind(new WeatherResponse.Wind());
        response.getMain().setTemp(temp);
        response.getWind().setSpeed(windSpeed);

        return response;
    }
}