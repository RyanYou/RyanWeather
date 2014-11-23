package com.ryanweather.utils;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * HTTP 工具类
 * 
 * @author Ryan You
 */
public class HttpUtil {
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;

	/**
	 * 
	 * 返回Entity,
	 * 
	 * @param uri
	 * @param params
	 * @param method
	 * @return
	 */
	public static HttpEntity getEntity(String uri, List<NameValuePair> params, int method) {
		HttpEntity entity = null;
		try {
			// 1.构建服务器对象
			HttpClient client = new DefaultHttpClient();

			// 2.设置访问超时时长
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);

			// 3.构建请求对象
			HttpUriRequest request = null;
			switch (method) {
				case METHOD_GET:
					StringBuilder sb = new StringBuilder();
					if (params!=null && !params.isEmpty()){
						for (NameValuePair p : params){
							sb.append(p.getName() + "=" + p.getValue() + "&");
							uri = uri + sb.substring(0,sb.length()-1);
						}
					}
					request = new HttpGet(uri);
					break;
					
				case METHOD_POST:
					request = new HttpPost(uri);
					if (params!=null && !params.isEmpty()){
						UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params);
						((HttpPost) request).setEntity(reqEntity);
					}
					break;
			}

			// 4.发送请求并得到响应
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200){
				entity = response.getEntity();
			}
		} catch (Exception e) {
			System.out.println("HttpUtils getEntity出现异常！");
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * 返回connection连接后的Inputream
	 * 
	 * @param entity
	 * @return
	 */
	public static InputStream getStream(HttpEntity entity) {
		InputStream in = null;
		try {
			in = entity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HttpUtil 取得InputStream出现异常！");
		}
		return in;
	}
	
	/**
	 * 获取响应实体的长度
	 * @param entity
	 * @return
	 */
	public static long getLength(HttpEntity entity){
		long len = 0;
		if (entity != null){
			len = entity.getContentLength();
		}
		return len;
	}

	/**
	 * 检查网络是否连接 , 如果连接return true,反之，return false;
	 */
	public static boolean isConnect(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infos = manager.getAllNetworkInfo();
		if (infos != null){
			for (NetworkInfo info : infos){
				State state = info.getState();
				if (state.equals(State.CONNECTED)){
					return true;
				}
			}
		}
		return false;
	}
	
}
