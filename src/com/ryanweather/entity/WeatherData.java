package com.ryanweather.entity;

import java.io.Serializable;

public class WeatherData implements Serializable {

	private static final long serialVersionUID = 1L;
	private String date;
	private String dayPictureUrl;
	private String nightPictureUrl;
	private String weather;
	private String wind;
	private String temperature;
	private String currentTemperature;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayPictureUrl() {
		return dayPictureUrl;
	}

	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}

	public String getNightPictureUrl() {
		return nightPictureUrl;
	}

	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}


	public String getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(String currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	@Override
	public String toString() {
		return "WeatherData [date=" + date + ", dayPictureUrl=" + dayPictureUrl
				+ ", nightPictureUrl=" + nightPictureUrl + ", weather="
				+ weather + ", wind=" + wind + ", temperature=" + temperature
				+ ", currentTemperature=" + currentTemperature + "]";
	}

	
}
