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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.hblg.lookingfellow.tools.StreamTool;

public class FriendsFragment extends Fragment implements OnItemClickListener {
	ListView listView;
	ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private friendsListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	Bitmap bitmap;
	
	String imagePath = "http://192.168.1.152:8080/lookingfellowWeb0.2/head/";
	
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
		try {
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/FriendServlet?tag=myfriends&qq=" + User.qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String str = new String(StreamTool.read(in));
				if(str.equals("error")) {
					Toast.makeText(getActivity(), "你还没有任何好友", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_default);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("headimage", imagePath + "head_" + obj.get("friendQq") + ".jpg");
						map.put("friendQq", obj.getString("friendQq"));
						map.put("friendName", obj.getString("friendName"));
						map.put("friendHometown", obj.getString("friendHometown"));
						map.put("friendSex", obj.getString("friendSex"));
						map.put("friendSigns", obj.getString("friendSigns"));
						map.put("friendPhone", obj.getString("friendPhone"));
						tempList.add(map);
					}
					return tempList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = this.data.get(position).get("friendQq");
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("friendQq", qq);
		startActivity(intent);
	}
	
}
