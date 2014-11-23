package com.ryanweather.activity;

/**
 * 用于天气update状态回调
 * @author Ryan You
 *
 */
public interface IUpdateStatusCallback {
	
	/**
	 * 更新天气情况的状态
	 * @param connectedState
	 * @param reason
	 */
	public void onUpatedStatusChange(int connectedState , String reason);
	
}
