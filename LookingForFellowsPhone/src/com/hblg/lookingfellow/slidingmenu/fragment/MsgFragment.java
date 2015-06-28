package com.hblg.lookingfellow.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.MsgListViewAdapter;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.RequestAddFriendMsgActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;

public class MsgFragment extends Fragment  {
	ListView listView;
	ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private MsgListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	Button titlebarRightmenu;
	
	List<String> chatToPersons;
	
	LayoutInflater inflater;
	PopupWindow popupWindow;
	View popupView;
	
	TextView chatToNameTextView;
	TextView deleteFromMsgListtTextView;
	TextView deletetTextView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.main_content_msg, null);
		listView = (ListView)view.findViewById(R.id.msgList);
		//listView.setOnItemClickListener(this);
		//listView.setOnItemLongClickListener(this);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_msg_leftmenu);
		titlebarRightmenu = (Button)view.findViewById(R.id.main_titlebar_msg_rightmenu);
		return view;
	}
	@Override
	public void onResume() {
		ArrayList<Map<String, Object>> data = this.getMessages();
		adapter.setData(data);
		listView.setAdapter(adapter);
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new MsgListViewAdapter(getActivity(), data, R.layout.listitem_msglayout, listView, this);
		ArrayList<Map<String, Object>> data = this.getMessages();
		this.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RequestAddFriendMsgActivity.class);
				startActivity(intent);
			}
		});
		SQLiteService service = new SQLiteService(getActivity());
		chatToPersons = service.getAllChatTo();
		//initPopupWindow();
	}
	
	public ArrayList<Map<String, Object>> getMessages() {
		SQLiteService service = new SQLiteService(getActivity());
		ArrayList<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
		tempList = service.getMessages();
		if(tempList.size() == 0) { //  ���û����Ϣ��¼
			Toast.makeText(getActivity(), "��û����Ϣ��¼", 0).show();
		}
		return tempList;
	}
	public void updataListView() {
		ArrayList<Map<String, Object>> data = getMessages();
		adapter.setData(data);
		listView.setAdapter(adapter);
	}
	/*
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		String friendQq = chatToPersons.get(chatToPersons.size()-1 - position);
		showPopwindow(view, friendQq);
		return false;
	}*/
	
	/*private void showPopwindow(View v, final String friendQq) {
		if(popupWindow.isShowing()) {
			// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			chatToNameTextView = (TextView)popupView.findViewById(R.id.chatToName);
			String name = new SQLiteService(getActivity()).getFriendNameByQq(friendQq);
			if(name != null) {
				chatToNameTextView.setText(name);
			} else {
				chatToNameTextView.setText(friendQq);
			}
			deleteFromMsgListtTextView = (TextView)popupView.findViewById(R.id.deleteFromMsgList);
			deleteFromMsgListtTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// �������¼�Ƴ����ҵ���Ϣ�б�
					new SQLiteService(getActivity()).deleteFromMyMessageList(friendQq);
					popupWindow.dismiss();
					// �����ҵ���Ϣ�б�
					ArrayList<Map<String, Object>> data = getMessages();
					adapter.setData(data);
					listView.setAdapter(adapter);
				}
			});
			deletetTextView = (TextView)popupView.findViewById(R.id.delete);
			deletetTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// �������¼�Ƴ����ҵ���Ϣ�б�
					new SQLiteService(getActivity()).deleteFromMyMessageList(friendQq);
					// �����ݿ���ɾ����ú��ѵ����������¼
					new SQLiteService(getActivity()).deleteMsg(User.qq, friendQq);
					popupWindow.dismiss();
					// �����ҵ���Ϣ�б�
					ArrayList<Map<String, Object>> data = getMessages();
					adapter.setData(data);
					listView.setAdapter(adapter);
				}
			});
		}
	}*/
	
	/*private void initPopupWindow() {
		inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.popupwindow_msg, null);
		popupWindow = new PopupWindow(popupView, 280, 100, false);
		// ���ô˲�����ý��㣬�����޷����
		popupWindow.setFocusable(true);
		// ��Ҫ����һ�´˲����������߿���ʧ 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //���õ��������ߴ�����ʧ  
		popupWindow.setOutsideTouchable(true);
		// ���ô��ڶ���Ч��
		//popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}*/

}
