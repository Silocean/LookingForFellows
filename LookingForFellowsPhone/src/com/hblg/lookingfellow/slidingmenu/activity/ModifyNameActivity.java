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
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;
import com.hblg.lookingfellow.user.User;

public class ModifyNameActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button modifySaveButton;
	EditText modifyName;
	
	String name;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifyname);
		gobackButton = (Button)this.findViewById(R.id.modifyname_goback_button);
		gobackButton.setOnClickListener(this);
		modifySaveButton = (Button)this.findViewById(R.id.modifyname_save);
		modifySaveButton.setOnClickListener(this);
		modifyName = (EditText)this.findViewById(R.id.modifyname_editText);
		String name = getIntent().getStringExtra("name");
		modifyName.setText(name);
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
		case R.id.modifyname_goback_button:
			this.finish();
			break;
		case R.id.modifyname_save:
			//TODO
			SQLiteService service = new SQLiteService(getApplicationContext());
			String qq = User.qq;
			name = modifyName.getText().toString().trim();
			service.modifyName(name, qq);
			dialog = ProgressDialog.show(ModifyNameActivity.this, "", "正在修改...", true);
			new ModifyNameThread().start();
			break;
		default:
			break;
		}
	}
	
	private class ModifyNameThread extends Thread {
		@Override
		public void run() {
			modify();
		}

		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("name", name);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
