package com.ryanweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class NetUtil {
	public static final int NETWORK_NONE = 0;
	public static final int NETWORK_WIFI = 1;
	public static final int NETWORK_MOBILE = 2;
	
	public static final int getNetworkStatus(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//WIFI
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == state || State.CONNECTING == state){
			return NETWORK_WIFI;
		}

		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED == state || State.CONNECTING == state){
			return NETWORK_MOBILE;
		}

		return NETWORK_NONE;
	}
	
}
