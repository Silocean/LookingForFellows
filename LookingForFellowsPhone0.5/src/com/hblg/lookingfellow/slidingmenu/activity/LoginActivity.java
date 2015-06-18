package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.io.StreamTokenizer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
import com.hblg.lookingfellow.tools.StreamTool;

public class LoginActivity extends Activity implements OnClickListener{
	Button sureButton;
	Button gobackButton;
	EditText qqEditText;
	EditText passwordEditText;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sureButton = (Button)this.findViewById(R.id.login_sure);
		sureButton.setOnClickListener(this);
		gobackButton = (Button)this.findViewById(R.id.login_goback_button);
		gobackButton.setOnClickListener(this);
		qqEditText = (EditText)this.findViewById(R.id.qq_editText);
		passwordEditText = (EditText)this.findViewById(R.id.password_editText);
	}
	
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "登录成功", 0).show();
				Intent intent = new Intent(getApplicationContext(), SlidingActivity.class);
				startActivity(intent);
			} else if(msg.arg1 == 2) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "登录失败", 0).show();
			} else if(msg.arg1 == 3) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接出现问题", 0).show();
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_goback_button:
			finish();
			break;
		case R.id.login_sure:
			if(check()) {
				dialog = ProgressDialog.show(LoginActivity.this, "", "正在登录，请稍等...", true);
				new LoginThread().start();
			}
			break;
		default:
			break;
		}
	}
	
	private class LoginThread extends Thread {
		@Override
		public void run() {
			String qq = qqEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			message = new Message();
			login(qq, password);
			handler.sendMessage(message);
		}

		private void login(String qq, String password) {
			try {
				String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/UserLoginServlet";
				Map<String, String> params = new HashMap<String, String>();
				params.put("qq", qq);
				params.put("password", password);
				StringBuilder sb = new StringBuilder(path);
				sb.append("?");
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey() + "=");
					sb.append(URLEncoder.encode(entry.getValue(), "utf-8") + "&");
				}
				sb.deleteCharAt(sb.length()-1);
				HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString()).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				if(conn.getResponseCode() == 200) {
					InputStream in = conn.getInputStream();
					byte[] data = StreamTool.read(in);
					String str = new String(data);
					if(str.contains("1")) {
						message.arg1 = 1; //表死登录成功
						return;
					} else if(str.contains("2")) {
						message.arg1 = 2; //表示登录失败
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			message.arg1 = 3; //表示网络连接出现问题
		}
	}

	private boolean check() {
		String qq = qqEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if(qq.equals("")) {
			Toast.makeText(getApplicationContext(), "请填写QQ号码", 0).show();
			return false;
		} else if(password.equals("")) {
			Toast.makeText(getApplicationContext(), "请填写密码", 0).show();
			return false;
		}
		return true;
	}

}
