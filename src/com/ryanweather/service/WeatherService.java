package com.ryanweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ryanweather.activity.IUpdateStatusCallback;
import com.ryanweather.app.WeatherApplication;
import com.ryanweather.dao.WeatherDao;
import com.ryanweather.entity.CityWeatherResponse;
import com.ryanweather.utils.L;
import com.ryanweather.utils.NetUtil;

public class WeatherService extends Service {
	public final static int WEATHER_UPDATE_STATUS_LOADING = 1;
	public final static int WEATHER_UPDATE_STATUS_SUCCESS = 2;
	public final static int WEATHER_UPDATE_STATUS_FAILED = 3;
	public boolean isFirstRefresh = true ;
	private XXBinder mBinder = new XXBinder();
	private WeatherDao mWeatherDao;
	private WeatherApplication mApp;
	private Thread mWeatherUpdateThread;
	private IUpdateStatusCallback mUpdateStatusCallback;
	
	public class XXBinder extends Binder{
		public WeatherService getService(){
			return WeatherService.this;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApp = WeatherApplication.instance;
		mWeatherDao = new WeatherDao(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		L.i("info","WeatherService onBind");
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	/**
	 * 回调IUpdateStatusCallback，用于回调主界面更新
	 * @param cb
	 */
	public void registerUpdateStatusCallback(IUpdateStatusCallback cb){
		mUpdateStatusCallback = cb;
	}
	
	/**
	 * 注销回调
	 */
	public void unRegisterUpdateStatutsCallback(){
		mUpdateStatusCallback = null;
	}
	
	/**
	 * 更新天气信息，当有网络的时候怎么做，当没有网络的时候怎么做
	 */
	public void updateWeahter(){
		
			//1.当正在update的时候终止
			if (mWeatherUpdateThread != null){
				return;
			}

			//2.当没有网络的时候终止
			if (NetUtil.getNetworkStatus(this) == NetUtil.NETWORK_NONE){
				String json = mWeatherDao.getJsonStringFromFile();
				mApp.mainCityWeatherResponse = mWeatherDao.getCityWeatherResponse(json);
				postUpatedStatus(WEATHER_UPDATE_STATUS_SUCCESS,"没有网络，请检查网络连接");
				return;
			}
			
			//3.读取网络数据
			mWeatherUpdateThread = new Thread(){
				@Override
				public void run() {
					try{
						postUpatedStatus(WEATHER_UPDATE_STATUS_LOADING,"正在刷新天气信息，请稍候。。。"); //发送正在更新的回调
						String json = mWeatherDao.getJsonStringFromBaiDu();
						CityWeatherResponse entity = mWeatherDao.getCityWeatherResponse(json);
						if (entity.getError() != 0 || (!("success").equals(entity.getStatus()))){
							//解析出来Json出现错误	
							postUpatedStatus(WEATHER_UPDATE_STATUS_FAILED,"解析Json出错，请检查API KEY或URL");
						}else{
							//解析出来Json出现错误
							mApp.mainCityWeatherResponse = entity; //将解释后的实体类，交个全局变量
							mWeatherDao.saveJsonStringToFile(json); //储存成功的Json到偏好设置中
							postUpatedStatus(WEATHER_UPDATE_STATUS_SUCCESS,"最新天气读取成功"); //发送成功的回调
//							isFirstRefresh = false;
						}
					}catch(Exception e){
						L.i("info","WeatherService updateWeahter 出现异常！");
						postUpatedStatus(WEATHER_UPDATE_STATUS_FAILED,"刷新天气失败: " + e.toString());
						e.printStackTrace();
					}finally{
						//无论成功与否都将线程销毁
						if (mWeatherUpdateThread != null){
							synchronized (mWeatherUpdateThread) {
								mWeatherUpdateThread = null;
							}
						}
					}
				}
			};
			mWeatherUpdateThread.start();
	}
	
	/**
	 * 得到PM2.5的级数
	 * @return
	 */
	public String getPm25Level(String pm25String) {
		return mWeatherDao.getPm25Level(pm25String);
	}
	
	/**
	 * 得到PM2.5的指数对应图片
	 * @param pm25
	 * @return
	 */
	public int getPm25Image(String pm25String) {
		return mWeatherDao.getPm25Image(pm25String);
	}
	
	/**
	 * 保存发布时间（即天气刷新完的那个发布时间）
	 */
	public void savePublishTimeToFile(long currentTime){
		mWeatherDao.savePublishTimeToFile(currentTime);
	}

	/**
	 * 读取上次天气发布时间(若上次天气刷新不成功需要用到)
	 * @return
	 */
	public long getPublishTimeFromFile(){
		return mWeatherDao.getPublishTimeFromFile();
	}
	
	private void postUpatedStatus(int status , String message){
		if (mUpdateStatusCallback != null){
			mUpdateStatusCallback.onUpatedStatusChange(status, message);
		}
	}
}
