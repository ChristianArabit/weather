package com.cvarabit.weather.service.Weather.impl;

import com.cvarabit.weather.model.Constants;
import com.cvarabit.weather.model.OpenWeatherMap.WeatherResponse;
import com.cvarabit.weather.model.WeatherReading;
import com.cvarabit.weather.model.Weatherstack.CurrentResponse;
import com.cvarabit.weather.service.Weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static com.cvarabit.weather.model.Constants.WEATHER_READING_CACHE_EXPIRATION;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weatherstack.access.key:temp}")
    private String weatherstackAccessKey;

    @Value("${open.weather.map.api.key:temp}")
    private String openWeatherMapAPIKey;

    private HashMap<String, WeatherReading> cachedWeatherReading = new HashMap<>();

    public WeatherReading getWeatherReading(String city) {
        WeatherReading weatherReading = new WeatherReading();
        long timeStampNow = System.currentTimeMillis();

        if (!cachedWeatherReading.containsKey(city) ||
                (timeStampNow - (cachedWeatherReading.get(city).getTimeStamp()) > WEATHER_READING_CACHE_EXPIRATION)) {

            try {
                weatherReading = callWeatherstack(weatherReading, city);
            } catch (Exception e) {
                weatherReading = callOpenWeatherMap(weatherReading, city);
            }

            weatherReading.setTimeStamp(timeStampNow);
            cachedWeatherReading.put(city, new WeatherReading(weatherReading));
        } else {
            weatherReading = new WeatherReading(
                    cachedWeatherReading.get(city).getWindSpeed(),
                    cachedWeatherReading.get(city).getTemperature()
            );
        }

        weatherReading.setTimeStamp(0L);
        return weatherReading;
    };

    private WeatherReading callWeatherstack(WeatherReading weatherReading, String city) {
        CurrentResponse response = restTemplate.getForObject(
                urlBuilderWeatherstack(Constants.WEATHERSTACK_CURRENT_API, city),
                CurrentResponse.class
        );

        if (response != null) {
            weatherReading = new WeatherReading(
                    response.getCurrent().getWindSpeed(),
                    response.getCurrent().getTemperature()
            );
        }

        return weatherReading;
    }

    private WeatherReading callOpenWeatherMap(WeatherReading weatherReading, String city) {
        WeatherResponse response = restTemplate.getForObject(
                urlBuilderOpenWeatherMap(Constants.OPEN_WEATHER_MAP_WEATHER_API, city),
                WeatherResponse.class
        );

        if (response != null) {
            weatherReading = new WeatherReading(
                    (int) Math.round(response.getWind().getSpeed()),
                    (int) Math.round(response.getMain().getTemp())
            );
        }

        return weatherReading;
    }

    private String urlBuilderWeatherstack(String api, String city) {
        String customCity = new String(city);
        if ("melbourne".equalsIgnoreCase(city)) {
            customCity = "Melbourne";
        }

        StringBuilder sb = new StringBuilder()
                .append(Constants.WEATHERSTACK_HOST)
                .append(api)
                .append("?")
                .append("access_key=")
                .append(weatherstackAccessKey)
                .append("&query=")
                .append(customCity)
        ; // Based on documentation: Returns the temperature in the selected unit. (Default: Celsius)

        return sb.toString();
    }

    private String urlBuilderOpenWeatherMap(String api, String city) {
        String customCity = new String(city);
        if ("melbourne".equalsIgnoreCase(city)) {
            customCity = "melbourne,AU";
        }

        StringBuilder sb = new StringBuilder()
                .append(Constants.OPEN_WEATHER_MAP_HOST)
                .append(api)
                .append("?")
                .append("q=")
                .append(customCity)
                .append("&appid=")
                .append(openWeatherMapAPIKey)
                .append("&units=")
                .append("metric") // Based on documentation: For temperature in Celsius use units=metric
        ;

        return sb.toString();
    }
}
