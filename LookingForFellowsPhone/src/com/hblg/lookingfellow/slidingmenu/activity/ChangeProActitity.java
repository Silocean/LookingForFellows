package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;

public class ChangeProActitity extends Activity {
	Button gobackButton;
	ListView listView;
	LinkedList<String> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeprovince);
		ManageActivity.addActiviy("ChangeProActitity", this);
		gobackButton = (Button)this.findViewById(R.id.chooseprovince_goback_button);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		SQLiteService sqLiteService = new SQLiteService(getApplicationContext());
		list = sqLiteService.getAllProvinces();
		list.addFirst("¹«¸æ°å");
		ChangeProListAdapter adapter = new ChangeProListAdapter();
		listView = (ListView)this.findViewById(R.id.provinceList);
		listView.setAdapter(adapter);
	}
	
	private class ChangeProListAdapter extends BaseAdapter{
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				convertView = inflater.inflate(R.layout.listitem_choosearea, null);
			}
			TextView areaNameTextview = (TextView)convertView.findViewById(R.id.areaname);
			final String provinceName = list.get(position);
			areaNameTextview.setText(provinceName);
			Button button = (Button)convertView.findViewById(R.id.bgbutton);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("pro", provinceName);
					setResult(5, intent);
					finish();
				}
			});
			return convertView;
		}
		
	}
	
}
