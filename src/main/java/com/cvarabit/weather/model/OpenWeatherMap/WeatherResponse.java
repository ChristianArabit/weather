package com.cvarabit.weather.model.OpenWeatherMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private Wind wind;

	private Main main;
	
    public Wind getWind() {
		return wind;
	}


	public void setWind(Wind wind) {
		this.wind = wind;
	}


	public Main getMain() {
		return main;
	}


	public void setMain(Main main) {
		this.main = main;
	}

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {

        private double temp;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {

        private double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }
}
