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

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.tools.ImageTool;

public class LeftFragment extends Fragment {
	
	MainFragment mainFragment;
	ImageView headImage;
	Button leftmenuMsg;
	Button leftmenuFriend;
	Button leftmenuSettings;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		headImage = (ImageView)view.findViewById(R.id.headimage);
		leftmenuMsg = (Button)view.findViewById(R.id.leftmenu_message);
		leftmenuFriend = (Button)view.findViewById(R.id.leftmenu_friend);
		leftmenuSettings = (Button)view.findViewById(R.id.leftmenu_settings);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
		headImage.setImageBitmap(output);
		leftmenuMsg.setSelected(true); //初始化的时候设置消息按钮项为默认选中状态
		leftmenuMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				leftmenuMsg.setSelected(true);
				leftmenuFriend.setSelected(false);
				leftmenuSettings.setSelected(false);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				leftmenuMsg.setSelected(false);
				leftmenuFriend.setSelected(true);
				leftmenuSettings.setSelected(false);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				leftmenuMsg.setSelected(false);
				leftmenuFriend.setSelected(false);
				leftmenuSettings.setSelected(true);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
	}

}
