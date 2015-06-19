package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.ProvinceListViewAdapter;
import com.hblg.lookingfellow.sqlite.SQLiteService;

public class ChooseProvinceActivity extends Activity {
	Button gobackButton;
	ListView listView;
	List<String> list;
	String tag;
	Bundle bundle = new Bundle();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseprovince);
		gobackButton = (Button)this.findViewById(R.id.chooseprovince_goback_button);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		tag = getIntent().getStringExtra("tag");
		bundle.putString("tag", tag);
		SQLiteService sqLiteService = new SQLiteService(getApplicationContext());
		list = sqLiteService.getAllProvinces();
		listView = (ListView)this.findViewById(R.id.provinceList);
		ProvinceListViewAdapter adapter = new ProvinceListViewAdapter(bundle, getApplicationContext(), list, R.layout.listitem_choosearea);
		listView.setAdapter(adapter);
	}

}
