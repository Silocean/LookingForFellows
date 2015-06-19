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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.MsgListViewAdapter;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.tools.ImageTool;

public class MsgFragment extends Fragment {
	ListView listView;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private MsgListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	Button titlebarRightmenu;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_msg, null);
		listView = (ListView)view.findViewById(R.id.msgList);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_msg_leftmenu);
		titlebarRightmenu = (Button)view.findViewById(R.id.main_titlebar_msg_rightmenu);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_default);
		for(int i=0; i<7; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("headimage", bitmap);
			map.put("name", "linxiaonan");
			map.put("content", "明天中午聚会，请务必准时集合！啦啦啦啦啦啦啦啦");
			map.put("time", "15:21");
			list.add(map);
		}
		adapter = new MsgListViewAdapter(getActivity(), list, R.layout.listitem_msglayout);
		
		listView.setAdapter(adapter);
		
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//TODO
			}
		});
	}

}
