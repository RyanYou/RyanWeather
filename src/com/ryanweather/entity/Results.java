package com.ryanweather.entity;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable {

	private static final long serialVersionUID = 1L;
	private String currentCity;
	private List<WeatherData> weather_data;
	private List<Index> index;
	private String pm25;

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public List<WeatherData> getWeather_data() {
		return weather_data;
	}

	public void setWeather_data(List<WeatherData> weather_data) {
		this.weather_data = weather_data;
	}

	public List<Index> getIndex() {
		return index;
	}

	public void setIndex(List<Index> index) {
		this.index = index;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	@Override
	public String toString() {
		return "Results [currentCity=" + currentCity + ", weather_data="
				+ weather_data + ", index=" + index + ", pm25=" + pm25 + "]";
	}

}
