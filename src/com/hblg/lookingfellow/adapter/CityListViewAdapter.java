package com.hblg.lookingfellow.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.RegisterActivity;

public class CityListViewAdapter extends BaseAdapter {
	Context context;
	List<String> list;
	int resourceId;
	LayoutInflater inflater;
	Bundle bundle;
	public CityListViewAdapter(Bundle bundle, Context context, List<String> list, int resourceId) {
		this.bundle = bundle;
		this.context = context;
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
		TextView areaNameTextview = (TextView)convertView.findViewById(R.id.areaname);
		final String cityName = list.get(position);
		areaNameTextview.setText(cityName);
		Button button = (Button)convertView.findViewById(R.id.bgbutton);
		final String provinceName = bundle.getString("province");
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(bundle.getString("tag").equals("register")) {
					bundle.putString("city", cityName);
					Intent intent = new Intent(context, RegisterActivity.class);
					intent.putExtras(bundle);
					// 防止 Calling startActivity() from outside of an Activity问题发生
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					context.startActivity(intent);
				}
			}
		});
		return convertView;
	}

}
