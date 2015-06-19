/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hblg.lookingfellow.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.PostsListViewAdapter;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView;
import com.hblg.lookingfellow.selfdefinedwidget.PullDownView.OnPullDownListener;
import com.hblg.lookingfellow.slidingmenu.activity.PostDetailActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SendPostActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.DBOpenHelper;

public class MainFragment extends Fragment  implements OnPullDownListener,OnItemClickListener {
	/*ListView listView;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private PostsListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_posts, null);
		listView = (ListView)view.findViewById(R.id.contentList);
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_rightmenu);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		for(int i=0; i<10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("headimage", bitmap);
			map.put("title", "欢迎加入湖北老乡群");
			map.put("content", "在这里你可以找到你所在高校中的老乡，你可以在这里发帖召集失散已久的老乡，还可以在侧边栏里搜索你的好友们!");
			map.put("replaycount", 35+"");
			map.put("publishname", "linxiaonan");
			map.put("publishtime", "10:17");
			list.add(map);
		}
		adapter = new PostsListViewAdapter(getActivity(), list, R.layout.listitem_postlayout);
		
		listView.setAdapter(adapter);
		
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SendPostActivity.class);
				startActivity(intent);
				DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity());
				dbOpenHelper.getWritableDatabase();
			}
		});
	}*/
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
	/**数据*/
	private ArrayList<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisLayout=inflater.inflate(R.layout.main_content_posts, null);
		fragmentActivity=getActivity();
	    bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		return thisLayout;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		    mPullDownView = (PullDownView)thisLayout.findViewById(R.id.contentList);

			mPullDownView.setOnPullDownListener(this);
			
			mListView = mPullDownView.getListView();
			
			
			mListView.setOnItemClickListener(this);
			adapter=new PostsListViewAdapter(getActivity(), data, R.layout.listitem_postlayout,mListView);
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
					startActivity(intent);
					DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity());
					dbOpenHelper.getWritableDatabase();
				}
			});
		
	}
	public ArrayList<Map<String,Object>> getData() {
		ArrayList<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
		for(int i=0; i<10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("headimage", "http://g.hiphotos.baidu.com/album/w%3D230/sign=a94d197c8435e5dd902ca2dc46c7a7f5/838ba61ea8d3fd1fa7bb5b14314e251f95ca5f6a.jpg");
			map.put("title", "欢迎加入湖北老乡群");
			map.put("content", "在这里你可以找到你所在高校中的老乡，你可以在这里发帖召集失散已久的老乡，还可以在侧边栏里搜索你的好友们!");
			map.put("replaycount", 35+"");
			map.put("publishname", "linxiaonan");
			map.put("publishtime", "10:17");
			tempList.add(map);
			map=null;
		}
		return tempList;
	}

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

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				data.clear();
				data.addAll(getData());
				adapter.setData(data);
				mListView.setVisibility(View.VISIBLE);
				// 诉它数据加载完毕;
				break;
			}
			case WHAT_DID_REFRESH: {
				data.clear();
				data.addAll(getData());
				adapter.setData(data);
				// 告诉它更新完毕
				break;
			}

			case WHAT_DID_MORE: {
				data.addAll(getData());
				adapter.setData(data);
				break;
			}
			}

		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startActivity(new Intent(fragmentActivity, PostDetailActivity.class));
		
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