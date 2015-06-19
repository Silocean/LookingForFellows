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

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.slidingmenu.activity.PersonInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.CheckSDCard;
import com.hblg.lookingfellow.tools.ImageLoader;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.user.User;

public class PersonFragment extends Fragment {
	
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	ImageView headImage;
	
	Button hometownButton;
	Button signsButton;
	Button mobileButton;
	
	TextView titleBarTextView;
	TextView homeTownTextView;
	TextView signsTextView;
	TextView phoneTextView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_person, null);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_edit_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_edit_rightmenu);
		headImage = (ImageView)view.findViewById(R.id.personinfo_headimage);
		hometownButton = (Button)view.findViewById(R.id.personinfo_hometown_button);
		signsButton = (Button)view.findViewById(R.id.personinfo_signs_button);
		mobileButton = (Button)view.findViewById(R.id.personinfo_mobile_button);
		titleBarTextView = (TextView)view.findViewById(R.id.titlebar_title);
		homeTownTextView = (TextView)view.findViewById(R.id.hometown_textview);
		signsTextView = (TextView)view.findViewById(R.id.signs_textview);
		phoneTextView = (TextView)view.findViewById(R.id.phone_textview);
		initStuInfo();
		return view;
	}
	@Override
	public void onResume() {
		initStuInfo();
		super.onResume();
	}
	
	private void initStuInfo() {
		Bitmap bitmap = ImageTool.getHeadImageFromLocalOrNet(getActivity(), User.qq);
		Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
		headImage.setImageBitmap(output);
		SQLiteService service = new SQLiteService(getActivity());
		String qq = User.qq;
		Student student = service.getStuInfo(qq);
		titleBarTextView.setText(student.getName());
		homeTownTextView.setText(student.getHometown());
		if(student.getSigns().equals("")) {
			signsTextView.setText("未设置");
		} else {
			signsTextView.setText(student.getSigns());
		}
		if(student.getPhone().equals("")) {
			phoneTextView.setText("未设置");
		} else {
			phoneTextView.setText(student.getPhone());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//对头像做圆形处理
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_default);
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
				/*Intent intent = new Intent(getActivity(), ChooseProvinceActivity.class);
				intent.putExtra("tag", "modify_fragment");
				startActivity(intent);*/
			}
		});
		signsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*Intent intent = new Intent(getActivity(), PersonSignsActivity.class);
				if(signsTextView.getText().toString().trim().equals("未设置")) {
					intent.putExtra("signs", "");
				} else {
					intent.putExtra("signs", signsTextView.getText().toString().trim());
				}
				startActivity(intent);*/
			}
		});
		mobileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*Intent intent = new Intent(getActivity(), ModifyMobileActivity.class);
				if(phoneTextView.getText().toString().trim().equals("未设置")) {
					intent.putExtra("mobile", "");
				} else {
					intent.putExtra("mobile", phoneTextView.getText().toString().trim());
				}
				startActivity(intent);*/
			}
		});
	}

}
