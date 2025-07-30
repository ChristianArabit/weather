package com.cvarabit.weather.controller;

import com.cvarabit.weather.model.WeatherReading;
import com.cvarabit.weather.service.Weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/weather")
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping
    public WeatherReading getWeather(@RequestParam(value = "city", defaultValue = "Melbourne") String city) {
        return weatherService.getWeatherReading(city);
    }
}