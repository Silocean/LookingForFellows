package com.hblg.lookingfellow.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView.OnPullDownListener;
import com.hblg.lookingfellow.slidingmenu.activity.AddFriendsActivity;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.tools.NetWorkHelper;

public class FriendsFragment extends Fragment implements  OnPullDownListener, OnItemClickListener {
	private PullDownView mPullDownView;
	private ListView listView;
	ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private friendsListViewAdapter adapter;
	private ImageView titlebarLeftmenu;
	private ImageView titlebarRightmenu;
	
	private Bitmap bitmap;
	
	private String qq = null;
	
	private View thisLayout;
	private FragmentActivity fragmentActivity;
	
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
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisLayout = inflater.inflate(R.layout.main_content_friends, null);
		fragmentActivity=getActivity();
		titlebarLeftmenu = (ImageView)thisLayout.findViewById(R.id.main_titlebar_friend_leftmenu);
		titlebarRightmenu = (ImageView)thisLayout.findViewById(R.id.main_titlebar_friend_rightmenu);
		return thisLayout;
	}
	@Override
	public void onResume() {
		super.onResume();
		ArrayList<Map<String, String>> data = this.getFriends();
		this.data = data;
		adapter.setData(data);
		listView.setAdapter(adapter);
		System.out.println(data);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	    mPullDownView = (PullDownView)thisLayout.findViewById(R.id.friendList);
	    mPullDownView.setOnPullDownListener(this);
	    listView = mPullDownView.getListView();
	    listView.setOnItemClickListener(this);
		adapter = new friendsListViewAdapter(getActivity(), data, R.layout.listitem_friendslayout, listView);
		adapter.setData(data);
		listView.setAdapter(adapter);
		
		//当进入时，加载数据过程中，设置为不可见
		listView.setVisibility(View.GONE);
		
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
				
		
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) fragmentActivity).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(fragmentActivity, AddFriendsActivity.class);
				startActivity(intent);
			}
		});
		loadData();
	}
	
	private ArrayList<Map<String, String>> getFriends() {
		/*ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		SQLiteService service = new SQLiteService(getActivity());
		tempList = service.getAllFriendsInfo(User.qq);
		if(tempList.size() == 0) {
			Toast.makeText(getActivity(), "你还没有好友啊", 0).show();
		}
		System.out.println(tempList);*/
		ArrayList<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		for(int index=0;index<10;index++){
			Map<String,String>map=new HashMap<String, String>();
			tempList.add(map);
			map=null;
		}
		
		return tempList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//临时测试
	/*	String qq = this.data.get(position).get("friQQ");
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("friendQq", qq);
		startActivity(intent);*/
		
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
				
				/** 关闭 刷新完毕 ***/
				mPullDownView.RefreshComplete();//这个事线程安全的 可看源代码
				Log.v("News","onRefresh");
				Message msg=new Message();
				ArrayList<Map<String, String>> newdatas =getFriends();
				if((null==newdatas)||newdatas.equals(null)){
					msg=mUIHandler.obtainMessage(WHAT_DID_FAILED);//加载数据失败
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
				//告诉它获取更多完毕  这个事线程安全的 可看源代码
				mPullDownView.notifyDidMore();
				Message msg=new Message();
				 NameValuePair page_pair=new BasicNameValuePair("page",currentPage+"");		
				ArrayList<Map<String, String>> moredatas =getFriends(); 
				if((null==moredatas)||moredatas.equals(null)){
					msg=mUIHandler.obtainMessage(WHAT_DID_FAILED);//加载数据失败
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
					msg=mUIHandler.obtainMessage(WHAT_DID_FAILED);//加载数据失败
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
				if(!(NetWorkHelper.isNetWorkConnection(fragmentActivity))){
					Toast.makeText(fragmentActivity,"",2000).show();
					return;
				}
				break;
			case WHAT_DID_LOAD_DATA: {
				if (msg.obj != null) {
					ArrayList<Map<String, String>> loaddatas = (ArrayList<Map<String, String>>) msg.obj;
					Log.v("response","data-->"+ loaddatas.toString());
					if (!(null==loaddatas)) {
						data.addAll(loaddatas);
						adapter.setData(data);
						currentPage=2;
					}
				}
				listView.setVisibility(View.VISIBLE);

				// 诉它数据加载完毕;
				break;
			}
			case WHAT_DID_REFRESH: {
				if (msg.obj != null) {
					ArrayList<Map<String, String>> newsdatas = (ArrayList<Map<String, String>>) msg.obj;
					if (!(null==newsdatas)) {
						data.clear();
						data.addAll(newsdatas);
						adapter.setData(data);
						currentPage=2;
					}
				}
				// 告诉它更新完毕
				break;
			}
			case WHAT_DID_MORE: {
				if (msg.obj != null) {
					ArrayList<Map<String, String>> moredatas = (ArrayList<Map<String, String>>) msg.obj;
					if (!(null==moredatas)) {
						data.addAll(moredatas);
						adapter.setData(data);
						++currentPage;
					}
				}
				break;
			}
			}
		}
	};
}