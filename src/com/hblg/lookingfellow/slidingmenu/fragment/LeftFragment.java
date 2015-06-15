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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;

public class LeftFragment extends Fragment {
	
	MainFragment mainFragment;
	
	RelativeLayout leftmenuNews;
	RelativeLayout leftmenuFriends;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		leftmenuNews = (RelativeLayout)view.findViewById(R.id.leftmenu_news);
		leftmenuFriends = (RelativeLayout)view.findViewById(R.id.leftmenu_friends);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		leftmenuNews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((SlidingActivity) getActivity()).showLeft();
				Toast.makeText(getActivity(), "news", Toast.LENGTH_SHORT).show();
			}
		});
		leftmenuFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((SlidingActivity) getActivity()).showLeft();
				Toast.makeText(getActivity(), "friends", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
