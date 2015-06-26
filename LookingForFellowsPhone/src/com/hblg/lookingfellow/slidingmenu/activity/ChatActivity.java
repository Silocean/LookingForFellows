package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.ChatListViewAdapter;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.model.ManageClientConnServer;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class ChatActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button personinfoButton;
	
	TextView titleTextView;
	Button sendButton;
	EditText contentEditText;
	
	static ListView listView;
	static ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	static ChatListViewAdapter adapter;
	
	public static String friendQq; // 聊天对象qq号码
	
	ObjectOutputStream oos = null;
	
	public static boolean active = false; // 该activity是不是处于最顶端
	
	@Override
	protected void onResume() {
		active = true;
		super.onResume();
	}
	@Override
	protected void onStop() {
		active = false;
		super.onStop();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friendQq = getIntent().getStringExtra("friendQq");
		ManageActivity.addActiviy("ChatActivity", this);
		setContentView(R.layout.activity_chat);
		gobackButton = (Button)this.findViewById(R.id.chat_goback_button);
		gobackButton.setOnClickListener(this);
		personinfoButton = (Button)this.findViewById(R.id.chat_personinfo_button);
		personinfoButton.setOnClickListener(this);
		titleTextView = (TextView)this.findViewById(R.id.titlebar_title);
		//临时测试2013.10.19
		String friName = new SQLiteService(getApplicationContext()).getFriendNameByQq(friendQq);
		if(friName != null) {
			titleTextView.setText(friName);
		} else {
			titleTextView.setText(friendQq);
		}
		/*SQLiteService service = new SQLiteService(getApplicationContext());
		Student student = service.getStuInfo(friendQq);
		titleTextView.setText(student.getName());*/
		sendButton = (Button)this.findViewById(R.id.chat_bottombar_sendbutton);
		sendButton.setOnClickListener(this);
		contentEditText = (EditText)this.findViewById(R.id.chat_bottombar_edittext);
		initListData();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		friendQq = intent.getStringExtra("friendQq");
		String friName = new SQLiteService(getApplicationContext()).getFriendNameByQq(friendQq);
		if(friName != null) {
			titleTextView.setText(friName);
		} else {
			titleTextView.setText(friendQq);
		}
		initListData();
		super.onNewIntent(intent);
	}
	/**
	 * 获取本地聊天记录
	 * @param receiver
	 * @return
	 */
	public ArrayList<Map<String, Object>> getChatMessages(String receiver) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		ArrayList<Map<String, Object>> tempList = service.getChatMessages(User.qq, receiver);
		if(tempList.size() == 0) { //  如果没有消息记录
			Toast.makeText(getApplicationContext(), "暂没有消息记录", 0).show();
		}
		return tempList;
	}
	
	public void initListData() {
		ArrayList<Map<String, Object>> data = this.getChatMessages(friendQq);
		adapter = new ChatListViewAdapter(getApplicationContext(), data, listView, this);
		this.data = data;
		adapter.setData(data);
		listView = (ListView)this.findViewById(R.id.chat_listview);
		listView.setAdapter(adapter);
		listView.setSelection(listView.getCount() - 1); // listView显示最后一项
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_goback_button:
			this.finish();
			break;
		case R.id.chat_personinfo_button:
			Intent intent = new Intent(getApplicationContext(), FriendInfoActivity.class);
			intent.putExtra("qq", friendQq);
			intent.putExtra("tag", "unfriendRequest");
			startActivity(intent);
			break;
		case R.id.chat_bottombar_sendbutton: 
			try {
				// 发送消息到服务器
				oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
				Message msg = new Message();
				msg.setType(MessageType.MSG_CHAT);
				msg.setSender(User.qq);
				msg.setReceiver(friendQq);
				msg.setDetails(contentEditText.getText().toString());
				String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
				msg.setTime(time);
				oos.writeObject(msg);
				
				contentEditText.setText(""); // 清空输入框
				
				// 保存聊天记录到本地
				SQLiteService service = new SQLiteService(getApplicationContext());
				service.saveMessage(msg);
				
				// 更新我的消息列表
				SQLiteService service2 = new SQLiteService(getApplicationContext());
				service2.updateMyMessageList(friendQq);
				
				// 更新聊天界面
				updateChatView(getApplicationContext(),msg);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 插入信息时更新聊天界面
	 */
	public static void updateChatView(Context context, Message msg) {
		SQLiteService service = new SQLiteService(context);
		Map<String, Object> map = service.getLastMessage(msg);
		System.out.println("最后一条消息："+map);
		data.add(map);
		adapter.setData(data);
		listView.setSelection(listView.getCount() - 1); // listView显示最后一项
	}
	/**
	 * 删除信息时更新聊天界面
	 */
	public void updateChatView() {
		ArrayList<Map<String, Object>> data = this.getChatMessages(friendQq);
		adapter.setData(data);
		listView.setSelection(listView.getCount() - 1); // listView显示最后一项
	}
}
