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

import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ClientConnServerThread;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.model.ManageClientConnServer;
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
		ManageActivity.addActiviy("SlidingActivity", this);
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
			// 返回到桌面，不是真正的退出
			/*Intent i = new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);*/
			
			if(ManageActivity.getActivity("LoginActivity")!=null){
	    		ManageActivity.getActivity("LoginActivity").finish();
	    	}
			
			// 给服务器发出一条消息告知自己下线了
			try {
				Message msg = new Message();
				msg.setType(MessageType.MSG_EXIT);
				msg.setSender(User.qq);
				ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
				oos.writeObject(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 将线程从线程池里移除
			ClientConnServerThread cct = ManageClientConnServer.getClientConServerThread(User.qq);
			cct.flag = false; // 停止线程
			ManageClientConnServer.removeClientConServerThread(User.qq);
			
			System.out.println("用户：" + User.qq + "下线");
			
	    	this.finish();
	        
	        //停止获取用户信息服务
	        Intent service = new Intent(getApplicationContext(), GetUserInfoService.class);
			stopService(service);
		}
	}

}
