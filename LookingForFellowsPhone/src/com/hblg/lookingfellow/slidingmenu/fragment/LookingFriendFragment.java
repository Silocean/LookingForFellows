package com.hblg.lookingfellow.slidingmenu.fragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.SearchfriendsListViewAdapter;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.StreamTool;

public class LookingFriendFragment extends Fragment implements OnItemClickListener {
	LayoutInflater inflater;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	ListView listView;
	ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SearchfriendsListViewAdapter adapter;
	
	Bitmap bitmap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.main_content_lookingfriend, null);
		listView = (ListView)view.findViewById(R.id.searchFriendList);
		listView.setOnItemClickListener(this);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_friend_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_friend_rightmenu);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<Map<String, String>> tempData = this.getFriends();
		this.list = tempData;
		adapter = new SearchfriendsListViewAdapter(getActivity(), list, R.layout.listitem_searchfriendslayout, listView);
		adapter.setData(tempData);
		listView.setAdapter(adapter);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = this.list.get(position).get("friendQq");
		Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
		intent.putExtra("qq", qq);
		intent.putExtra("tag", "addRequest");
		startActivity(intent);
	}
	/**
	 * 获取用户好友列表
	 * @return
	 */
	private ArrayList<Map<String, String>> getFriends() {
		ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		try {
			SQLiteService service = new SQLiteService(getActivity());
			Student student = service.getStuInfo(User.qq);
			String province = student.getProvince();
			@SuppressWarnings("unused")
			String city = student.getCity();
			String path = Common.PATH + "FriendServlet?tag=searchfriends&province=";
			path = path + URLEncoder.encode(province, "utf-8") + "&qq=" + User.qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String str = new String(StreamTool.read(in));
				if(str.equals("]")) {
					Toast.makeText(getActivity(), "没有和你同省份的好友", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_default);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("friendQq", obj.getString("friendQq"));
						map.put("friendName", obj.getString("friendName"));
						map.put("friendPro", obj.getString("friendPro"));
						map.put("friendCity", obj.getString("friendCity"));
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
