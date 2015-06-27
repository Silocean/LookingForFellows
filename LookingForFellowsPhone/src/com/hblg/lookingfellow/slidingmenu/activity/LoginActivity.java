package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.LoginUser;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ChatClient;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.model.ManageClientConnServer;
import com.hblg.lookingfellow.service.GetUserInfoService;
import com.hblg.lookingfellow.sqlite.SQLiteService;
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
		ManageActivity.addActiviy("LoginActivity", this);
		setContentView(R.layout.activity_login);
		sureButton = (Button)this.findViewById(R.id.login_sure);
		sureButton.setOnClickListener(this);
		gobackButton = (Button)this.findViewById(R.id.login_goback_button);
		gobackButton.setOnClickListener(this);
		qqEditText = (EditText)this.findViewById(R.id.qq_editText);
		passwordEditText = (EditText)this.findViewById(R.id.password_editText);
		
		SQLiteService service = new SQLiteService(getApplicationContext());
		
	}
	
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1) {
				dialog.dismiss();
				//保存qq和密码到User类中，以便使用
				String qq = qqEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();
				User.qq = qq;
				User.password = password;
				
				// 启动聊天客户端
				new ChatClient(getApplicationContext());
				
				try {
					DataInputStream dis = new DataInputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getInputStream());
					if(dis.readUTF().equals("error")) { // 已有用户登录
						Toast.makeText(getApplicationContext(), "该用户已登录", 0).show();
					} else {
						Toast.makeText(getApplicationContext(), "登录成功", 0).show();
						
						// 请求暂存在服务器的消息
						com.hblg.lookingfellow.entity.Message msgChatMsg = new com.hblg.lookingfellow.entity.Message();
						msgChatMsg.setType(MessageType.MSG_REQUESTRETURNCHATMSG);
						msgChatMsg.setSender(User.qq);
						ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
						oos.writeObject(msgChatMsg);
						
						com.hblg.lookingfellow.entity.Message msgRequestAddFriend = new com.hblg.lookingfellow.entity.Message();
						msgRequestAddFriend.setType(MessageType.MSG_REQUESTRETURNADDFRIENDMSG);
						msgRequestAddFriend.setSender(User.qq);
						ObjectOutputStream oos2 = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
						oos2.writeObject(msgRequestAddFriend);
						
						ManageClientConnServer.getClientConServerThread(User.qq).start();
						
						//启动程序主窗体
						Intent intent = new Intent(getApplicationContext(), SlidingActivity.class);
						startActivity(intent);
						
						if(checkIsNotFirstLogin(qq)) {
							//如果已有用户登录过的话，就不启动服务来获取用户信息了
							System.out.println("already has one login");
						} else {
							//启动服务，获取用户用信息
							Intent service = new Intent(getApplicationContext(), GetUserInfoService.class);
							startService(service);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if(msg.arg1 == 2) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "用户名或密码错误", 0).show();
			} else if(msg.arg1 == 3) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接出现问题", 0).show();
			}
		}
		
	}
	
	public boolean checkIsNotFirstLogin(String qq) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		if(service.checkStuInfo(qq)) {
			return true; //表示已有用户登录过
		}
		return false; // 表示还没有用户登录过
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
				String path = Common.PATH + "UserLoginServlet";
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
						message.arg1 = 1; // 表示登录成功
						return;
					} else if(str.contains("2")) {
						message.arg1 = 2; // 表示用户名或密码错误
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
