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

public class PostsListViewAdapter extends BaseAdapter {
	
	List<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	
	public PostsListViewAdapter(Context context, List<Map<String, Object>> list, int resourceId) {
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
		ImageView headImage = (ImageView)convertView.findViewById(R.id.postlayout_headimage);
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		Bitmap bm = (Bitmap)map.get("headimage");
		Bitmap output = ImageTool.toRoundCorner(bm, 360.0f);
		headImage.setImageBitmap(output);
		TextView titleTextView = (TextView)convertView.findViewById(R.id.postlayout_title);
		String title = (String)map.get("title");
		titleTextView.setText(title);
		TextView contentTextView = (TextView)convertView.findViewById(R.id.postlayout_content);
		String content = (String)map.get("content");
		contentTextView.setText(content);
		TextView replycountTextView = (TextView)convertView.findViewById(R.id.postlayout_replay_count);
		String replycount = (String)map.get("replycount");
		replycountTextView.setText(replycount);
		TextView publishnameTextView = (TextView)convertView.findViewById(R.id.postlayout_publish_name);
		String publishname = (String)map.get("publishname");
		publishnameTextView.setText(publishname);
		TextView publishtimeTextView = (TextView)convertView.findViewById(R.id.postlayout_publish_time);
		String publishtime = (String)map.get("publishtime");
		publishtimeTextView.setText(publishtime);
		return convertView;
	}

}
