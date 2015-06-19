package com.hblg.lookingfellow.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.ImageTool;

public class MsgListViewAdapter extends BaseAdapter {
	
	List<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	
	public MsgListViewAdapter(Context context, List<Map<String, Object>> list, int resourceId) {
		this.list = list;
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
		}
		ImageView headImage = (ImageView)convertView.findViewById(R.id.msglayout_headimage);
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		Bitmap bm = (Bitmap)map.get("headimage");
		//Bitmap output = ImageTool.toRoundCorner(bm, 360.0f);
		headImage.setImageBitmap(bm);
		TextView nameTextView = (TextView)convertView.findViewById(R.id.msglayout_name);
		String name = (String)map.get("name");
		nameTextView.setText(name);
		TextView contentTextView = (TextView)convertView.findViewById(R.id.msglayout_content);
		String content = (String)map.get("content");
		contentTextView.setText(content);
		TextView timeTextView = (TextView)convertView.findViewById(R.id.msglayout_time);
		String time = (String)map.get("time");
		timeTextView.setText(time);
		return convertView;
	}

}
