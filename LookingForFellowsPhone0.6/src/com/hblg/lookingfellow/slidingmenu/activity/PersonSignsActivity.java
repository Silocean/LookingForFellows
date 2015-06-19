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

public class PersonSignsActivity extends Activity {
	Button gobackButton;
	Button saveButton;
	EditText signsEditText;
	
	String signs;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personsigns);
		gobackButton = (Button)this.findViewById(R.id.personsigns_goback);
		saveButton = (Button)this.findViewById(R.id.personsigns_save);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//TODO
				SQLiteService service = new SQLiteService(getApplicationContext());
				String qq = User.qq;
				signs = signsEditText.getText().toString().trim();
				service.modifySigns(signs, qq);
				dialog = ProgressDialog.show(PersonSignsActivity.this, "", "正在修改...", true);
				new ModifySignsThread().start();
			}
		});
		signsEditText = (EditText)this.findViewById(R.id.personsigns_content);
		String signs = getIntent().getStringExtra("signs");
		signsEditText.setText(signs);
	}
	private class ModifySignsThread extends Thread {
		@Override
		public void run() {
			modify();
		}

		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("signs", signs);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
}
