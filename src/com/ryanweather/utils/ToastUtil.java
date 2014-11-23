package com.ryanweather.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryanweahter.R;

public class ToastUtil {
	private static Toast toast;
	
	public static void showShort(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showShort(Context context, String message, int resId) {
		View view = LayoutInflater.from(context).inflate(resId, null);
		TextView tv = (TextView) view.findViewById(R.id.custom_toast);
		tv.setText(message);
		toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showShort(Context context, String message, int resId,int position) {
		View view = LayoutInflater.from(context).inflate(resId, null);
		TextView tv = (TextView) view.findViewById(R.id.custom_toast);
		tv.setText(message);
		toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(position, 0, 0);
		toast.show();
	}

	public static void showLong(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showLong(Context context, String message, int resId) {
		View view = LayoutInflater.from(context).inflate(resId, null);
		TextView tv = (TextView) view.findViewById(R.id.custom_toast);
		tv.setText(message);
		toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	
}
