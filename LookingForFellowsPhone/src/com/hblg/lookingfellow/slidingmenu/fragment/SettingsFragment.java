package com.hblg.lookingfellow.slidingmenu.fragment;

import android.content.Intent;
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
import com.hblg.lookingfellow.slidingmenu.activity.PersonInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;

public class SettingsFragment extends Fragment implements OnClickListener{
	
	ImageView titlebarLeftmenu;
	
	Button personinfoButton;
	Button erweimaButton;
	Button notificationButton;
	Button chatbgButton;
	Button privacyButton;
	Button toolsButton;
	Button lockButton;
	Button feedbackButton;
	Button aboutButton;
	Button exitButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_settings, null);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_settings_leftmenu);
		titlebarLeftmenu.setOnClickListener(this);
		personinfoButton = (Button)view.findViewById(R.id.settings_personinfo_button);
		personinfoButton.setOnClickListener(this);
		erweimaButton = (Button)view.findViewById(R.id.settings_erweima_button);
		erweimaButton.setOnClickListener(this);
		notificationButton = (Button)view.findViewById(R.id.settings_notification_button);
		notificationButton.setOnClickListener(this);
		chatbgButton = (Button)view.findViewById(R.id.settings_chatbg_button);
		chatbgButton.setOnClickListener(this);
		privacyButton = (Button)view.findViewById(R.id.settings_privacy_button);
		privacyButton.setOnClickListener(this);
		toolsButton = (Button)view.findViewById(R.id.settings_tools_button);
		toolsButton.setOnClickListener(this);
		lockButton = (Button)view.findViewById(R.id.settings_lock_button);
		lockButton.setOnClickListener(this);
		feedbackButton = (Button)view.findViewById(R.id.settings_feedback_button);
		feedbackButton.setOnClickListener(this);
		aboutButton = (Button)view.findViewById(R.id.settings_about_button);
		aboutButton.setOnClickListener(this);
		exitButton = (Button)view.findViewById(R.id.settings_exit_button);
		exitButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_titlebar_settings_leftmenu:
			((SlidingActivity) getActivity()).showLeft();
			break;
		case R.id.settings_personinfo_button:
			Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.settings_erweima_button:
			//TODO
			Toast.makeText(getActivity(), "erweima", 0).show();
			break;
		case R.id.settings_notification_button:
			//TODO
			Toast.makeText(getActivity(), "notification", 0).show();
			break;
		case R.id.settings_chatbg_button:
			//TODO
			Toast.makeText(getActivity(), "chatbg", 0).show();
			break;
		case R.id.settings_privacy_button:
			//TODO
			Toast.makeText(getActivity(), "privacy", 0).show();
			break;
		case R.id.settings_tools_button:
			//TODO
			Toast.makeText(getActivity(), "tools", 0).show();
			break;
		case R.id.settings_lock_button:
			//TODO
			Toast.makeText(getActivity(), "lock", 0).show();
			break;
		case R.id.settings_feedback_button:
			//TODO
			Toast.makeText(getActivity(), "feedback", 0).show();
			break;
		case R.id.settings_about_button:
			//TODO
			Toast.makeText(getActivity(), "about", 0).show();
			break;
		case R.id.settings_exit_button:
			//TODO
			Toast.makeText(getActivity(), "exit", 0).show();
			break;
		default:
			break;
		}
	}

}
