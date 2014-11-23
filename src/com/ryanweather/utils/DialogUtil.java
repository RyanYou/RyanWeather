package com.ryanweather.utils;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ryanweahter.R;


public class DialogUtil {
	
	/**
	 * 返回一个设置好的Dialog,用于退出当前账号
	 * @param context
	 * @param view
	 * @return
	 */
	public static Dialog getMenuDialog(Activity context, View view) {

		final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = getScreenWidth(context);
		// int screenH = getScreenHeight(context);
		lp.width = screenW;
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
		return dialog;
	}
	
	/**
	 * 得到当前Activity的宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Activity context){
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	/**
	 * 得到当前Activity的高度
	 * @param context
	 * @return
	 */
	public static int getSrceenHeight(Activity context){
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}
	
	
//	/**
//	 * 返回一个设置好的Dialog,用于登录界面
//	 * @param context
//	 * @param message
//	 * @return
//	 */
//	public static Dialog getShowingDialog(Activity context , String message){
//		final Dialog dialog = new Dialog(context, R.style.DialogStyle);
//		dialog.setCancelable(true);
//		dialog.setContentView(R.layout.custom_dialog);
//		Window window = dialog.getWindow();
//		WindowManager.LayoutParams lp = window.getAttributes();
//		int screenWidth = getScreenWidth(context);
//		lp.width = (int) (screenWidth * 0.6) ;
//		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
//		titleTxtv.setText(message);
//		return dialog;
//	}
//
//	public static Dialog getShowingDialog(Activity context , String message , float widthPercent){
//		final Dialog dialog = new Dialog(context, R.style.DialogStyle);
//		dialog.setCancelable(true);
//		dialog.setContentView(R.layout.custom_dialog);
//		Window window = dialog.getWindow();
//		WindowManager.LayoutParams lp = window.getAttributes();
//		int screenWidth = getScreenWidth(context);
//		lp.width = (int) (screenWidth * widthPercent) ;
//		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
//		titleTxtv.setText(message);
//		return dialog;		
//	}

	
}
