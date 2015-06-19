package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaRecorder.OutputFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.selfdefinedwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.selfdefinedwidget.SendpostEditText;
import com.hblg.lookingfellow.slidingmenu.fragment.MainFragment;
import com.hblg.lookingfellow.tools.StreamTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;
import com.hblg.lookingfellow.user.User;

public class SendPostActivity extends Activity {
	Button goback;
	Button sendPost;
	EditText titleEditText;
	SendpostEditText contentEditText;
	
	MainFragment mainFragment;
	
	ProgressDialog dialog;
	Message msg = new Message();
	UIHandler handler = new UIHandler(this);
	private class UIHandler extends Handler {
		SendPostActivity activity;
		public UIHandler(SendPostActivity activity) {
			this.activity = activity;
		}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "发帖成功", 0).show();
				setResult(1);
				this.activity.finish();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "发帖失败", 0).show();
				setResult(2);
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接出现问题", 0).show();
				setResult(3);
				break;
			default:
				break;
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainFragment = (MainFragment) getIntent().getSerializableExtra("mainFragment");
		setContentView(R.layout.activity_sendpost);
		goback = (Button)this.findViewById(R.id.sendpost_goback);
		sendPost = (Button)this.findViewById(R.id.sendpost);
		titleEditText = (EditText)this.findViewById(R.id.sendpost_title);
		titleEditText.addTextChangedListener(new MaxLengthWatcher(30, titleEditText, this));
		contentEditText = (SendpostEditText)this.findViewById(R.id.sendpost_content);
		contentEditText.addTextChangedListener(new MaxLengthWatcher(600, contentEditText, this));
		goback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		sendPost.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					if(check()) {
						dialog = ProgressDialog.show(SendPostActivity.this, "", "请稍等...", true);
						new SendPostThread().start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	protected boolean check() {
		String title = titleEditText.getText().toString();
		String details = contentEditText.getText().toString();
		if(title.equals("")) {
			Toast.makeText(getApplicationContext(), "标题不能为空", 0).show();
			return false;
		} else if(details.equals("")) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();
			return false;
		}
		return true;
	}
	private class SendPostThread extends Thread {
		@Override
		public void run() {
			try {
				sendPost();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected void sendPost() throws Exception {
		String title = titleEditText.getText().toString();
		String details = contentEditText.getText().toString();
		String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("qq", User.qq);
		params.put("title", title);
		params.put("details", details);
		params.put("time", time);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey() + "=");
			sb.append(URLEncoder.encode(entry.getValue(), "utf-8") + "&");
		}
		sb.deleteCharAt(sb.length()-1);
		System.out.println(sb);
		byte[] data = sb.toString().getBytes();
		String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/PostsServlet";
		System.out.println(path);
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		out.write(data);
		if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream in = conn.getInputStream();
			String str = new String(StreamTool.read(in));
			if(str.equals("success")) {
				msg = handler.obtainMessage(1); // 1表示发帖成功
				msg.sendToTarget();
				return;
			} else {
				msg = handler.obtainMessage(2); // 2表示发帖失败
				msg.sendToTarget();
				return;
			}
		}
		msg = handler.obtainMessage(3); // 3表示网络连接出现问题
		msg.sendToTarget();
	}
	
}
