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
	
	public static String friendQq; // �������qq����
	
	ObjectOutputStream oos = null;
	
	public static boolean active = false; // ��activity�ǲ��Ǵ������
	
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
		//��ʱ����2013.10.19
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
	 * ��ȡ���������¼
	 * @param receiver
	 * @return
	 */
	public ArrayList<Map<String, Object>> getChatMessages(String receiver) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		ArrayList<Map<String, Object>> tempList = service.getChatMessages(User.qq, receiver);
		if(tempList.size() == 0) { //  ���û����Ϣ��¼
			Toast.makeText(getApplicationContext(), "��û����Ϣ��¼", 0).show();
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
		listView.setSelection(listView.getCount() - 1); // listView��ʾ���һ��
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
				// ������Ϣ��������
				oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
				Message msg = new Message();
				msg.setType(MessageType.MSG_CHAT);
				msg.setSender(User.qq);
				msg.setReceiver(friendQq);
				msg.setDetails(contentEditText.getText().toString());
				String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
				msg.setTime(time);
				oos.writeObject(msg);
				
				contentEditText.setText(""); // ��������
				
				// ���������¼������
				SQLiteService service = new SQLiteService(getApplicationContext());
				service.saveMessage(msg);
				
				// �����ҵ���Ϣ�б�
				SQLiteService service2 = new SQLiteService(getApplicationContext());
				service2.updateMyMessageList(friendQq);
				
				// �����������
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
	 * ������Ϣʱ�����������
	 */
	public static void updateChatView(Context context, Message msg) {
		SQLiteService service = new SQLiteService(context);
		Map<String, Object> map = service.getLastMessage(msg);
		System.out.println("���һ����Ϣ��"+map);
		data.add(map);
		adapter.setData(data);
		listView.setSelection(listView.getCount() - 1); // listView��ʾ���һ��
	}
	/**
	 * ɾ����Ϣʱ�����������
	 */
	public void updateChatView() {
		ArrayList<Map<String, Object>> data = this.getChatMessages(friendQq);
		adapter.setData(data);
		listView.setSelection(listView.getCount() - 1); // listView��ʾ���һ��
	}
}
