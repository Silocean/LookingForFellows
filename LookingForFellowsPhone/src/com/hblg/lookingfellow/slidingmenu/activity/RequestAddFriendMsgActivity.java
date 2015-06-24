package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.MsgListViewAdapter;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.sqlite.SQLiteService;

public class RequestAddFriendMsgActivity extends Activity implements OnItemLongClickListener, OnItemClickListener {
	ListView listView;
	ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private MsgListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	
	LayoutInflater inflater;
	PopupWindow popupWindow;
	View popupView;
	
	TextView chatToNameTextView;
	TextView deletetTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msgrequestaddfriend);
		titlebarLeftmenu = (ImageView)this.findViewById(R.id.main_titlebar_msg_leftmenu);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		listView = (ListView)this.findViewById(R.id.requestAddFriendList);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		adapter = new MsgListViewAdapter(getApplicationContext(), data, R.layout.listitem_msglayout, listView);
		ArrayList<Map<String, Object>> data = this.getMessages();
		this.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
		initPopupWindow();
	}
	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<Map<String, Object>> data = this.getMessages();
		this.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
	}
	private ArrayList<Map<String, Object>> getMessages() {
		SQLiteService service = new SQLiteService(getApplicationContext());
		ArrayList<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
		tempList = service.getRequestAddFriendMessages();
		if(tempList.size() == 0) { //  ���û����Ϣ��¼
			Toast.makeText(getApplicationContext(), "��û��������Ӻ�����Ϣ��¼", 0).show();
		}
		return tempList;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		String friendQq = (String)data.get(position).get("msgSender");
		showPopwindow(view, friendQq);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = (String) data.get(position).get("msgSender");
		Intent intent = new Intent(getApplicationContext(), FriendInfoActivity.class);
		intent.putExtra("qq", qq);
		intent.putExtra("tag", "agreeRequest");
		startActivity(intent);
	}
	private void showPopwindow(View v, final String friendQq) {
		if(popupWindow.isShowing()) {
			// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			chatToNameTextView = (TextView)popupView.findViewById(R.id.chatToName);
			String name = new SQLiteService(getApplicationContext()).getFriendNameByQq(friendQq);
			if(name != null) {
				chatToNameTextView.setText(name);
			} else {
				chatToNameTextView.setText(friendQq);
			}
			deletetTextView = (TextView)popupView.findViewById(R.id.delete);
			deletetTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// �����ݿ���ɾ����ú��ѵ����������¼
					new SQLiteService(getApplicationContext()).deleteRequestAddFriendMsg(User.qq, friendQq);
					popupWindow.dismiss();
					// �����б�
					ArrayList<Map<String, Object>> data = getMessages();
					adapter.setData(data);
					listView.setAdapter(adapter);
				}
			});
		}
	}
	
	private void initPopupWindow() {
		inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.popupwindow_requestaddfriendmsg, null);
		popupWindow = new PopupWindow(popupView, 280, 100, false);
		// ���ô˲�����ý��㣬�����޷����
		popupWindow.setFocusable(true);
		// ��Ҫ����һ�´˲����������߿���ʧ 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //���õ��������ߴ�����ʧ  
		popupWindow.setOutsideTouchable(true);
		// ���ô��ڶ���Ч��
		//popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}
}
