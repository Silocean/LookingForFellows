package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.CityListViewAdapter;
import com.hblg.lookingfellow.sqlite.SQLiteService;

public class ChooseCityActivity extends Activity {
	Button gobackButton;
	ListView listView;
	List<String> list;
	String tag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosecity);
		gobackButton = (Button)this.findViewById(R.id.choosecity_goback_button);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		Bundle bundle = getIntent().getExtras();
		String proName = bundle.getString("province");
		SQLiteService sqLiteService = new SQLiteService(getApplicationContext());
		list = sqLiteService.getAllCities(proName);
		listView = (ListView)this.findViewById(R.id.cityList);
		CityListViewAdapter adapter = new CityListViewAdapter(bundle, getApplicationContext(), list, R.layout.listitem_choosearea);
		listView.setAdapter(adapter);
	}

}
