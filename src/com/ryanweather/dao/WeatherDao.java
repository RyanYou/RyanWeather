package com.ryanweather.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.ryanweahter.R;
import com.ryanweather.entity.CityWeatherResponse;
import com.ryanweather.entity.Index;
import com.ryanweather.entity.Results;
import com.ryanweather.entity.WeatherData;
import com.ryanweather.utils.GlobalConstants;
import com.ryanweather.utils.HttpUtil;
import com.ryanweather.utils.PreferenceUtils;
import com.ryanweather.utils.SteamUtil;


public class WeatherDao {
	private Context mContext;
	
	public WeatherDao(Context mContext){
		this.mContext = mContext;
	}
	
	/**
	 * 从服务器中得到Json字符串
	 * @return
	 */
	public String getJsonStringFromBaiDu(){
		String str = null;
		try {
			HttpEntity entity = HttpUtil.getEntity(GlobalConstants.WEATHER_URL, null, HttpUtil.METHOD_GET);
			str = SteamUtil.inputStream2String(HttpUtil.getStream(entity));
		} catch (Exception e) {
			Log.i("info","WeatherDao getJsonStringFromBaiDu() 出现异常");
			e.printStackTrace();
		}		
		return str;
	}
	
	/**
	 * 如果没有连接上网，则从偏好设置上读取数据
	 * @return
	 */
	public String getJsonStringFromFile(){
		String str = null;
		try{
			str = PreferenceUtils.getPrefString(mContext, GlobalConstants.LOCAL_JSON, "");
		}catch (Exception e){
			Log.i("info","WeatherDao getJsonStringFromFile() 出现异常");
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 *  储存Json到偏好设置中
	 */
	public void saveJsonStringToFile(String content){
		PreferenceUtils.setPrefString(mContext, GlobalConstants.LOCAL_JSON, content);
	}
	
	
	/**
	 * 取得实体类CityWeatherResponse
	 * @return
	 */
	public CityWeatherResponse getCityWeatherResponse(String json){
		CityWeatherResponse cwr = new CityWeatherResponse();
		try {
			JSONObject jObj = new JSONObject(json);
			cwr.setDate(jObj.getString("date"));
			cwr.setError(jObj.getInt("error"));
			cwr.setStatus(jObj.getString("status"));
			cwr.setResults(getResults(jObj.getString("results")));
		} catch (JSONException e) {
			Log.i("info","WeatherDao CityWeatherResponse() 出现异常");
			e.printStackTrace();
		}
		return cwr;
	}
	
	/**
	 * 取得实体类CityWeatherResponse里的Results
	 * @param jsonString
	 * @return
	 */
	private Results getResults(String jsonString){
		Results results = new Results();
		try {
			JSONArray array = new JSONArray(jsonString);
			JSONObject jObj = array.getJSONObject(0);
			results.setCurrentCity(jObj.getString("currentCity"));
			results.setPm25(jObj.getString("pm25"));
			results.setWeather_data(getWeatherData(jObj.getString("weather_data")));
			results.setIndex(getIndex(jObj.getString("index")));
		} catch (JSONException e) {
			Log.i("info","WeatherDao getResults() 出现异常");
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * 取得实体类WeahterData
	 * @param jsonString
	 * @return
	 */
	private List<WeatherData> getWeatherData(String jsonString){
		List<WeatherData> list = new ArrayList<WeatherData>();
		try {
			JSONArray array = new JSONArray(jsonString);
			for (int i = 0;i < array.length();i++){
				JSONObject jObj = array.getJSONObject(i);
				WeatherData weatherData = new WeatherData();
				weatherData.setDate(jObj.getString("date"));
				weatherData.setTemperature(jObj.getString("temperature"));
				weatherData.setDayPictureUrl(jObj.getString("dayPictureUrl"));
				weatherData.setNightPictureUrl(jObj.getString("nightPictureUrl"));
				weatherData.setWeather(jObj.getString("weather"));
				weatherData.setWind(jObj.getString("wind"));
				if (i == 0){
					//如果是第一个date则需要修改date的字符串。 如 <date>周五 08月01日 (实时：36℃)</date> ，date:应该是周五，Current Temperature应该是是36℃
					revisedFirstWeatherData(weatherData); 
				}
				list.add(weatherData);
			}
		} catch (JSONException e) {
			Log.i("info","WeatherDao getWeatherData() 出现异常");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 如果是第一个date则需要修改date的字符串
	 * @param weather
	 * @return
	 */
	private void revisedFirstWeatherData(WeatherData weather){
		String info = weather.getDate().trim();
		if (info!=null){
			String correctDate = info.substring(0,3);
			String currentTemperature = info.split("实时：")[1].replace(")", "");
			weather.setDate(correctDate);
			weather.setCurrentTemperature(currentTemperature);
		}
	}
	
	
	/**
	 * 取得实体类Index
	 * @param jsonString
	 * @return
	 */
	private List<Index> getIndex(String jsonString){
		List<Index> list = new ArrayList<Index>();
		try {
			JSONArray array = new JSONArray(jsonString);
			for (int i = 0 ; i < array.length() ; i++){
				JSONObject jObj = array.getJSONObject(i);
				Index index = new Index();
				index.setTitle(jObj.getString("title"));
				index.setZs(jObj.getString("zs"));
				index.setTipt(jObj.getString("tipt"));
				index.setDes(jObj.getString("des"));
				list.add(index);
			}
		} catch (JSONException e) {
			Log.i("info","WeatherDao getIndex() 出现异常");
			e.printStackTrace();
		}
		
		return list;
	}

	/**
	 * 得到PM2.5的级数
	 * @return
	 */
	public String getPm25Level(String pm25String) {
		String result = null;
		if (pm25String == null){
			result = "N/A";
			return result;
		}
		
		int pm25 = Integer.valueOf(pm25String);
		if (pm25 <= 50){
			result = "优";
		}else if(pm25 > 50 && pm25 <= 100){
			result = "良";
		}else if(pm25 > 100 && pm25 <= 150){
			result = "轻度污染";
		}else if(pm25 > 150 && pm25 <= 200){
			result = "中度污染";
		}else if(pm25 > 200 && pm25 <= 300){
			result = "重度污染";
		}else if(pm25 > 300){
			result = "严重污染";
		}
		return result;
	}
	
	/**
	 * 得到PM2.5的指数对应图片
	 * @param pm25
	 * @return
	 */
	public int getPm25Image(String pm25String) {
		int resource = 0;
		if (pm25String == null){
			resource = R.drawable.biz_plugin_weather_0_50; 
			return resource;
		}
		
		int pm25 = Integer.valueOf(pm25String);
		if (pm25 <= 50){
			resource = R.drawable.biz_plugin_weather_0_50;
		}else if(pm25 > 50 && pm25 <= 100){
			resource = R.drawable.biz_plugin_weather_51_100;
		}else if(pm25 > 100 && pm25 <= 150){
			resource = R.drawable.biz_plugin_weather_101_150;
		}else if(pm25 > 150 && pm25 <= 200){
			resource = R.drawable.biz_plugin_weather_151_200;
		}else if(pm25 > 200 && pm25 <= 300){
			resource = R.drawable.biz_plugin_weather_201_300;
		}else if(pm25 > 300){
			resource = R.drawable.biz_plugin_weather_201_300;
		}
		return resource;
	}
	
	/**
	 * 保存发布时间（即天气刷新完的那个发布时间）
	 */
	public void savePublishTimeToFile(long currentTime){
		PreferenceUtils.setSettingLong(mContext, GlobalConstants.LASTEST_PUBLISH_TIME, currentTime);
	}
	
	/**
	 * 读取上次天气发布时间(若上次天气刷新不成功需要用到)
	 * @return
	 */
	public long getPublishTimeFromFile(){
		return PreferenceUtils.getPrefLong(mContext, GlobalConstants.LASTEST_PUBLISH_TIME,System.currentTimeMillis());
	}
}
