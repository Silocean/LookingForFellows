package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.ImageUtils;
import com.hblg.lookingfellow.tools.ImageUtils.ImageCallBack;

public class MsgListViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	ListView listView;
	
	Bitmap bm;
	
	ViewHolder holder;
	
	public MsgListViewAdapter(Context context, ArrayList<Map<String, Object>> list, int resourceId, ListView listView) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		this.listView = listView;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(ArrayList<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
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
		holder = new ViewHolder();
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			holder.headImage = (ImageView)convertView.findViewById(R.id.msglayout_headimage);
			holder.nameTextView = (TextView)convertView.findViewById(R.id.msglayout_name);
			holder.contentTextView = (TextView)convertView.findViewById(R.id.msglayout_content);
			holder.timeTextView = (TextView)convertView.findViewById(R.id.msglayout_time);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		
		String name = (String)map.get("msgReceiverName");
		holder.nameTextView.setText(name);
		
		String content = (String)map.get("msgDetails");
		holder.contentTextView.setText(content);
		
		String time = (String)map.get("msgTime");
		holder.timeTextView.setText(time);
		//Í·Ïñ
		ImageUtils.ImageCallBack callBack = new ImageCallBack() {
			public void loadImage(Bitmap bitMap, String imageTag) {
				ImageView imageView = (ImageView)listView.findViewWithTag(imageTag);
				if(null==bitMap){
					imageView.setBackgroundResource(R.drawable.head_default);
				}else if(null==imageView){
					holder.headImage.setBackgroundResource(R.drawable.head_default);
					return;
				}
				imageView.setImageBitmap(bitMap);
			}
		};
		String headUrl = (String)map.get("headimage");
		ImageUtils.setImageView(holder.headImage, headUrl, context, callBack);
		
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView headImage;
		private TextView nameTextView;
		private TextView contentTextView;
		private TextView timeTextView;
	}

}
