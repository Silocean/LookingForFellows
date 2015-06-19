package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;

public class ModifySexActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	Button gobackButton;
	Button modifySexButton;
	RadioGroup radioGroup;
	RadioButton maleRadioButton;
	RadioButton femaleRadioButton;
	
	String sex;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifysex);
		gobackButton = (Button)this.findViewById(R.id.modifysex_goback_button);
		gobackButton.setOnClickListener(this);
		radioGroup = (RadioGroup)this.findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		maleRadioButton = (RadioButton)this.findViewById(R.id.modifysex_radiobutton_male);
		femaleRadioButton = (RadioButton)this.findViewById(R.id.modifysex_radiobutton_female);
		String sex = getIntent().getStringExtra("sex");
		if(sex.equals("男")) {
			Log.i("ModifySexActivity", "male");
			maleRadioButton.setSelected(true);
		} else if(sex.equals("女")) {
			Log.i("ModifySexActivity", "female");
			femaleRadioButton.setSelected(true);
		}
	}
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1) {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存成功", 0).show();
				Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
				startActivity(intent);
			} else {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存失败", 0).show();
			}
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modifysex_goback_button:
			this.finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.modifysex_radiobutton_male:
			//TODO
			changeSex();
			break;
		case R.id.modifysex_radiobutton_female:
			//TODO
			changeSex();
			break;
		default:
			break;
		}
	}
	private void changeSex() {
		SQLiteService service = new SQLiteService(getApplicationContext());
		String qq = User.qq;
		if(maleRadioButton.isChecked()) {
			sex = "男";
		} else {
			sex = "女";
		}
		service.modifySex(sex, qq);
		dialog = ProgressDialog.show(ModifySexActivity.this, "", "正在修改...", true);
		new ModifySexThread().start();
	}
	
	private class ModifySexThread extends Thread {
		@Override
		public void run() {
			modify();
		}
		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("sex", sex);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
