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
import android.widget.Button;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;

public class LeftFragment extends Fragment {
	
	
	Button leftmenuPosts;
	Button leftmenuPerson;
	Button leftmenuFriends;
	Button leftmenuMsg;
	Button leftmenuSettings;
	
	Button leftmenuCross;
	Button leftmenuNear;
	Button leftmenuShare;
	
	Button leftmenuInvite;
	
	MainFragment mainFragment;
	PersonFragment personFragment;
	FriendsFragment friendsFragment;
	MsgFragment msgFragment;
	SettingsFragment settingsFragment;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		leftmenuPosts = (Button)view.findViewById(R.id.leftmenu_posts_button);
		leftmenuPerson = (Button)view.findViewById(R.id.leftmenu_person_button);
		leftmenuFriends = (Button)view.findViewById(R.id.leftmenu_friends_button);
		leftmenuMsg = (Button)view.findViewById(R.id.leftmenu_msg_button);
		leftmenuSettings = (Button)view.findViewById(R.id.leftmenu_settings_button);
		leftmenuCross = (Button)view.findViewById(R.id.leftmenu_cross_button);
		leftmenuNear = (Button)view.findViewById(R.id.leftmenu_near_button);
		leftmenuShare = (Button)view.findViewById(R.id.leftmenu_share_button);
		leftmenuInvite = (Button)view.findViewById(R.id.leftmenu_invite_button);
		mainFragment = new MainFragment();
		personFragment = new PersonFragment();
		settingsFragment = new SettingsFragment();
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		leftmenuPosts.setSelected(true);
		leftmenuPosts.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				leftmenuPosts.setSelected(true);
				leftmenuPerson.setSelected(false);
				leftmenuFriends.setSelected(false);
				leftmenuMsg.setSelected(false);
				leftmenuSettings.setSelected(false);
				((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, SlidingActivity.mainFragment);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuPerson.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				leftmenuPosts.setSelected(false);
				leftmenuPerson.setSelected(true);
				leftmenuFriends.setSelected(false);
				leftmenuMsg.setSelected(false);
				leftmenuSettings.setSelected(false);
				((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, personFragment);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuFriends.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				leftmenuPosts.setSelected(false);
				leftmenuPerson.setSelected(false);
				leftmenuFriends.setSelected(true);
				leftmenuMsg.setSelected(false);
				leftmenuSettings.setSelected(false);
				if(friendsFragment == null) {
					friendsFragment = new FriendsFragment();
				}
				((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, friendsFragment);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuMsg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				leftmenuPosts.setSelected(false);
				leftmenuPerson.setSelected(false);
				leftmenuFriends.setSelected(false);
				leftmenuMsg.setSelected(true);
				leftmenuSettings.setSelected(false);
				msgFragment = new MsgFragment();
				if(msgFragment == null) {
					msgFragment = new MsgFragment();
				}
				((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, msgFragment);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				leftmenuPosts.setSelected(false);
				leftmenuPerson.setSelected(false);
				leftmenuFriends.setSelected(false);
				leftmenuMsg.setSelected(false);
				leftmenuSettings.setSelected(true);
				((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, settingsFragment);
				((SlidingActivity)getActivity()).showLeft();
			}
		});
		leftmenuCross.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getActivity(), "cross", 0).show();
			}
		});
		leftmenuNear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getActivity(), "near", 0).show();
			}
		});
		leftmenuShare.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getActivity(), "share", 0).show();
			}
		});
		leftmenuInvite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getActivity(), "invite", 0).show();
			}
		});
	}

}
