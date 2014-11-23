package com.ryanweather.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ryanweahter.R;
import com.ryanweather.entity.Index;
import com.ryanweather.utils.L;

public class LiveTipsAdapter extends BaseAdapter{
	private List<Index> mData;
	private LayoutInflater mLayoutInflater;
	
//	public LiveTipsAdapter(Context mContext,CityWeatherResponse mResponse){
//	}
	
	public LiveTipsAdapter(Context mContext,List<Index> mData){
		this.mLayoutInflater = LayoutInflater.from(mContext);
		setData(mData);
	}
	
	
	private void setData(List<Index> data) {
		if (data != null){
			this.mData = data;
		}else{
			this.mData = new ArrayList<Index>();
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Index getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder{
		ImageView tipsIcon;
		TextView tipt;
		TextView zs;
		ImageView arrow;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.biz_plugin_live_tips_subitem, null);
			holder = new ViewHolder();
			holder.tipsIcon = (ImageView) convertView.findViewById(R.id.live_tips_subitem_icon);
			holder.arrow = (ImageView) convertView.findViewById(R.id.live_tips_subitem_arrow);
			holder.tipt = (TextView) convertView.findViewById(R.id.live_tips_subitem_tipt);
			holder.zs = (TextView) convertView.findViewById(R.id.live_tips_subitem_zs);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		L.i("info", mData.size() + "");
		Index index = mData.get(position);
//		holder.tipsIcon.setImageResource(R.drawable.skin_qz_icon_face_nor);
		holder.tipt.setText(index.getTipt());
		holder.zs.setText(index.getZs());
		return convertView;
	}

}
