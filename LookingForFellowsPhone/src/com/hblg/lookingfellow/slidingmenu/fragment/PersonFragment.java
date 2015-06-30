package com.hblg.lookingfellow.slidingmenu.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.PersonInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;

public class PersonFragment extends Fragment {
	
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	ImageView headImage;
	ImageView headImageBg;
	
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
		headImageBg = (ImageView)view.findViewById(R.id.personinfo_headimage_bg);
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
		Bitmap headBg = ImageTool.getHeadImageBgFromLocalOrNet(getActivity(), User.qq);
		headImageBg.setBackgroundDrawable(new BitmapDrawable(headBg));
		Bitmap head = ImageTool.getHeadImageFromLocalOrNet(getActivity(), User.qq);
		Bitmap output = ImageTool.toRoundCorner(head, 360.0f);
		headImage.setImageBitmap(output);
		SQLiteService service = new SQLiteService(getActivity());
		String qq = User.qq;
		Student student = service.getStuInfo(qq);
		titleBarTextView.setText(student.getName());
		homeTownTextView.setText(student.getProvince() + " " + student.getCity());
		if(student.getSigns().equals("")) {
			signsTextView.setText("Œ¥…Ë÷√");
		} else {
			signsTextView.setText(student.getSigns());
		}
		if(student.getPhone().equals("")) {
			phoneTextView.setText("Œ¥…Ë÷√");
		} else {
			phoneTextView.setText(student.getPhone());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
				if(signsTextView.getText().toString().trim().equals("Œ¥…Ë÷√")) {
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
				if(phoneTextView.getText().toString().trim().equals("Œ¥…Ë÷√")) {
					intent.putExtra("mobile", "");
				} else {
					intent.putExtra("mobile", phoneTextView.getText().toString().trim());
				}
				startActivity(intent);*/
			}
		});
	}

}
