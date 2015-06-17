package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.ChatListViewAdapter;

public class ChatActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button personinfoButton;
	ListView listView;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	ChatListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		gobackButton = (Button)this.findViewById(R.id.chat_goback_button);
		gobackButton.setOnClickListener(this);
		personinfoButton = (Button)this.findViewById(R.id.chat_personinfo_button);
		personinfoButton.setOnClickListener(this);
		initListData();
	}
	
	public void initListData() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		for(int i=0; i<7; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("leftsendtime", "09-12 13:55");
			map.put("leftheadimage", bitmap);
			map.put("leftchatcontent", "¹þà¶°¡£¡£¡£¡");
			list.add(map);
		}
		listView = (ListView)this.findViewById(R.id.chat_listview);
		adapter = new ChatListViewAdapter(getApplicationContext(), list);
		listView.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_goback_button:
			this.finish();
			break;
		case R.id.chat_personinfo_button:
			Intent intent = new Intent(getApplicationContext(), FriendInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
