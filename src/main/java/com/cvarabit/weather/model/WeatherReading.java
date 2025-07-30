package com.cvarabit.weather.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherReading {

    @JsonProperty("wind_speed")
    int windSpeed;

	@JsonProperty("temperature_degrees")
    int temperature;

    @JsonProperty("time_stamp")
    Long timeStamp;

    public WeatherReading() {
    }

    public WeatherReading(int windSpeed, int temperature) {
        super();
        this.windSpeed = windSpeed;
        this.temperature = temperature;
    }

    public WeatherReading(WeatherReading weatherReading) {
        this.windSpeed = weatherReading.getWindSpeed();
        this.temperature = weatherReading.getTemperature();
        this.timeStamp = weatherReading.getTimeStamp();
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

    public Long getTimeStamp() {
        return timeStamp != null && timeStamp == 0 ? null : timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
