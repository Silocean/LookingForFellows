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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;

public class MainFragment extends Fragment {
	ListView listView;
	private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3",
			"Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",
			"Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
			"Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",
			"Content Item 16" };
	private ArrayAdapter<String> contentListAdapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content, null);
		listView = (ListView)view.findViewById(R.id.contentList);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_rightmenu);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		contentListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
				contentItems);
		listView.setAdapter(contentListAdapter);
		
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showRight();
			}
		});
	}

}
