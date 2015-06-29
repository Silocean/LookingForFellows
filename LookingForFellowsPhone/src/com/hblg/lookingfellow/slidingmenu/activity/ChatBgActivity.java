package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.MySharePreferences;
import com.hblg.lookingfellow.tools.UIMode;

//ÁÄÌì±³¾°
public class ChatBgActivity extends Activity {
	private Button backBtn;
	
	private LinearLayout blackLayout;
	private ImageView blackImg;
	
	private LinearLayout blueLayout;
	private ImageView blueImg;
	
	private LinearLayout redLayout;
	private ImageView redImg;
	
    private MyListener myListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatbg);
		UIMode.changeUIMode(ChatBgActivity.this, UIMode.checkUIMode(ChatBgActivity.this));
		myListener=new MyListener();
		initView();
	}
	private void initView(){
		backBtn=(Button)findViewById(R.id.chatbg_goback_button);
		backBtn.setOnClickListener(myListener);
		
		blackLayout=(LinearLayout)findViewById(R.id.bg_black_layout);
		blackImg=(ImageView)findViewById(R.id.bg_black_img);
		blackLayout.setOnClickListener(myListener);
		
		blueLayout=(LinearLayout)findViewById(R.id.bg_blue_layout);
		blueLayout.setOnClickListener(myListener);
		blueImg=(ImageView)findViewById(R.id.bg_blue_img);
		
		redLayout=(LinearLayout)findViewById(R.id.bg_red_layout);
		redLayout.setOnClickListener(myListener);
		redImg=(ImageView)findViewById(R.id.bg_red_img);
		
		String type=MySharePreferences.getShare(getApplicationContext()).getString(MySharePreferences.BGSKIN, "none");
		if(MySharePreferences.BGSKIN_BLACK.equals(type)||MySharePreferences.BGSKIN_BLACK==type){
			blackImg.setVisibility(View.VISIBLE);
			blueImg.setVisibility(View.GONE);
			redImg.setVisibility(View.GONE);
		}else if(MySharePreferences.BGSKIN_BLUE.equals(type)||MySharePreferences.BGSKIN_BLUE==type){
			blackImg.setVisibility(View.GONE);
			blueImg.setVisibility(View.VISIBLE);
			redImg.setVisibility(View.GONE);
		}else if(MySharePreferences.BGSKIN_RED.equals(type)||MySharePreferences.BGSKIN_RED==type){
			blackImg.setVisibility(View.GONE);
			blueImg.setVisibility(View.GONE);
			redImg.setVisibility(View.VISIBLE);
		}
	}
	private class MyListener implements OnClickListener{
		public void onClick(View v){
			switch(v.getId()){
			case R.id.chatbg_goback_button:
				finish();
				break;
			case R.id.bg_black_layout:
				blackImg.setVisibility(View.VISIBLE);
				blueImg.setVisibility(View.GONE);
				redImg.setVisibility(View.GONE);
				MySharePreferences.getShare(getApplicationContext()).edit()
				    .putString(MySharePreferences.BGSKIN, MySharePreferences.BGSKIN_BLACK).commit();
				break;
			case R.id.bg_blue_layout:
				blackImg.setVisibility(View.GONE);
				blueImg.setVisibility(View.VISIBLE);
				redImg.setVisibility(View.GONE);
				MySharePreferences.getShare(getApplicationContext()).edit()
			    .putString(MySharePreferences.BGSKIN, MySharePreferences.BGSKIN_BLUE).commit();
				break;
			case R.id.bg_red_layout:
				blackImg.setVisibility(View.GONE);
				blueImg.setVisibility(View.GONE);
				redImg.setVisibility(View.VISIBLE);
				MySharePreferences.getShare(getApplicationContext()).edit()
			    .putString(MySharePreferences.BGSKIN, MySharePreferences.BGSKIN_RED).commit();
				break;
			}
		}
	}
	

}
