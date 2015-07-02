package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.MySharePreferences;

public class ChatBgGridViewAdapter extends BaseAdapter{
	Context context;
	List<Integer> list;
	int resourceId;
	LayoutInflater inflater;
	Bundle bundle;
	
	public ChatBgGridViewAdapter(Context context) {
		this.context = context;
		initList();
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	private void initList(){
		list=new ArrayList<Integer>();
		list.add(R.drawable.bg_default_thumb);
		list.add(R.drawable.bg_01_thumb);
		list.add(R.drawable.bg_02_thumb);
		list.add(R.drawable.bg_03_thumb);
		list.add(R.drawable.bg_04_thumb);
		list.add(R.drawable.bg_05_thumb);
		list.add(R.drawable.bg_06_thumb);
		list.add(R.drawable.bg_07_thumb);
		list.add(R.drawable.bg_08_thumb);
		list.add(R.drawable.bg_09_thumb);
		list.add(R.drawable.bg_10_thumb);
		list.add(R.drawable.bg_11_thumb);
		list.add(R.drawable.bg_12_thumb);
		list.add(R.drawable.bg_13_thumb);
		list.add(R.drawable.bg_14_thumb);
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if(null==convertView){
			holder=new Holder();
			convertView=inflater.inflate(R.layout.griditem_bg, null);
			holder.bgImg=(ImageView)convertView.findViewById(R.id.bg_img);
			holder.checkImg=(ImageView)convertView.findViewById(R.id.bg_indicator_img);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final int clickPosition=position;
		if (position == MySharePreferences.getShare(context).getInt(MySharePreferences.BGSKIN, 1)) {  
            holder.checkImg.setVisibility(View.VISIBLE);  
        } else {
            holder.checkImg.setVisibility(View.GONE);
        }
		holder.bgImg.setBackgroundResource(list.get(position));
		holder.bgImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MySharePreferences.getShare(context).edit().putInt(MySharePreferences.BGSKIN, clickPosition).commit();
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
	private class Holder{
		private ImageView bgImg;
		private ImageView checkImg;
	}
}
