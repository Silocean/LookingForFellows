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

import android.content.Intent;
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
import com.hblg.lookingfellow.slidingmenu.activity.ChooseProvinceActivity;
import com.hblg.lookingfellow.slidingmenu.activity.ModifyMobileActivity;
import com.hblg.lookingfellow.slidingmenu.activity.PersonInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.PersonSignsActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.tools.ImageTool;

public class PersonFragment extends Fragment {
	
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	ImageView headImage;
	
	Button hometownButton;
	Button signsButton;
	Button mobileButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_person, null);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_edit_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_edit_rightmenu);
		headImage = (ImageView)view.findViewById(R.id.personinfo_headimage);
		hometownButton = (Button)view.findViewById(R.id.personinfo_hometown_button);
		signsButton = (Button)view.findViewById(R.id.personinfo_signs_button);
		mobileButton = (Button)view.findViewById(R.id.personinfo_mobile_button);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//对头像做圆形处理
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
		headImage.setImageBitmap(output);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
				startActivity(intent);
			}
		});
		hometownButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ChooseProvinceActivity.class);
				startActivity(intent);
			}
		});
		signsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PersonSignsActivity.class);
				startActivity(intent);
			}
		});
		mobileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ModifyMobileActivity.class);
				startActivity(intent);
			}
		});
	}

}
