package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Friend;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.model.ManageClientConnServer;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.StreamTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class FriendInfoActivity extends Activity implements OnClickListener {
	
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
	
	Button addfriendButton;
	
	String qq = null;
	String  tag = null;
	
	ObjectOutputStream oos = null;
	
	Friend friend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("FriendInfoActivity", this);
		qq = getIntent().getStringExtra("qq");
		tag = getIntent().getStringExtra("tag");
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
		addfriendButton = (Button)this.findViewById(R.id.addfriend_button);
		addfriendButton.setOnClickListener(this);
		titlebarLeftmenu.setOnClickListener(this);
		titlebarRightmenu.setOnClickListener(this);
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
				friend = parseJson(json);
				titleTextView.setText(friend.getName());
				hometowntTextView.setText(friend.getHometown());
				if(friend.getSigns().equals("")) {
					signsTextView.setText("未设置");
				} else {
					signsTextView.setText(friend.getSigns());
				}
				if(friend.getPhone().equals("")) {
					mobileTextView.setText("未设置");
				} else {
					mobileTextView.setText(friend.getPhone());
				}
				Bitmap bitmap = ImageTool.getHeadImageFromLocalOrNet(getApplicationContext(), qq);
				Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
				headImage.setImageBitmap(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Friend parseJson(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		Friend friend = new Friend();
		friend.setQq(obj.getString("stuQQ"));
		friend.setName(obj.getString("stuName"));
		friend.setHometown(obj.getString("stuHometown"));
		friend.setSex(obj.getString("stuSex"));
		friend.setSigns(obj.getString("stuSigns"));
		friend.setPhone(obj.getString("stuPhone"));
		return friend;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_titlebar_goback_leftmenu:
			finish();
			break;
		case R.id.main_titlebar_gomore_rightmenu:
			//TODO
			Toast.makeText(getApplicationContext(), "moreInfo", 0).show();
		case R.id.addfriend_button:
			if(tag != null && tag.equals("agreeRequest")) { // 表示是同意好友添加请求
				try {
					oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
					Message msg = new Message();
					msg.setType(MessageType.MSG_AGREEADDFRIEND);
					msg.setSender(User.qq);
					msg.setReceiver(qq);
					msg.setDetails("我们已经是好友啦，现在找我聊天吧");
					String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
					msg.setTime(time);
					oos.writeObject(msg);
					System.out.println("send agree add friend message");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 添加好友信息到本地
				new SQLiteService(getApplicationContext()).addFriend(friend);
			}
		default:
			break;
		}
	}
	

}
