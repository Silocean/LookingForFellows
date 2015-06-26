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
	/**本类的布局*/
	private View thisLayout;
	
	/**Fragment所在的Activity*/
	private FragmentActivity fragmentActivity;
	
	private ImageView titlebarLeftmenu;
	private ImageView titlebarRightmenu;
	
	/**Handler What加载数据完毕**/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/**Handler What更新数据完毕**/
	private static final int WHAT_DID_REFRESH = 1;
	/**Handler What更多数据完毕**/
	private static final int WHAT_DID_MORE = 2;
	/**Handler What加载数据失败**/
	private static final int WHAT_DID_FAILED=3;
	
	/**加载更多页码，默认为第二页，当刷新时重置为2，当一次加载更多完成*时加1*/
	private int currentPage=2;
    
	private Bitmap bitmap;
	
	private ListView mListView;
	private PullDownView mPullDownView;
	PostsListViewAdapter adapter;
	
	String imagePath = "http://192.168.1.152:8080/lookingfellowWeb0.2/head/";
	
	/**数据*/
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
		//当进入时，加载数据过程中，设置为不可见
		mListView.setVisibility(View.GONE);
		
		//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(false, 1);
		//隐藏 并禁用尾部
		mPullDownView.setHideFooter();
		//显示并启用自动获取更多
		mPullDownView.setShowFooter();
		//隐藏并且禁用头部刷新
		mPullDownView.setHideHeader();
		//显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		
		//加载数据  本类使用
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
				startActivityForResult(intent, 1); // 1表示发帖activity撤销，并发帖成功
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
					//Toast.makeText(getActivity(), "服务器端出现问题，请稍后再试", 0).show();
				} else if(str.equals("[")){
					Toast.makeText(getActivity(), "暂没有人发帖", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					//saveToCache(array); // 保存帖子条目数据到缓存
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
			//Toast.makeText(getActivity(), "网络连接出现问题", 0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempList;
	}*/

	/**刷新事件接口  这里要注意的是获取更多完 要关闭 刷新的进度条RefreshComplete()**/
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
				
				/** 关闭 刷新完毕 ***/
				mPullDownView.RefreshComplete();//这个事线程安全的 可看源代码

				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.obj = "After refresh " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();

	}

	/**刷新事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
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
				
				//告诉它获取更多完毕  这个事线程安全的 可看源代码
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
						Toast.makeText(getActivity(), "获取数据失败", 0).show();
					} else {
						data.addAll(tempData);
						adapter.setData(data);
						mListView.setVisibility(View.VISIBLE);
						// 诉它数据加载完毕;
						page = 0;
					}
					break;
				}
				case WHAT_DID_REFRESH: {
					data.clear();
					ArrayList<Map<String, Object>> tempData = getData();
					if(tempData == null) {
						Toast.makeText(getActivity(), "获取数据失败", 0).show();
					} else {
						data.addAll(tempData);
						adapter.setData(data);
						// 告诉它更新完毕
						page = 0;
					}
					break;
				}
				case WHAT_DID_MORE: {
					page += 1;
					System.out.println("当前页数："+page);
					ArrayList<Map<String, Object>> tempData = getData();
					if(tempData == null) {
						Toast.makeText(getActivity(), "获取数据失败", 0).show();
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