package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//�ж��Ƿ������˳�����ʱ����ciactivity
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
	}
	
}
