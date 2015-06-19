package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.EditText;
import android.widget.ListView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.ChatListViewAdapter;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageClientConnServer;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class ChatActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button personinfoButton;
	
	Button sendButton;
	EditText contentEditText;
	
	ListView listView;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	ChatListViewAdapter adapter;
	
	String friendQq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		friendQq = getIntent().getStringExtra("friendQq");
		setContentView(R.layout.activity_chat);
		gobackButton = (Button)this.findViewById(R.id.chat_goback_button);
		gobackButton.setOnClickListener(this);
		personinfoButton = (Button)this.findViewById(R.id.chat_personinfo_button);
		personinfoButton.setOnClickListener(this);
		sendButton = (Button)this.findViewById(R.id.chat_bottombar_sendbutton);
		sendButton.setOnClickListener(this);
		contentEditText = (EditText)this.findViewById(R.id.chat_bottombar_edittext);
		initListData();
	}
	
	public void initListData() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_default);
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
		case R.id.chat_bottombar_sendbutton:
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
				Message msg = new Message();
				msg.setType(MessageType.MSG_CHAT);
				msg.setSender(User.qq);
				msg.setReveiver(friendQq);
				msg.setDetails(contentEditText.getText().toString());
				String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
				msg.setTime(time);
				oos.writeObject(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
}
