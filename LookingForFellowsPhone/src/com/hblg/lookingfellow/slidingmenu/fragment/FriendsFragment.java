package com.hblg.lookingfellow.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.friendsListViewAdapter;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.AddFriendsActivity;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;

public class FriendsFragment extends Fragment implements OnItemClickListener {
	ListView listView;
	ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private friendsListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	Bitmap bitmap;
	
	String qq = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_friends, null);
		listView = (ListView)view.findViewById(R.id.friendList);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_friend_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_friend_rightmenu);
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		ArrayList<Map<String, String>> data = this.getFriends();
		this.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new friendsListViewAdapter(getActivity(), data, R.layout.listitem_friendslayout, listView);
		ArrayList<Map<String, String>> data = this.getFriends();
		this.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private ArrayList<Map<String, String>> getFriends() {
		ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		SQLiteService service = new SQLiteService(getActivity());
		tempList = service.getAllFriendsInfo(User.qq);
		if(tempList.size() == 0) {
			Toast.makeText(getActivity(), "你还没有好友啊", 0).show();
		}
		return tempList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = this.data.get(position).get("friQQ");
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("friendQq", qq);
		startActivity(intent);
	}
	
}
