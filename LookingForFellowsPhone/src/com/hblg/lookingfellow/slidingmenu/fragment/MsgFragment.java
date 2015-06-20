/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hblg.lookingfellow.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.MsgListViewAdapter;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;

public class MsgFragment extends Fragment implements OnItemClickListener{
	ListView listView;
	ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private MsgListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	Button titlebarRightmenu;
	
	List<String> chatToPersons;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_msg, null);
		listView = (ListView)view.findViewById(R.id.msgList);
		listView.setOnItemClickListener(this);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_msg_leftmenu);
		titlebarRightmenu = (Button)view.findViewById(R.id.main_titlebar_msg_rightmenu);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new MsgListViewAdapter(getActivity(), data, R.layout.listitem_msglayout, listView);
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
				Toast.makeText(getActivity(), "复选", 0).show();
			}
		});
		SQLiteService service = new SQLiteService(getActivity());
		chatToPersons = service.getAllChatTo();
	}
	
	public ArrayList<Map<String, Object>> getMessages() {
		SQLiteService service = new SQLiteService(getActivity());
		ArrayList<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
		tempList = service.getMessages();
		if(tempList.size() == 0) { //  如果没有消息记录
			Toast.makeText(getActivity(), "暂没有消息记录", 0).show();
		}
		return tempList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = chatToPersons.get(chatToPersons.size()-1 - position);
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("friendQq", qq);
		startActivity(intent);
	}

}
