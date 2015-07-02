package com.hblg.lookingfellow.slidingmenu.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.customwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;

public class ModifyMobileActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button modifySaveButton;
	EditText modifyMobile;
	
	String mobile;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("ModifyMobileActivity", this);
		setContentView(R.layout.activity_modifymobile);
		gobackButton = (Button)this.findViewById(R.id.modifymobile_goback_button);
		gobackButton.setOnClickListener(this);
		modifySaveButton = (Button)this.findViewById(R.id.modifymobile_save);
		modifySaveButton.setOnClickListener(this);
		modifyMobile = (EditText)this.findViewById(R.id.modifyMobile_editText);
		modifyMobile.addTextChangedListener(new MaxLengthWatcher(11, modifyMobile, this));
		String mobile = getIntent().getStringExtra("mobile");
		modifyMobile.setText(mobile);
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
		case R.id.modifymobile_goback_button:
			this.finish();
			break;
		case R.id.modifymobile_save:
			//TODO
			if(check()) {
				SQLiteService service = new SQLiteService(getApplicationContext());
				String qq = User.qq;
				mobile = modifyMobile.getText().toString().trim();
				service.modifyMobile(mobile, qq);
				dialog = ProgressDialog.show(ModifyMobileActivity.this, "", "正在修改...", true);
				new ModifyMobileThread().start();
			}
			break;
		default:
			break;
		}
	}
	
	private boolean check() {
		mobile = modifyMobile.getText().toString().trim();
		if(!mobile.matches("\\d{7,11}")) {
			Toast.makeText(getApplicationContext(), "手机号码格式不正确", 0).show();
			return false;
		}
		return true;
	}
	private class ModifyMobileThread extends Thread {
		public void run() {
			modify();
		}

		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("mobile", mobile);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
