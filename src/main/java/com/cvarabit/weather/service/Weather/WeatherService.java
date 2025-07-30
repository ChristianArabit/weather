package com.cvarabit.weather.service.Weather;

import com.cvarabit.weather.model.WeatherReading;


public interface WeatherService {

    WeatherReading getWeatherReading(String city);
}
