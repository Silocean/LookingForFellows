package com.hblg.lookingfellow.slidingmenu.activity;

import com.hblg.lookingfellow.sqlite.DBOpenHelper;
import com.hblg.lookingfellow.sqlite.SQLiteService;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class StartActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//判断是否是在退出程序时调用ciactivity
		if(getIntent() != null) {
			if(getIntent().getStringExtra("tag") != null) {
				if(getIntent().getStringExtra("tag").equals("close")) {
					finish();
					return;
				}
			}
		}
		Intent intent = new Intent(getApplicationContext(), RegisterAndLoginActivity.class);
		startActivity(intent);
		SQLiteService service = new SQLiteService(getApplicationContext());
	}
	
}
