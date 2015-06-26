package com.hblg.lookingfellow.slidingmenu.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.support.v4.app.FragmentActivity;
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
import com.hblg.lookingfellow.adapter.PostsListViewAdapter;
import com.hblg.lookingfellow.entity.Post;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView.OnPullDownListener;
import com.hblg.lookingfellow.slidingmenu.activity.PostDetailActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SendPostActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.DBOpenHelper;
import com.hblg.lookingfellow.tools.StreamTool;

public class MainFragment extends Fragment  implements OnPullDownListener, OnItemClickListener {
	/**����Ĳ���*/
	private View thisLayout;
	
	/**Fragment���ڵ�Activity*/
	private FragmentActivity fragmentActivity;
	
	private ImageView titlebarLeftmenu;
	private ImageView titlebarRightmenu;
	
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
    
	private Bitmap bitmap;
	
	private ListView mListView;
	private PullDownView mPullDownView;
	PostsListViewAdapter adapter;
	
	String imagePath = "http://192.168.1.152:8080/lookingfellowWeb0.2/head/";
	
	/**����*/
	private ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	
	private static int page = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisLayout=inflater.inflate(R.layout.main_content_posts, null);
		fragmentActivity=getActivity();
		return thisLayout;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("onActivityCreated");
	    mPullDownView = (PullDownView)thisLayout.findViewById(R.id.contentList);

		mPullDownView.setOnPullDownListener(this);
		
		mListView = mPullDownView.getListView();
		
		
		mListView.setOnItemClickListener(this);
		adapter=new PostsListViewAdapter(getActivity(), data, R.layout.listitem_postlayout, mListView);
		adapter.setData(data);
		mListView.setAdapter(adapter);
		//������ʱ���������ݹ����У�����Ϊ���ɼ�
		mListView.setVisibility(View.GONE);
		
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
		
		//��������  ����ʹ��
		loadData();
	   
	   titlebarLeftmenu=(ImageView)thisLayout.findViewById(R.id.main_titlebar_leftmenu);
	   titlebarLeftmenu.setOnClickListener(new OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ((SlidingActivity)fragmentActivity).showLeft();
		  }
	   });
	   titlebarRightmenu = (ImageView)thisLayout.findViewById(R.id.main_titlebar_rightmenu);
	   titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SendPostActivity.class);
				startActivityForResult(intent, 1); // 1��ʾ����activity�������������ɹ�
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1) {
			loadData();
		}
	}
	
	
	
	
	
	private ArrayList<Map<String, Object>> getData() {
		ArrayList<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for(int index=0;index<10;index++){
			Map<String,Object>map=new HashMap<String, Object>();
			tempList.add(map);
			map=null;
		}
		return tempList;
	}

	/*public ArrayList<Map<String,Object>> getData(int page) {
		ArrayList<Map<String ,Object>> tempList = new ArrayList<Map<String,Object>>(); 
		try {
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/PostsServlet?page=";
			path = path + page;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] data = StreamTool.read(in);
				String str = new String(data);
				if(str.equals("error")) {
					//Toast.makeText(getActivity(), "�������˳������⣬���Ժ�����", 0).show();
				} else if(str.equals("[")){
					Toast.makeText(getActivity(), "��û���˷���", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					//saveToCache(array); // ����������Ŀ���ݵ�����
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_default);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("headimage", imagePath + "head_" + obj.get("authorId") + ".jpg");
						map.put("authorId", obj.get("authorId"));
						map.put("title", obj.getString("title"));
						map.put("content", obj.getString("details"));
						map.put("replycount", obj.getString("replyNum"));
						map.put("publishname", obj.getString("authorName"));
						map.put("publishtime", obj.getString("time"));
						tempList.add(map);
					}
					return tempList;
				}
			}
			//Toast.makeText(getActivity(), "�������ӳ�������", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempList;
	}*/

	/**ˢ���¼��ӿ�  ����Ҫע����ǻ�ȡ������ Ҫ�ر� ˢ�µĽ�����RefreshComplete()**/
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

				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.obj = "After refresh " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();

	}

	/**ˢ���¼��ӿ�  ����Ҫע����ǻ�ȡ������ Ҫ�ر� ����Ľ����� notifyDidMore()**/
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
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = "After more " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();
	}

	public Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_DID_LOAD_DATA: {
					data.clear();
					ArrayList<Map<String, Object>> tempData = getData();
					if(tempData == null) {
						Toast.makeText(getActivity(), "��ȡ����ʧ��", 0).show();
					} else {
						data.addAll(tempData);
						adapter.setData(data);
						mListView.setVisibility(View.VISIBLE);
						// �������ݼ������;
						page = 0;
					}
					break;
				}
				case WHAT_DID_REFRESH: {
					data.clear();
					ArrayList<Map<String, Object>> tempData = getData();
					if(tempData == null) {
						Toast.makeText(getActivity(), "��ȡ����ʧ��", 0).show();
					} else {
						data.addAll(tempData);
						adapter.setData(data);
						// �������������
						page = 0;
					}
					break;
				}
				case WHAT_DID_MORE: {
					page += 1;
					System.out.println("��ǰҳ����"+page);
					ArrayList<Map<String, Object>> tempData = getData();
					if(tempData == null) {
						Toast.makeText(getActivity(), "��ȡ����ʧ��", 0).show();
					} else {
						data.addAll(tempData);
						adapter.setData(data);
					}
					break;
				}
			}
		}

	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), PostDetailActivity.class);
		
		startActivity(intent);
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
				msg.what=WHAT_DID_LOAD_DATA;
				mUIHandler.sendMessage(msg);
			}
		}).start();
	}
}