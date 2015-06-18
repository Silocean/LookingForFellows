package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hblg.lookingfellow.R;

public class RegisterAndLoginActivity extends Activity implements OnClickListener{
	Button registerButton;
	Button loginButton;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regandlogin);
		registerButton = (Button)this.findViewById(R.id.registerButton);
		registerButton.setOnClickListener(this);
		loginButton = (Button)this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerButton:
			intent = new Intent(getApplicationContext(), RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.loginButton:
			intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
