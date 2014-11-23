package com.ryanweather.utils;

/**
 * 储存全局常量
 * 
 * @author Ryan You
 * 
 */

public class GlobalConstants {
	public static final String BAIDU_CHELIANWANG_KEY = "ZxNG6jQfvzjWtbWdcVFeEXZ7"; // 百度API车联网的KEY
	public static final String WEATHER_URL = "http://api.map.baidu.com/telematics/v3/weather?location=广州&output=json&ak="
			+ BAIDU_CHELIANWANG_KEY;
	public static final String LOCAL_JSON =  "local_json";
	public static final String LASTEST_PUBLISH_TIME = "lastest_publish_time";
}
