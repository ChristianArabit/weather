package com.cvarabit.weather.model.Weatherstack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentResponse {

    private Current current;

    public Current getCurrent() {
		return current;
	}

	public void setCurrent(Current current) {
		this.current = current;
	}

	@Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        private int temperature;
        
		@JsonProperty("wind_speed")
        private int windSpeed;

		
        public int getTemperature() {
			return temperature;
		}

		public void setTemperature(int temperature) {
			this.temperature = temperature;
		}


		public int getWindSpeed() {
			return windSpeed;
		}

		public void setWindSpeed(int windSpeed) {
			this.windSpeed = windSpeed;
		}
    }
}
