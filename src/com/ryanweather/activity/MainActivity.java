package com.ryanweather.activity;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ryanweahter.R;
import com.ryanweather.adapter.FourDaysWeatherAdapter;
import com.ryanweather.adapter.LiveTipsAdapter;
import com.ryanweather.app.WeatherApplication;
import com.ryanweather.entity.CityWeatherResponse;
import com.ryanweather.entity.Index;
import com.ryanweather.entity.WeatherData;
import com.ryanweather.service.WeatherService;
import com.ryanweather.service.WeatherService.XXBinder;
import com.ryanweather.ui.mycustomui.CustomDialog;
import com.ryanweather.ui.pulltorefresh.PullToRefreshBase;
import com.ryanweather.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ryanweather.ui.pulltorefresh.PullToRefreshScrollView;
import com.ryanweather.utils.ToastUtil;

public class MainActivity extends Activity implements IUpdateStatusCallback , OnClickListener , OnItemClickListener{
	private WeatherApplication mApp;
	private WeatherService mWeatherService; //MVC中的Controller
	private TextView mCurrCity; //当前城市
	private TextView mPublishTime; //发布时间(刷新时间)
	private PullToRefreshScrollView mPullToRefreshScrollView; //Pull To Refresh ScrollView自定义控件
	private TextView mPm25Indicate; //PM2.5标题
	private ImageView mPm25Pic; //PM2.5 的图片
	private TextView mPm25Data; //PM2.5当前数据
	private TextView mPm25Level; //PM2.5级数
	private ImageView mCurrWeatherPic; //当前天气图片
	private TextView mCurrWeekToday; //今天星期几
	private TextView mCurrTemperature; //当前温度
	private TextView mCurrClimate; //当前气候
	private TextView mCurrWind; //当前风力
	private ImageView mRefreshBtn; //刷新按钮
	private ProgressBar mProgressBar; //旋转进度条
	private ImageButton mLocationBtn; //定位按钮
	private GridView mFourDaysWeatherGridView; //第二个Weather组件(未来三天的天气预测)
	private FourDaysWeatherAdapter mFurtherFourDaysWeatherAdapter; //用于未来三天天气预测的Adapter
	private ListView mLiveTipsListView; //用于生活指数的ListView
	private LiveTipsAdapter mLiveTipsAdapter; //用于生活指数的ListView的Adapter
	private RelativeLayout mCurrWeatherBlock;
	private LinearLayout mFurtherFourDaysBlock;
	private LinearLayout mLiveTipsBlock;
	private ProgressDialog mWaitingProgressDialog; //等待时用到的ProgressDialog
	private Handler mMainHandler = new Handler();
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			if (mWeatherService!=null){
				mWeatherService.unRegisterUpdateStatutsCallback();
				mWeatherService = null;
			}
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mWeatherService = ((XXBinder)service).getService();
			if (mWeatherService != null){
				mWeatherService.registerUpdateStatusCallback(MainActivity.this);
				mWeatherService.updateWeahter();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData(); //初始化数据
		initViews(); //初始化组件
		initEvent(); //绑定事件
		bindService(new Intent(this,com.ryanweather.service.WeatherService.class), mServiceConnection, Context.BIND_AUTO_CREATE); //绑定WeatherService
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection); //解除绑定WeatherService
	}
	
	/**
	 * 初始化组件
	 */
	private void initViews() {
		mCurrCity = (TextView) findViewById(R.id.city);
		mPublishTime = (TextView) findViewById(R.id.time);
		mPm25Indicate = (TextView) findViewById(R.id.pm2_5);
		mPm25Pic = (ImageView) findViewById(R.id.pm2_5_img); 
		mPm25Data = (TextView) findViewById(R.id.pm_data);
		mPm25Level = (TextView) findViewById(R.id.pm2_5_quality);
		mCurrWeatherPic = (ImageView) findViewById(R.id.weather_img);
		mCurrWeekToday = (TextView) findViewById(R.id.week_today);
		mCurrTemperature = (TextView) findViewById(R.id.temperature);
		mCurrClimate = (TextView) findViewById(R.id.climate);
		mCurrWind = (TextView) findViewById(R.id.wind);
		mRefreshBtn = (ImageView) findViewById(R.id.title_update_btn);
		mProgressBar = (ProgressBar) findViewById(R.id.title_update_progress);
		mLocationBtn = (ImageButton) findViewById(R.id.title_location);
		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.main_activity_pull_to_refresh_scrollview);
		mFourDaysWeatherGridView = (GridView) findViewById(R.id.weather_gridview);
		mLiveTipsListView = (ListView) findViewById(R.id.weather_live_tips_listview);
		mCurrWeatherBlock = (RelativeLayout) findViewById(R.id.block_weather_today);
		mFurtherFourDaysBlock = (LinearLayout) findViewById(R.id.block_weather_further_four_days);
		mLiveTipsBlock = (LinearLayout) findViewById(R.id.block_live_tips);
		mWaitingProgressDialog = new ProgressDialog(this);
	}	
	
	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mRefreshBtn.setOnClickListener(this);
		mLocationBtn.setOnClickListener(this);
		mLiveTipsListView.setOnItemClickListener(this);
		mPullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>(){
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				mWeatherService.updateWeahter();
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mApp = WeatherApplication.instance;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_update_btn:
			mWeatherService.updateWeahter();
			break;
		case R.id.title_location:
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击两次后退出
	 */
	private long firstPresstime;
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		if (System.currentTimeMillis() - firstPresstime < 3000){
			finish();
		}else{
			firstPresstime = System.currentTimeMillis();
			ToastUtil.showShort(this, "按两次后退出程序",R.layout.custom_toast);
		}
	}
	
	/**
	 * 更新界面的天气信息
	 */
	public void updateWeatherInfo(boolean isSuccess){
		CityWeatherResponse entity = mApp.mainCityWeatherResponse;
		if (entity == null){
			return;
		}
		updateMainWeatherInfo(entity,isSuccess); //更新上面的天气部分（包括实时天气，PM2.5)
		updateFurtherFourDaysInfo(entity,isSuccess); //更新下面的四日预测天气部分
		updateLiveTipsInfo(entity,isSuccess); //更新生活指数部分
		mCurrWeatherBlock.setVisibility(View.VISIBLE);
		mFurtherFourDaysBlock.setVisibility(View.VISIBLE);
		mLiveTipsBlock.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 更新生活指数部分
	 * @param entity
	 * @param isSuccess
	 */
	private void updateLiveTipsInfo(CityWeatherResponse entity,boolean isSuccess) {
		mLiveTipsAdapter = new LiveTipsAdapter(this, entity.getResults().getIndex());
		mLiveTipsListView.setAdapter(mLiveTipsAdapter);
		mLiveTipsAdapter.notifyDataSetChanged();
//		ListViewUtil.setListViewHeightBasedOnChildren(mLiveTipsListView);
	}
	
	/**
	 * 更新下面的四日预测天气部分
	 * @param entity
	 * @param isSuccess
	 */
	private void updateFurtherFourDaysInfo(CityWeatherResponse entity , boolean isSuccess){
		mFurtherFourDaysWeatherAdapter = new FourDaysWeatherAdapter(this, entity);
		mFourDaysWeatherGridView.setAdapter(mFurtherFourDaysWeatherAdapter);
		mFurtherFourDaysWeatherAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 更新上面的天气部分（包括实时天气，PM2.5)
	 * @param entity
	 * @param isSuccess
	 */
	@SuppressLint("SimpleDateFormat")
	private void updateMainWeatherInfo(CityWeatherResponse entity , boolean isSuccess){
		WeatherData currWeather = entity.getResults().getWeather_data().get(0); 
		mCurrCity.setText(entity.getResults().getCurrentCity());
		mCurrWeatherPic.setImageResource(mApp.getWeatherIcon(currWeather.getWeather()));
		String pm25 = entity.getResults().getPm25();
		mPm25Indicate.setText("PM2.5");
		mPm25Data.setText(pm25);
		mPm25Level.setText(mWeatherService.getPm25Level(pm25));
		mPm25Pic.setImageResource(mWeatherService.getPm25Image(pm25));
		mCurrTemperature.setText(currWeather.getCurrentTemperature());
		mCurrClimate.setText(currWeather.getWeather());
		mCurrWind.setText(currWeather.getWind());
		//若天气成功刷新，则更新界面的发布时间为当前时间，并保存这个发布时间到偏好设置中
		long currentTime = (isSuccess)?System.currentTimeMillis():mWeatherService.getPublishTimeFromFile(); 
		mCurrWeekToday.setText(new SimpleDateFormat("今天 EEEE").format(currentTime).toString());
		mPublishTime.setText(new SimpleDateFormat("MM月dd日 hh:mm ").format(currentTime) + "发布"); 		
	}
	
	/**
	 * 关于天气更新前中后，界面作出反应
	 */
	@Override
	public void onUpatedStatusChange(int connectedState, final String reason) {
		switch (connectedState) {
		case WeatherService.WEATHER_UPDATE_STATUS_SUCCESS: //当Service更新成功后
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					updateWeatherInfo(true);
					mPullToRefreshScrollView.onRefreshComplete();
					if (mRefreshBtn.getVisibility() == View.GONE){
						mRefreshBtn.setVisibility(View.VISIBLE);
					}
					if (mProgressBar.getVisibility() == View.VISIBLE){
						mProgressBar.setVisibility(View.GONE);
					}
					if (mWeatherService.isFirstRefresh){
						mWeatherService.isFirstRefresh = false;
					}
					if (mWaitingProgressDialog.isShowing() && mWaitingProgressDialog != null){
						mWaitingProgressDialog.dismiss();
					}
					ToastUtil.showShort(MainActivity.this, reason,R.layout.custom_toast);
				}
			});
			break;
		case WeatherService.WEATHER_UPDATE_STATUS_FAILED: //当Servie更新失败时
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					updateWeatherInfo(false);
					mPullToRefreshScrollView.onRefreshComplete();
					if (mRefreshBtn.getVisibility() == View.GONE){
						mRefreshBtn.setVisibility(View.VISIBLE);
					}
					if (mProgressBar.getVisibility() == View.VISIBLE){
						mProgressBar.setVisibility(View.GONE);
					}
					if (mWaitingProgressDialog.isShowing() && mWaitingProgressDialog != null){
						mWaitingProgressDialog.dismiss();
					}
					ToastUtil.showShort(MainActivity.this, reason,R.layout.custom_toast);
				}
			});
			break;
		case WeatherService.WEATHER_UPDATE_STATUS_LOADING: //当Servie正在更新时
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					if (mWeatherService.isFirstRefresh){
						showWaitingDialog();
					}
					if (mRefreshBtn.getVisibility() == View.VISIBLE){
						mRefreshBtn.setVisibility(View.GONE);
					}
					if (mProgressBar.getVisibility() == View.GONE){
						mProgressBar.setVisibility(View.VISIBLE);
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	/**
	 * 第一次进入的时候要Show Waiting Dialog
	 */
	private void showWaitingDialog() {
		mWaitingProgressDialog.setCancelable(false);
		mWaitingProgressDialog.setMessage("天气读取中，请稍等！");
		mWaitingProgressDialog.show();
	}

	/**
	 * 当生活指数Item点击后
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Index index = (Index) parent.getAdapter().getItem(position);
		CustomDialog.Builder dialog = new CustomDialog.Builder(this);
		dialog.setMessage(index.getZs() + "\n\n" + index.getDes())
		.setTitle(index.getTitle())
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
}
