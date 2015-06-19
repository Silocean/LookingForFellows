package com.hblg.lookingfellow.adapter;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.ChooseCityActivity;
import com.hblg.lookingfellow.slidingmenu.activity.PersonInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.RegisterActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;

public class CityListViewAdapter extends BaseAdapter {
	ChooseCityActivity chooseCityActivity;
	Context context;
	List<String> list;
	int resourceId;
	LayoutInflater inflater;
	Bundle bundle;
	
	public CityListViewAdapter(ChooseCityActivity chooseCityActivity, Bundle bundle, Context context, List<String> list, int resourceId) {
		this.chooseCityActivity = chooseCityActivity;
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
					Intent intent = new Intent(context, RegisterActivity.class);
					Bundle myBundle = new Bundle();
					myBundle.putString("city", cityName);
					myBundle.putString("province", provinceName);
					intent.putExtras(myBundle);
					// 防止 Calling startActivity() from outside of an Activity问题发生
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					context.startActivity(intent);
				} else if(bundle.getString("tag").equals("modify_fragment")) {
					//chooseCityActivity.changeHometown(provinceName, cityName, 1);
				} else if(bundle.getString("tag").equals("modify_activity")) {
					chooseCityActivity.changeHometown(provinceName, cityName, 2);
				}
			}
		});
		return convertView;
	}


}
