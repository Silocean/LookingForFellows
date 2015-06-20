package com.hblg.lookingfellow.slidingmenu.activity;

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
import com.hblg.lookingfellow.entity.*;
import com.hblg.lookingfellow.R;
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
				Toast.makeText(getApplicationContext(), "��¼�ɹ�", 0).show();
				
				//����qq�����뵽User���У��Ա�ʹ��
				String qq = qqEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();
				User user = new User();
				user.saveQqAndPassword(qq, password);
				
				// ��������ͻ���
				ChatClient cc = new ChatClient(getApplicationContext());
				
				// �����ݴ��ڷ���������Ϣ
				try {
					com.hblg.lookingfellow.entity.Message msgChatMsg = new com.hblg.lookingfellow.entity.Message();
					msgChatMsg.setType(MessageType.MSG_REQUESTCHATMSG);
					msgChatMsg.setSender(User.qq);
					ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
					oos.writeObject(msgChatMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//��������������
				Intent intent = new Intent(getApplicationContext(), SlidingActivity.class);
				startActivity(intent);
				
				if(checkIsNotFirstLogin(qq)) {
					//��������û���¼���Ļ����Ͳ�������������ȡ�û���Ϣ��
					System.out.println("already has one login");
				} else {
					//�������񣬻�ȡ�û�����Ϣ
					Intent service = new Intent(getApplicationContext(), GetUserInfoService.class);
					startService(service);
				}
				
			} else if(msg.arg1 == 2) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "�û������������", 0).show();
			} else if(msg.arg1 == 3) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "�������ӳ�������", 0).show();
			}
		}
		
	}
	
	public boolean checkIsNotFirstLogin(String qq) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		if(service.checkStuInfo(qq)) {
			return true; //��ʾ�����û���¼��
		}
		return false; // ��ʾ��û���û���¼��
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_goback_button:
			finish();
			break;
		case R.id.login_sure:
			if(check()) {
				dialog = ProgressDialog.show(LoginActivity.this, "", "���ڵ�¼�����Ե�...", true);
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
						message.arg1 = 1; // ��ʾ��¼�ɹ�
						return;
					} else if(str.contains("2")) {
						message.arg1 = 2; // ��ʾ�û������������
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			message.arg1 = 3; //��ʾ�������ӳ�������
		}
	}

	private boolean check() {
		String qq = qqEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if(qq.equals("")) {
			Toast.makeText(getApplicationContext(), "����дQQ����", 0).show();
			return false;
		} else if(password.equals("")) {
			Toast.makeText(getApplicationContext(), "����д����", 0).show();
			return false;
		}
		return true;
	}

}
