package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.selfdefinedwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.tools.StreamTool;

public class RegisterActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button hometownButton;
	EditText nameEditText;
	EditText qqEditText;
	EditText passwordEditText;
	TextView hometownTextView;
	Bundle bundle;
	Button sureButton;
	
	ProgressDialog dialog;
	UIHandler handler = new UIHandler();
	Message  message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		gobackButton = (Button)this.findViewById(R.id.register_goback_button);
		gobackButton.setOnClickListener(this);
		hometownButton = (Button)this.findViewById(R.id.register_hometown_bg_button);
		hometownButton.setOnClickListener(this);
		nameEditText = (EditText)this.findViewById(R.id.name_editText);
		nameEditText.addTextChangedListener(new MaxLengthWatcher(20, nameEditText, this));
		qqEditText = (EditText)this.findViewById(R.id.qq_editText);
		qqEditText.addTextChangedListener(new MaxLengthWatcher(11, qqEditText, this));
		passwordEditText = (EditText)this.findViewById(R.id.password_editText);
		passwordEditText.addTextChangedListener(new MaxLengthWatcher(15, passwordEditText, this));
		hometownTextView = (TextView)this.findViewById(R.id.hometown_textView);
		String name = nameEditText.getText().toString();
		String qq = qqEditText.getText().toString();
		nameEditText.setText(name);
		qqEditText.setText(qq);
		sureButton = (Button)this.findViewById(R.id.register_sure);
		sureButton.setOnClickListener(this);
	}
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 0) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "注册成功", 0).show();
				dialog = null;
			} else if(msg.arg1 == 1) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接出现问题", 0).show();
				dialog = null;
			} else if(msg.arg1 == 2) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "该qq号已被注册", 0).show();
				dialog = null;
			}else if(msg.arg1 == 3) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "服务器端出问题", 0).show();
				dialog = null;
			}
		}
		
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
				dialog = ProgressDialog.show(RegisterActivity.this, "", "正在注册，请稍等...", true);
				new RegisterThread().start();
			}
			break;
		default:
			break;
		}
	}
	
	private class RegisterThread extends Thread {
		public void run() {
			String qq = qqEditText.getText().toString().trim();
			String name = nameEditText.getText().toString().trim();
			//一定要去掉空格，否则在用get方式提交数据的时候会出错
			String hometown = hometownTextView.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			message = new Message();
			register(qq, name, hometown, password);
			//System.out.println(message.arg1);
			handler.sendMessage(message);
		}
	}

	private void register(String qq, String name, String hometown, String password) {
		try {
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/UserRegisterServlet";
			Map<String, String> params = new HashMap<String, String>();
			params.put("qq", qq);
			params.put("name", name);
			params.put("hometown", hometown);
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
					System.out.println(str);
					message.arg1 = 3; //3.服务器端出问题
					return;
				} else if(str.contains("2")) {
					System.out.println(str);
					message.arg1 = 2; //2.qq号码已被注册
					return;
				}
				message.arg1 = 0; // 0.注册成功
				return;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.arg1 = 1; //网络连接出现问题
	}

	private boolean check() {
		String hometown = hometownTextView.getText().toString().trim();
		String name = nameEditText.getText().toString().trim();
		String qq = qqEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if(hometown.equals("请选择你的老家")) {
			Toast.makeText(getApplicationContext(), "请填写家乡所在地", 0).show();
			return false;
		} else if(name.equals("")) {
			Toast.makeText(getApplicationContext(), "请填写姓名", 0).show();
			return false;
		} else if(qq.equals("")) {
			Toast.makeText(getApplicationContext(), "请填写QQ号码", 0).show();
			return false;
		} else if(password.equals("")) {
			Toast.makeText(getApplicationContext(), "请填写密码", 0).show();
			return false;
		} else if(!qq.matches("\\d{4,11}")) {
			Toast.makeText(getApplicationContext(), "qq号码格式不正确", 0).show();
			return false;
		} else if(!password.matches("\\w{6,15}")) {
			Toast.makeText(getApplicationContext(), "密码为6-15位的a-zA-Z_0-9字符", 0).show();
			return false;
		}
		return true;
	}

}
