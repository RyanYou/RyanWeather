package com.ryanweather.entity;

import java.io.Serializable;

public class CityWeatherResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private int error;
	private String status;
	private String date;
	private Results results;

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "CityWeatherResponse [error=" + error + ", status=" + status
				+ ", date=" + date + ", results=" + results + "]";
	}


}
