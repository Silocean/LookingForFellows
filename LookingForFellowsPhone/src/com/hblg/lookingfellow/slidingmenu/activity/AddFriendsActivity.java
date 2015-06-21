package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.SearchfriendsListViewAdapter;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.StreamTool;

public class AddFriendsActivity extends Activity {
	Button gobackButton;
	ListView listView;
	ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SearchfriendsListViewAdapter adapter;
	
	Bitmap bitmap;
	
	String imagePath = "http://192.168.1.152:8080/lookingfellowWeb0.2/head/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("AddFriendsActivity", this);
		setContentView(R.layout.activity_addfriends);
		listView = (ListView)this.findViewById(R.id.searchFriendList);
		ArrayList<Map<String, String>> tempData = this.getFriends();
		this.list = tempData;
		adapter = new SearchfriendsListViewAdapter(getApplicationContext(), list, R.layout.listitem_searchfriendslayout, listView);
		adapter.setData(tempData);
		listView.setAdapter(adapter);
		gobackButton = (Button)this.findViewById(R.id.addfriends_goback_button);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	/**
	 * 获取用户好友列表
	 * @return
	 */
	private ArrayList<Map<String, String>> getFriends() {
		ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		try {
			SQLiteService service = new SQLiteService(getApplicationContext());
			Student student = service.getStuInfo(User.qq);
			String hometown = student.getHometown();
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/FriendServlet?tag=searchfriends&hometown=";
			path = path + URLEncoder.encode(hometown, "utf-8") + "&qq=" + User.qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String str = new String(StreamTool.read(in));
				if(str.equals("]")) {
					Toast.makeText(getApplicationContext(), "没有和你同省份的好友", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_default);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("headimage", imagePath + "head_" + obj.get("friendQq") + ".jpg");
						map.put("friendQq", obj.getString("friendQq"));
						map.put("friendName", obj.getString("friendName"));
						map.put("friendHometown", obj.getString("friendHometown"));
						map.put("friendSex", obj.getString("friendSex"));
						map.put("friendSigns", obj.getString("friendSigns"));
						map.put("friendPhone", obj.getString("friendPhone"));
						tempList.add(map);
					}
					return tempList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempList;
	}

}
