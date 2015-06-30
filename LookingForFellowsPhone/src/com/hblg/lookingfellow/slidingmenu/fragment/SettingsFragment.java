package com.hblg.lookingfellow.slidingmenu.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.ChatBgActivity;
import com.hblg.lookingfellow.slidingmenu.activity.FeedBackActivity;
import com.hblg.lookingfellow.slidingmenu.activity.PersonInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.QRCodeActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.tools.MySharePreferences;
import com.hblg.lookingfellow.tools.UIMode;
import com.hblg.lookingfellow.tools.UpdateManager;

public class SettingsFragment extends Fragment implements OnClickListener{
	private FragmentActivity fragmentActivity;
	
	private ImageView titlebarLeftmenu;
	
	private Button personinfoButton;
	private Button erweimaButton;
	
	private Button lightModeButton;
	private Button chatbgButton;
	private Button onPhotoButton;
	
	private Button updateButton;
	private Button feedbackButton;
	private Button aboutButton;
	
	private Button exitButton;
	
	private TextView UIModeTxt;//亮度模式
	/**是否开启图片*/
	private ImageView photoImg;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_settings, null);
		fragmentActivity=getActivity();
		
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_settings_leftmenu);
		titlebarLeftmenu.setOnClickListener(this);
		
		personinfoButton = (Button)view.findViewById(R.id.settings_personinfo_button);
		personinfoButton.setOnClickListener(this);
		erweimaButton = (Button)view.findViewById(R.id.settings_erweima_button);
		erweimaButton.setOnClickListener(this);
		
		lightModeButton=(Button)view.findViewById(R.id.settings_lightmode_button);
		lightModeButton.setOnClickListener(this);
		chatbgButton = (Button)view.findViewById(R.id.settings_chatbg_button);
		chatbgButton.setOnClickListener(this);
		onPhotoButton=(Button)view.findViewById(R.id.settings_photo_on_button);
		onPhotoButton.setOnClickListener(this);
		
		updateButton=(Button)view.findViewById(R.id.settings_update_button);
		updateButton.setOnClickListener(this);
		feedbackButton = (Button)view.findViewById(R.id.settings_feedback_button);
		feedbackButton.setOnClickListener(this);
		aboutButton = (Button)view.findViewById(R.id.settings_about_button);
		aboutButton.setOnClickListener(this);
		
		exitButton = (Button)view.findViewById(R.id.settings_exit_button);
		exitButton.setOnClickListener(this);
		
		SharedPreferences sf = MySharePreferences.getShare(fragmentActivity);
		
		//亮度模式
		UIModeTxt = (TextView)view.findViewById(R.id.settings_uimode_txt);
		int flag = sf.getInt(MySharePreferences.UI, 2);
		
		if(2 == flag) {
			UIMode.changeUIMode(fragmentActivity, 0);
			UIModeTxt.setText("弱光模式");
		} else {
			UIMode.changeUIMode(fragmentActivity, 2);
			UIModeTxt.setText("强光模式");
		}

		//是否开启图片
		photoImg=(ImageView)view.findViewById(R.id.setting_on_off_img);
		int status=MySharePreferences.getShare(fragmentActivity).getInt(MySharePreferences.PHOTODEAL, 1);
		if (1==status) {
			photoImg.setBackgroundDrawable(fragmentActivity.getResources().getDrawable(R.drawable.modifysex_radiobutton_checked));
		}else{
			photoImg.setBackgroundDrawable(fragmentActivity.getResources().getDrawable(R.drawable.modifysex_radiobutton_unchecked));
		}
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		
		case R.id.main_titlebar_settings_leftmenu:
			((SlidingActivity) getActivity()).showLeft();
			break;
		case R.id.settings_personinfo_button:
			intent = new Intent(getActivity(), PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.settings_erweima_button:
			intent=new Intent(getActivity(),QRCodeActivity.class);
			startActivity(intent);
			break;
		case R.id.settings_lightmode_button:
			int flag=MySharePreferences.getShare(fragmentActivity).getInt(MySharePreferences.UI, 2);
			if(2==flag){
				MySharePreferences.getShare(fragmentActivity).edit().putInt(MySharePreferences.UI, 0).commit();
				UIMode.changeUIMode(fragmentActivity, 0);
				UIModeTxt.setText("强光模式");
			}else{
				MySharePreferences.getShare(fragmentActivity).edit().putInt(MySharePreferences.UI, 2).commit();
				UIMode.changeUIMode(fragmentActivity, 2);
				UIModeTxt.setText("弱光模式");
			}
			break;
		case R.id.settings_chatbg_button:
			intent = new Intent(getActivity(), ChatBgActivity.class);
			startActivity(intent);
			break;
		case R.id.settings_photo_on_button:
			int status=MySharePreferences.getShare(fragmentActivity).getInt(MySharePreferences.PHOTODEAL, 1);
			if (1==status) {
				photoImg.setBackgroundDrawable(fragmentActivity.getResources().getDrawable(R.drawable.modifysex_radiobutton_unchecked));
				MySharePreferences.getShare(fragmentActivity).edit().putInt(MySharePreferences.PHOTODEAL,0).commit();

			}else{
				photoImg.setBackgroundDrawable(fragmentActivity.getResources().getDrawable(R.drawable.modifysex_radiobutton_checked));
				MySharePreferences.getShare(fragmentActivity).edit().putInt(MySharePreferences.PHOTODEAL,1).commit();
			}
			break;
		case R.id.settings_update_button:
			UpdateManager.getUpdateManager().checkAppUpdate(fragmentActivity, true);
			break;
		case R.id.settings_feedback_button:
			intent=new Intent(fragmentActivity,FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.settings_about_button:
			Toast.makeText(getActivity(), "about", 0).show();
			break;
		case R.id.settings_exit_button:
			Toast.makeText(getActivity(), "exit", 0).show();
			break;
		default:
			break;
		}
	}


}
