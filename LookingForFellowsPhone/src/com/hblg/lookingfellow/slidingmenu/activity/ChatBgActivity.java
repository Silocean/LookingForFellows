package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.R.id;
import com.hblg.lookingfellow.adapter.ChatBgGridViewAdapter;
import com.hblg.lookingfellow.customwidget.MyGridView;
import com.hblg.lookingfellow.model.ManageActivity;

public class ChatBgActivity extends Activity{
	private MyGridView myGridView;
	ChatBgGridViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("ChatBgActivity", this);
		setContentView(R.layout.activity_chatbg);
		myGridView=(MyGridView)findViewById(id.chat_bg_gridview);
		adapter=new ChatBgGridViewAdapter(getApplicationContext());
		myGridView.setAdapter(adapter);
		findViewById(R.id.chatbg_goback_button).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	

}
