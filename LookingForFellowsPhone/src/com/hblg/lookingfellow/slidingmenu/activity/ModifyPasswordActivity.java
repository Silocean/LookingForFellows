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
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.selfdefinedwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;

public class ModifyPasswordActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button modifySaveButton;
	
	EditText nowEditText;
	EditText newEditText;
	
	String newPassword;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		gobackButton = (Button)this.findViewById(R.id.modifypassword_goback_button);
		gobackButton.setOnClickListener(this);
		modifySaveButton = (Button)this.findViewById(R.id.modifypassword_save);
		modifySaveButton.setOnClickListener(this);
		nowEditText = (EditText)this.findViewById(R.id.modifypassword_now_editText);
		nowEditText.addTextChangedListener(new MaxLengthWatcher(15, nowEditText, this));
		newEditText = (EditText)this.findViewById(R.id.modifypassword_new_editText);
		newEditText.addTextChangedListener(new MaxLengthWatcher(15, newEditText, this));
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
		case R.id.modifypassword_goback_button:
			this.finish();
			break;
		case R.id.modifypassword_save:
			if(check()) {
				String qq = User.qq;
				String oldPassword = User.password;
				if(oldPassword.equals(nowEditText.getText().toString().trim())) {
					SQLiteService service = new SQLiteService(getApplicationContext());
					newPassword = newEditText.getText().toString().trim();
					service.modifyPassword(newPassword, qq);
					dialog = ProgressDialog.show(ModifyPasswordActivity.this, "", "正在修改...", true);
					new ModifyPasswordThread().start();
				} else {
					Toast.makeText(getApplicationContext(), "原密码不正确", 0).show();
				}
			}
			break;
		default:
			break;
		}
	}
	
	private boolean check() {
		newPassword = newEditText.getText().toString().trim();
		if(newPassword.equals("")) {
			Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
			return false;
		} else if(!newPassword.matches("\\w{6,15}")) {
			Toast.makeText(getApplicationContext(), "密码为6-15位的a-zA-Z_0-9字符", 0).show();
			return false;
		}
		return true;
	}
	private class ModifyPasswordThread extends Thread {
		@Override
		public void run() {
			modify();
		}
		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("password", newPassword);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
