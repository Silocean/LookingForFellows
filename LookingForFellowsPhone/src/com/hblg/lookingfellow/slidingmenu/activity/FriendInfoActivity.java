package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
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
	
	Button friendButton;
	
	String qq = null;
	String  tag = null;
	
	ObjectOutputStream oos = null;
	
	Friend friend;
	
	LayoutInflater inflater;
	PopupWindow popupWindow;
	View popupView;
	
	Button unfriendButton;
	Button cancelButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("FriendInfoActivity", this);
		setContentView(R.layout.activity_friendinfo);
		qq = getIntent().getStringExtra("qq");
		tag = getIntent().getStringExtra("tag");
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
		friendButton = (Button)this.findViewById(R.id.addfriend_button);
		initFriendInfoPopupWindow();
		unfriendButton = (Button) popupView.findViewById(R.id.friendinfo_popupwindow_unfriend);
		unfriendButton.setOnClickListener(this);
		cancelButton = (Button) popupView.findViewById(R.id.friendinfo_popupwindow_cancel);
		cancelButton.setOnClickListener(this);
		if(tag.equals("agreeRequest")) {
			friendButton.setText("ͬ�Ⲣ��ӶԷ�Ϊ����");
			friendButton.setVisibility(View.VISIBLE);
			titlebarRightmenu.setVisibility(View.GONE);
		} else if(tag.equals("addRequest")) {
			friendButton.setText("��Ϊ����");
			friendButton.setVisibility(View.VISIBLE);
			titlebarRightmenu.setVisibility(View.GONE);
		} else if(tag.equals("unfriendRequest")) {
			friendButton.setText("������ѹ�ϵ");
		}
		friendButton.setOnClickListener(this);
		titlebarLeftmenu.setOnClickListener(this);
		titlebarRightmenu.setOnClickListener(this);
		initFriendInfo(qq);
	}

	private void initFriendInfo(String qq) {
		try {
			String path = Common.PATH + "GetUserInfoServlet?qq=";
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
					signsTextView.setText("δ����");
				} else {
					signsTextView.setText(friend.getSigns());
				}
				if(friend.getPhone().equals("")) {
					mobileTextView.setText("δ����");
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
			this.dismissPopwindow(v);
			break;
		case R.id.addfriend_button:
			if(tag != null && tag.equals("agreeRequest")) { // ��ʾ��ͬ������������
				try {
					oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
					Message msg = new Message();
					msg.setType(MessageType.MSG_AGREEADDFRIEND);
					msg.setSender(User.qq);
					msg.setReceiver(qq);
					msg.setDetails("�����Ѿ��Ǻ��������������������");
					String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
					msg.setTime(time);
					oos.writeObject(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// ��Ӻ�����Ϣ������
				new SQLiteService(getApplicationContext()).addFriend(friend);
			} else if(tag != null && tag.equals("addRequest")) { // ��ʾ��������Ӻ�������
				try {
					oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
					Message msg = new Message();
					msg.setType(MessageType.MSG_REQUESTADDFRIEND);
					msg.setSender(User.qq);
					msg.setReceiver(qq);
					msg.setDetails("���磬�������Ѱɣ�");
					String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
					msg.setTime(time);
					oos.writeObject(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.friendinfo_popupwindow_unfriend:
			this.dismissPopwindow(v);
			try {
				oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
				Message msg = new Message();
				msg.setType(MessageType.MSG_UNFRINEDMSG);
				msg.setSender(User.qq);
				msg.setReceiver(qq);
				String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
				msg.setTime(time);
				oos.writeObject(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// �ӱ���ɾ��������Ϣ
			new SQLiteService(getApplicationContext()).deleteFriendInfo(qq);
			break;
		case R.id.friendinfo_popupwindow_cancel:
			this.dismissPopwindow(v);
			break;
 		default:
			break;
		}
	}
	private void dismissPopwindow(View v) {
		if(popupWindow.isShowing()) {
			// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		}
	}
	private void initFriendInfoPopupWindow() {
		inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.popupwindow_friendinfo, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
		// ���ô˲�����ý��㣬�����޷����
		popupWindow.setFocusable(true);  
		// ��Ҫ����һ�´˲����������߿���ʧ 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //���õ��������ߴ�����ʧ  
		popupWindow.setOutsideTouchable(true);
		// ���ô��ڶ���Ч��
		popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}

}
