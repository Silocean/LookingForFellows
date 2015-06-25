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
import android.os.Handler;
import android.os.Message;
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
import com.hblg.lookingfellow.adapter.friendsListViewAdapter;
import com.hblg.lookingfellow.entity.Friend;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView.OnPullDownListener;
import com.hblg.lookingfellow.slidingmenu.activity.AddFriendsActivity;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.StreamTool;

public class FriendsFragment extends Fragment implements OnItemClickListener, OnPullDownListener {
	ListView listView;
	static ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private friendsListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	String qq = null;
	
	/**Handler What�����������**/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/**Handler What�����������**/
	private static final int WHAT_DID_REFRESH = 1;
	/**Handler What�����������**/
	private static final int WHAT_DID_MORE = 2;
	/**Handler What��������ʧ��**/
	private static final int WHAT_DID_FAILED=3;
	
	private PullDownView mPullDownView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_friends, null);
		mPullDownView = (PullDownView)view.findViewById(R.id.friendList);
		listView = mPullDownView.getListView();
		mPullDownView.setOnPullDownListener(this);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_friend_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_friend_rightmenu);
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		ArrayList<Map<String, String>> data = this.getFriends();
		FriendsFragment.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
		System.out.println(data);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new friendsListViewAdapter(getActivity(), data, R.layout.listitem_friendslayout, listView);
		ArrayList<Map<String, String>> data = this.getFriends();
		FriendsFragment.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		//���ÿ����Զ���ȡ���� �������һ���Զ���ȡ  �ĳ�false�������Զ���ȡ����
		mPullDownView.enableAutoFetchMore(false, 1);
		//��ʾ���ҿ���ʹ��ͷ��ˢ��
		mPullDownView.setShowHeader();
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private ArrayList<Map<String, String>> getFriends() {
		ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		SQLiteService service = new SQLiteService(getActivity());
		tempList = service.getAllFriendsInfo(User.qq);
		if(tempList.size() == 0) {
			Toast.makeText(getActivity(), "�㻹û�к��Ѱ�", 0).show();
		}
		return tempList;
	}
	/**
	 * �ӷ�����ȡ���û����к�����Ϣ�����浽����
	 */
	private void getFriendsFromNet() {
		try {
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/FriendServlet?tag=myfriends";
			path = path + "&qq=" + User.qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String str = new String(StreamTool.read(in));
				if(str.equals("]")) {
					Toast.makeText(getActivity(), "�㻹û�к��Ѱ�", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					// ��ɾ���������к��Ѽ�¼
					new SQLiteService(getActivity()).deleteAllFriendInfo();
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Friend friend = new Friend();
						friend.setQq(obj.getString("friendQq"));
						friend.setName(obj.getString("friendName"));
						friend.setHometown(obj.getString("friendHometown"));
						friend.setSex(obj.getString("friendSex"));
						friend.setSigns(obj.getString("friendSigns"));
						friend.setPhone(obj.getString("friendPhone"));
						new SQLiteService(getActivity()).addFriend(friend);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = FriendsFragment.data.get(position-1).get("friQQ"); // ע�������positionҪ-1
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("friendQq", qq);
		startActivity(intent);
	}
	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/** �ر� ˢ����� ***/
				mPullDownView.RefreshComplete();//������̰߳�ȫ�� �ɿ�Դ����

				Message msg = handler.obtainMessage(WHAT_DID_REFRESH);
				msg.obj = "After refresh " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();
	}
	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//��������ȡ�������  ������̰߳�ȫ�� �ɿ�Դ����
				mPullDownView.notifyDidMore();
				Message msg = handler.obtainMessage(WHAT_DID_MORE);
				msg.obj = "After more " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();
	}
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_REFRESH:
				getFriendsFromNet();
				ArrayList<Map<String, String>> data = getFriends();
				FriendsFragment.data = data;
				System.out.println(FriendsFragment.data);
				adapter.setData(data);
				listView.setAdapter(adapter);
				break;
			case WHAT_DID_MORE:
				
				break;
			default:
				break;
			}
		}
		
	};
}
