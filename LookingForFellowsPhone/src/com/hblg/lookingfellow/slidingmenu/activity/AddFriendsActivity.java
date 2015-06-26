package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.SearchfriendsListViewAdapter;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView.OnPullDownListener;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetWorkHelper;
import com.hblg.lookingfellow.tools.StreamTool;

public class AddFriendsActivity extends Activity implements  OnPullDownListener,OnItemClickListener {
	Button gobackButton;
	private PullDownView mPullDownView;
	ListView listView;
	ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SearchfriendsListViewAdapter adapter;
	
	/**Handler What�����������**/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/**Handler What�����������**/
	private static final int WHAT_DID_REFRESH = 1;
	/**Handler What�����������**/
	private static final int WHAT_DID_MORE = 2;
	/**Handler What��������ʧ��**/
	private static final int WHAT_DID_FAILED=3;
	
	/**���ظ���ҳ�룬Ĭ��Ϊ�ڶ�ҳ����ˢ��ʱ����Ϊ2����һ�μ��ظ������*ʱ��1*/
	private int currentPage=2;
	
	Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("AddFriendsActivity", this);
		setContentView(R.layout.activity_addfriends);
		 mPullDownView = (PullDownView)findViewById(R.id.searchFriendList);
		 mPullDownView.setOnPullDownListener(this);
		 listView = mPullDownView.getListView();
		 listView.setOnItemClickListener(this);
		
		adapter = new SearchfriendsListViewAdapter(getApplicationContext(), list, R.layout.listitem_searchfriendslayout, listView);
		adapter.setData(list);
		listView.setAdapter(adapter);
		
		//������ʱ���������ݹ����У�����Ϊ���ɼ�
		listView.setVisibility(View.GONE);
		
		//���ÿ����Զ���ȡ���� �������һ���Զ���ȡ  �ĳ�false�������Զ���ȡ����
		mPullDownView.enableAutoFetchMore(false, 1);
		//���� ������β��
		mPullDownView.setHideFooter();
		//��ʾ�������Զ���ȡ����
		mPullDownView.setShowFooter();
		//���ز��ҽ���ͷ��ˢ��
		mPullDownView.setHideHeader();
		//��ʾ���ҿ���ʹ��ͷ��ˢ��
		mPullDownView.setShowHeader();
		
		gobackButton = (Button)this.findViewById(R.id.addfriends_goback_button);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		loadData();
	}
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
				Log.v("News","onRefresh");
				Message msg=new Message();
				ArrayList<Map<String, String>> newdatas =getFriends();
				if((null==newdatas)||newdatas.equals(null)){
					msg=mUIHandler.obtainMessage(WHAT_DID_FAILED);//��������ʧ��
				}else{
					msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
					msg.obj = newdatas;
				}
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
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.v("News","onMore");
				//��������ȡ�������  ������̰߳�ȫ�� �ɿ�Դ����
				mPullDownView.notifyDidMore();
				Message msg=new Message();
				 NameValuePair page_pair=new BasicNameValuePair("page",currentPage+"");		
				ArrayList<Map<String, String>> moredatas =getFriends(); 
				if((null==moredatas)||moredatas.equals(null)){
					msg=mUIHandler.obtainMessage(WHAT_DID_FAILED);//��������ʧ��
				}else{
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					msg.obj = moredatas;
				}
				msg.sendToTarget();
			}
		}).start();
		
	}
	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(0000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg=new Message();
				ArrayList<Map<String, String>> loaddatas = getFriends();
				if((null==loaddatas)||loaddatas.equals(null)){
					msg=mUIHandler.obtainMessage(WHAT_DID_FAILED);//��������ʧ��
				}else{
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					msg.obj = loaddatas;
				}
				msg.sendToTarget();
			}
		}).start();
	}
	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_FAILED:
				if(!(NetWorkHelper.isNetWorkConnection(getApplicationContext()))){
					Toast.makeText(getApplicationContext(),"",2000).show();
					return;
				}
				break;
			case WHAT_DID_LOAD_DATA: {
				if (msg.obj != null) {
					ArrayList<Map<String, String>> loaddatas = (ArrayList<Map<String, String>>) msg.obj;
					Log.v("response","data-->"+ loaddatas.toString());
					if (!(null==loaddatas)) {
						list.addAll(loaddatas);
						adapter.setData(list);
						currentPage=2;
					}
				}
				listView.setVisibility(View.VISIBLE);
				

				// �������ݼ������;
				break;
			}
			case WHAT_DID_REFRESH: {
				if (msg.obj != null) {
					ArrayList<Map<String, String>> newsdatas = (ArrayList<Map<String, String>>) msg.obj;
					if (!(null==newsdatas)) {
						list.clear();
						list.addAll(newsdatas);
						adapter.setData(list);
						currentPage=2;
					}
				}
				// �������������
				break;
			}
			case WHAT_DID_MORE: {
				if (msg.obj != null) {
					ArrayList<Map<String, String>> moredatas = (ArrayList<Map<String, String>>) msg.obj;
					if (!(null==moredatas)) {
						list.addAll(moredatas);
						adapter.setData(list);
						++currentPage;
					}
				}
				break;
			}
			}
		}
	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String qq = this.list.get(position).get("friendQq");
		Intent intent = new Intent(getApplicationContext(), FriendInfoActivity.class);
		intent.putExtra("qq", qq);
		intent.putExtra("tag", "addRequest");
		startActivity(intent);
	}
	/**
	 * ��ȡ�û������б�
	 * @return
	 */
	/*private ArrayList<Map<String, String>> getFriends() {
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
					Toast.makeText(getApplicationContext(), "û�к���ͬʡ�ݵĺ���", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_default);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
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
	}*/
	//��ʱ�����õ�
	private ArrayList<Map<String, String>> getFriends() {
		ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		for(int index=0;index<10;index++){
			Map<String,String>map=new HashMap<String, String>();
			tempList.add(map);
			map=null;
		}
		return tempList;
	}

}
