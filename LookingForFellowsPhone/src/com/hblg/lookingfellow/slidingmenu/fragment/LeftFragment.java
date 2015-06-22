package com.hblg.lookingfellow.slidingmenu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
/**
 * 左部fragment
 * @author Silocean
 *
 */
public class LeftFragment extends Fragment implements OnClickListener{
	
	
	Button leftmenuPosts;
	Button leftmenuPerson;
	Button leftmenuFriends;
	Button leftmenuMsg;
	ImageView newMsgNotificationImageView;
	Button leftmenuSettings;
	
	Button leftmenuCross;
	Button leftmenuNear;
	Button leftmenuShare;
	
	Button leftmenuInvite;
	
	
	PersonFragment personFragment;
	MainFragment mainFragment;
	FriendsFragment friendsFragment;
	MsgFragment msgFragment;
	SettingsFragment settingsFragment;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		leftmenuPosts = (Button)view.findViewById(R.id.leftmenu_posts_button);
		leftmenuPosts.setOnClickListener(this);
		leftmenuPerson = (Button)view.findViewById(R.id.leftmenu_person_button);
		leftmenuPerson.setOnClickListener(this);
		leftmenuFriends = (Button)view.findViewById(R.id.leftmenu_friends_button);
		leftmenuFriends.setOnClickListener(this);
		leftmenuMsg = (Button)view.findViewById(R.id.leftmenu_msg_button);
		leftmenuMsg.setOnClickListener(this);
		newMsgNotificationImageView = (ImageView)view.findViewById(R.id.leftmenu_msg_new_notification);
		leftmenuSettings = (Button)view.findViewById(R.id.leftmenu_settings_button);
		leftmenuSettings.setOnClickListener(this);
		leftmenuCross = (Button)view.findViewById(R.id.leftmenu_cross_button);
		leftmenuCross.setOnClickListener(this);
		leftmenuNear = (Button)view.findViewById(R.id.leftmenu_near_button);
		leftmenuNear.setOnClickListener(this);
		leftmenuShare = (Button)view.findViewById(R.id.leftmenu_share_button);
		leftmenuShare.setOnClickListener(this);
		leftmenuInvite = (Button)view.findViewById(R.id.leftmenu_invite_button);
		leftmenuInvite.setOnClickListener(this);
		mainFragment = new MainFragment();
		return view;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		leftmenuPosts.setSelected(true); // 默认选中“公告板”
		if(Common.newMsg) {
			newMsgNotificationImageView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftmenu_posts_button:
			leftmenuPosts.setSelected(true);
			leftmenuPerson.setSelected(false);
			leftmenuFriends.setSelected(false);
			leftmenuMsg.setSelected(false);
			leftmenuSettings.setSelected(false);
			((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, mainFragment);
			((SlidingActivity)getActivity()).showLeft();
			break;
		case R.id.leftmenu_person_button:
			leftmenuPosts.setSelected(false);
			leftmenuPerson.setSelected(true);
			leftmenuFriends.setSelected(false);
			leftmenuMsg.setSelected(false);
			leftmenuSettings.setSelected(false);
			if(personFragment == null) {
				personFragment = new PersonFragment();
			}
			((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, personFragment);
			((SlidingActivity)getActivity()).showLeft();
			break;
		case R.id.leftmenu_friends_button:
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
			break;
		case R.id.leftmenu_msg_button:
			leftmenuPosts.setSelected(false);
			leftmenuPerson.setSelected(false);
			leftmenuFriends.setSelected(false);
			leftmenuMsg.setSelected(true);
			leftmenuSettings.setSelected(false);
			if(msgFragment == null) {
				msgFragment = new MsgFragment();
			}
			((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, msgFragment);
			((SlidingActivity)getActivity()).showLeft();
			break;
		case R.id.leftmenu_settings_button:
			leftmenuPosts.setSelected(false);
			leftmenuPerson.setSelected(false);
			leftmenuFriends.setSelected(false);
			leftmenuMsg.setSelected(false);
			leftmenuSettings.setSelected(true);
			if(settingsFragment == null) {
				settingsFragment = new SettingsFragment();
			}
			((SlidingActivity)getActivity()).replaceFragment(R.id.center_frame, settingsFragment);
			((SlidingActivity)getActivity()).showLeft();
			break;
		case R.id.leftmenu_cross_button:
			//TODO
			Toast.makeText(getActivity(), "cross", 0).show();
			break;
		case R.id.leftmenu_near_button:
			//TODO
			Toast.makeText(getActivity(), "near", 0).show();
			break;
		case R.id.leftmenu_share_button:
			//TODO
			Toast.makeText(getActivity(), "share", 0).show();
			break;
		case R.id.leftmenu_invite_button:
			//TODO
			Toast.makeText(getActivity(), "invite", 0).show();
			break;
		default:
			break;
		}
	}

}
