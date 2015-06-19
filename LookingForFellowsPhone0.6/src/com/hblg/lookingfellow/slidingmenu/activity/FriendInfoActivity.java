package com.hblg.lookingfellow.slidingmenu.activity;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.ImageTool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FriendInfoActivity extends Activity {
	
	Button titlebarLeftmenu;
	Button titlebarRightmenu;
	
	ImageView headImage;
	
	Button hometownButton;
	Button signsButton;
	Button mobileButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendinfo);
		titlebarLeftmenu = (Button)this.findViewById(R.id.main_titlebar_goback_leftmenu);
		titlebarRightmenu = (Button)this.findViewById(R.id.main_titlebar_gomore_rightmenu);
		headImage = (ImageView)this.findViewById(R.id.friendinfo_headimage);
		hometownButton = (Button)this.findViewById(R.id.friendinfo_hometown_button);
		signsButton = (Button)this.findViewById(R.id.friendinfo_signs_button);
		mobileButton = (Button)this.findViewById(R.id.friendinfo_mobile_button);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
		headImage.setImageBitmap(output);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getApplicationContext(), "moreInfo", 0).show();
			}
		});
		hometownButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		signsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		mobileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}

}
