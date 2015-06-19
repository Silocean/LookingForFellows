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
package com.hblg.lookingfellow.slidingmenu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.service.GetUserInfoService;
import com.hblg.lookingfellow.slidingmenu.fragment.LeftFragment;
import com.hblg.lookingfellow.slidingmenu.fragment.MainFragment;
import com.hblg.lookingfellow.slidingmenu.fragment.RightFragment;
import com.hblg.lookingfellow.slidingmenu.view.SlidingMenu;

public class SlidingActivity extends FragmentActivity {
	public SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	MainFragment mainFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		rightFragment = new RightFragment();
		t.replace(R.id.right_frame, rightFragment);

		mainFragment = new MainFragment();
		t.replace(R.id.center_frame, mainFragment);
		t.commit();
	}
	
	public void replaceFragment(int id, Fragment fragment) {
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		t.replace(id, fragment);
		t.commit();
	}


	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}
    
    long waitTime = 2000;
    long touchTime = 0;

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if((currentTime-touchTime)>=waitTime) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		}else {
			Intent intent = new Intent(getApplicationContext(), StartActivity.class);
			intent.putExtra("tag", "close");
			//最关键是这句 Intent.FLAG_ACTIVITY_CLEAR_TOP使得处于栈底的A发挥推土机的作用，从最底层把栈里所有的activity都清理掉，再在自己的oncreate方法加一句finish结束自己，即可实现退出。
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	        startActivity(intent);
	        
	        //停止获取用户信息服务
	        Intent service = new Intent(getApplicationContext(), GetUserInfoService.class);
			stopService(service);
		}
	}

}
