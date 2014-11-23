package com.ryanweather.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ryanweahter.R;
import com.ryanweather.app.WeatherApplication;
import com.ryanweather.entity.CityWeatherResponse;
import com.ryanweather.entity.WeatherData;
import com.ryanweather.utils.L;

public class FourDaysWeatherAdapter extends BaseAdapter {
	private List<WeatherData> mData;
	private CityWeatherResponse mResponse;
	private LayoutInflater mLayoutInflater;
	private WeatherApplication mApp;
	
	public FourDaysWeatherAdapter(Context mContext,CityWeatherResponse mResponse){
		this.mLayoutInflater = LayoutInflater.from(mContext);
		mApp = WeatherApplication.instance;
		this.mResponse = mResponse;
		setData(mResponse.getResults().getWeather_data());
	}
	

	private void setData(List<WeatherData> mData) {
		if (mData!=null){
			this.mData = mData;
		}else{
			mData = new ArrayList<WeatherData>();
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public WeatherData getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder{
		TextView weekday;
		ImageView weatherIcon;
		TextView temperature;
		TextView climate;
		TextView wind;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.biz_plugin_weather_subitem, null);
			holder = new ViewHolder();
			holder.weekday = (TextView) convertView.findViewById(R.id.fourday_weather_weekday);
			holder.weatherIcon = (ImageView) convertView.findViewById(R.id.fourday_weather_weather_img);
			holder.temperature = (TextView) convertView.findViewById(R.id.fourday_weather_temperature);
			holder.climate = (TextView) convertView.findViewById(R.id.fourday_weather_climate);
			holder.wind = (TextView) convertView.findViewById(R.id.fourday_weather_wind);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		WeatherData weatherData = mData.get(position);
		holder.weekday.setText(getFurtherDate(position) + " " + weatherData.getDate());
		holder.weatherIcon.setImageResource(mApp.getWeatherIcon(weatherData.getWeather()));
		holder.temperature.setText(weatherData.getTemperature());
		holder.climate.setText(weatherData.getWeather());
		holder.wind.setText(weatherData.getWind());
		return convertView;
	}


	@SuppressLint("SimpleDateFormat")
	private String getFurtherDate(int position) {
		L.i("info",mResponse.getDate());
		Calendar calendar = Calendar.getInstance(); //new GregorianCalendar();
		int year = Integer.valueOf(mResponse.getDate().split("-")[0]);
		int month = Integer.valueOf(mResponse.getDate().split("-")[1]) - 1;
		int day = Integer.valueOf(mResponse.getDate().split("-")[2]);
		calendar.set(year, month, day);
		calendar.add(Calendar.DAY_OF_MONTH,position);//把日期往后增加一天.整数往后推,负数往前移动
		String returnDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
		return returnDate;
	}
	
}














