package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.StreamTool;
import com.hblg.lookingfellow.user.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendInfoActivity extends Activity {
	
	Button titlebarLeftmenu;
	Button titlebarRightmenu;
	
	ImageView headImage;
	
	Button hometownButton;
	Button signsButton;
	Button mobileButton;
	
	TextView titleTextView;
	TextView hometowntTextView;
	TextView signsTextView;
	TextView mobileTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String qq = getIntent().getStringExtra("authorId");
		setContentView(R.layout.activity_friendinfo);
		titlebarLeftmenu = (Button)this.findViewById(R.id.main_titlebar_goback_leftmenu);
		titlebarRightmenu = (Button)this.findViewById(R.id.main_titlebar_gomore_rightmenu);
		headImage = (ImageView)this.findViewById(R.id.friendinfo_headimage);
		hometownButton = (Button)this.findViewById(R.id.friendinfo_hometown_button);
		signsButton = (Button)this.findViewById(R.id.friendinfo_signs_button);
		mobileButton = (Button)this.findViewById(R.id.friendinfo_mobile_button);
		titleTextView = (TextView)this.findViewById(R.id.titlebar_title);
		hometowntTextView = (TextView)this.findViewById(R.id.hometown);
		signsTextView = (TextView)this.findViewById(R.id.signs);
		mobileTextView = (TextView)this.findViewById(R.id.mobile);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getApplicationContext(), "moreInfo", 0).show();
			}
		});
		initFriendInfo(qq);
	}

	private void initFriendInfo(String qq) {
		try {
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/GetUserInfoServlet?qq=";
			path = path + qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] data = StreamTool.read(in);
				String json = new String(data, "utf-8");
				Student student = parseJson(json);
				titleTextView.setText(student.getName());
				hometowntTextView.setText(student.getHometown());
				if(student.getSigns().equals("")) {
					signsTextView.setText("Œ¥…Ë÷√");
				} else {
					signsTextView.setText(student.getSigns());
				}
				if(student.getPhone().equals("")) {
					mobileTextView.setText("Œ¥…Ë÷√");
				} else {
					mobileTextView.setText(student.getPhone());
				}
				Bitmap bitmap = ImageTool.getHeadImageFromLocalOrNet(getApplicationContext(), qq);
				Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
				headImage.setImageBitmap(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Student parseJson(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		Student student = new Student();
		student.setQq(obj.getString("stuQQ"));
		student.setName(obj.getString("stuName"));
		student.setPassword(obj.getString("stuPassword"));
		student.setHometown(obj.getString("stuHometown"));
		student.setSex(obj.getString("stuSex"));
		student.setSigns(obj.getString("stuSigns"));
		student.setPhone(obj.getString("stuPhone"));
		return student;
	}
	

}
