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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.fragment.LeftFragment;
import com.hblg.lookingfellow.slidingmenu.fragment.MainFragment;
import com.hblg.lookingfellow.slidingmenu.fragment.RightFragment;
import com.hblg.lookingfellow.slidingmenu.view.SlidingMenu;

public class SlidingActivity extends FragmentActivity {
	SlidingMenu mSlidingMenu;
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


	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

}
