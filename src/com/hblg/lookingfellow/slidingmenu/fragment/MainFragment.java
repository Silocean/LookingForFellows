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
import android.widget.ImageView;
import android.widget.ListView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.PostsListViewAdapter;
import com.hblg.lookingfellow.slidingmenu.activity.SendPostActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.DBOpenHelper;

public class MainFragment extends Fragment {
	ListView listView;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private PostsListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_posts, null);
		listView = (ListView)view.findViewById(R.id.contentList);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_rightmenu);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		for(int i=0; i<10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("headimage", bitmap);
			map.put("title", "��ӭ�����������Ⱥ");
			map.put("content", "������������ҵ������ڸ�У�е����磬����������﷢���ټ�ʧɢ�Ѿõ����磬�������ڲ������������ĺ�����!");
			map.put("replaycount", 35+"");
			map.put("publishname", "linxiaonan");
			map.put("publishtime", "10:17");
			list.add(map);
		}
		adapter = new PostsListViewAdapter(getActivity(), list, R.layout.listitem_postlayout);
		
		listView.setAdapter(adapter);
		
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SendPostActivity.class);
				startActivity(intent);
				DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity());
				dbOpenHelper.getWritableDatabase();
			}
		});
	}

}
