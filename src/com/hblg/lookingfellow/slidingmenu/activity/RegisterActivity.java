package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;

public class RegisterActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button hometownButton;
	EditText nameEditText;
	EditText qqEditText;
	TextView hometownTextView;
	Bundle bundle;
	Button sureButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		gobackButton = (Button)this.findViewById(R.id.register_goback_button);
		gobackButton.setOnClickListener(this);
		hometownButton = (Button)this.findViewById(R.id.register_hometown_bg_button);
		hometownButton.setOnClickListener(this);
		nameEditText = (EditText)this.findViewById(R.id.name_editText);
		qqEditText = (EditText)this.findViewById(R.id.qq_editText);
		hometownTextView = (TextView)this.findViewById(R.id.hometown_textView);
		String name = nameEditText.getText().toString();
		String qq = qqEditText.getText().toString();
		nameEditText.setText(name);
		qqEditText.setText(qq);
		sureButton = (Button)this.findViewById(R.id.register_sure);
		sureButton.setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		bundle = intent.getExtras();
		String provinceName = bundle.getString("province");
		String cityName = bundle.getString("city");
		hometownTextView.setText(provinceName + " " + cityName);
		super.onNewIntent(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_goback_button:
			finish();
			break;
		case R.id.register_hometown_bg_button:
			Intent intent = new Intent(getApplicationContext(), ChooseProvinceActivity.class);
			intent.putExtra("tag", "register");
			startActivity(intent);
			break;
		case R.id.register_sure:
			if(check()) {
				//TODO
			}
			break;
		default:
			break;
		}
	}

	private boolean check() {
		//Toast.makeText(getApplicationContext(), nameEditText.getText().toString(), 0).show();
		if(hometownTextView.getText().toString().equals("请选择你的老家")) {
			Toast.makeText(getApplicationContext(), "请填写家乡所在地", 0).show();
			return false;
		} else if(nameEditText.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "请填写姓名", 0).show();
			return false;
		} else if(qqEditText.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "请填写QQ号码", 0).show();
			return false;
		}
		return true;
	}

}
