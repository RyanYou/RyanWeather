package com.ryanweather.app;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;

import com.example.ryanweahter.R;
import com.ryanweather.dao.WeatherDao;
import com.ryanweather.entity.CityWeatherResponse;

public class WeatherApplication extends Application{
	public static WeatherApplication instance;
	public CityWeatherResponse mainCityWeatherResponse;
	private Map<String,Integer> mWeatherIconMap;
	private WeatherDao mWeatherDao;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initData();
		initWeahterIcon();
	}
	
	public void initData(){
		mWeatherDao = new WeatherDao(this);
		mainCityWeatherResponse = mWeatherDao.getCityWeatherResponse(mWeatherDao.getJsonStringFromFile());
	}
	
	/**
	 * 初始化主界面用到的天气图
	 * 
	 */
	private void initWeahterIcon() {
		mWeatherIconMap = new HashMap<String, Integer>();
		mWeatherIconMap.put("晴", R.drawable.biz_plugin_weather_qing);//晴|
		mWeatherIconMap.put("多云", R.drawable.biz_plugin_weather_duoyun);//多云|
		mWeatherIconMap.put("阴", R.drawable.biz_plugin_weather_yin);//阴|
		mWeatherIconMap.put("阵雨", R.drawable.biz_plugin_weather_xiaoyu);//阵雨|
		mWeatherIconMap.put("雷阵雨", R.drawable.biz_plugin_weather_leizhenyu);//雷阵雨|
		mWeatherIconMap.put("雷阵雨伴有冰雹", R.drawable.biz_plugin_weather_leizhenyubingbao);//雷阵雨伴有冰雹|
		mWeatherIconMap.put("雨夹雪", R.drawable.biz_plugin_weather_yujiaxue);//雨夹雪|
		mWeatherIconMap.put("小雨", R.drawable.biz_plugin_weather_xiaoyu);//小雨|
		mWeatherIconMap.put("中雨", R.drawable.biz_plugin_weather_zhongyu);//中雨|
		mWeatherIconMap.put("大雨", R.drawable.biz_plugin_weather_dayu);//大雨|
		mWeatherIconMap.put("暴雨", R.drawable.biz_plugin_weather_baoyu);//暴雨|
		mWeatherIconMap.put("大暴雨", R.drawable.biz_plugin_weather_dabaoyu);//大暴雨|
		mWeatherIconMap.put("特大暴雨", R.drawable.biz_plugin_weather_tedabaoyu);//特大暴雨|
		mWeatherIconMap.put("阵雪", R.drawable.biz_plugin_weather_zhenxue);//阵雪|
		mWeatherIconMap.put("小雪", R.drawable.biz_plugin_weather_xiaoxue);//小雪|
		mWeatherIconMap.put("中雪", R.drawable.biz_plugin_weather_zhongxue);//中雪|
		mWeatherIconMap.put("大雪", R.drawable.biz_plugin_weather_daxue);//大雪|
		mWeatherIconMap.put("暴雪", R.drawable.biz_plugin_weather_baoxue);//暴雪|
		mWeatherIconMap.put("雾", R.drawable.biz_plugin_weather_wu);//雾|
		mWeatherIconMap.put("冻雨", R.drawable.biz_plugin_weather_xiaoyu);//冻雨|
		mWeatherIconMap.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);//沙尘暴|
		mWeatherIconMap.put("小雨转中雨", R.drawable.biz_plugin_weather_xiaoyu);//小雨转中雨|
		mWeatherIconMap.put("中雨转大雨", R.drawable.biz_plugin_weather_zhongyu);//中雨转大雨|
		mWeatherIconMap.put("大雨转暴雨", R.drawable.biz_plugin_weather_dayu);//大雨转暴雨|
		mWeatherIconMap.put("暴雨转大暴雨", R.drawable.biz_plugin_weather_baoyu);//暴雨转大暴雨|
		mWeatherIconMap.put("大暴雨转特大暴雨", R.drawable.biz_plugin_weather_dabaoyu);//大暴雨转特大暴雨|
		mWeatherIconMap.put("小雪转中雪", R.drawable.biz_plugin_weather_xiaoyu);//小雪转中雪|
		mWeatherIconMap.put("中雪转大雪", R.drawable.biz_plugin_weather_zhongxue);//中雪转大雪|
		mWeatherIconMap.put("大雪转暴雪", R.drawable.biz_plugin_weather_daxue);//大雪转暴雪|
		mWeatherIconMap.put("浮尘", R.drawable.biz_plugin_weather_shachenbao);//浮尘|
		mWeatherIconMap.put("扬沙", R.drawable.biz_plugin_weather_shachenbao);//扬沙|
		mWeatherIconMap.put("强沙尘暴", R.drawable.biz_plugin_weather_shachenbao);//强沙尘暴|
		mWeatherIconMap.put("霾", R.drawable.biz_plugin_weather_shachenbao);//霾 
	}
	
	/**
	 * 得到主界面图片的键值对
	 * @return
	 */
	public Map<String,Integer> getWeatherIconMap(){
		return mWeatherIconMap;
	}
	
	/**
	 * 取得主界面的天气图片的ImageResouce
	 * @return
	 */
	public int getWeatherIcon(String key){
		int iconRes = R.drawable.biz_plugin_weather_qing;
		if (mWeatherIconMap.containsKey(key)){
			iconRes = mWeatherIconMap.get(key);
		}
		return iconRes; 
	}
	


	
}
