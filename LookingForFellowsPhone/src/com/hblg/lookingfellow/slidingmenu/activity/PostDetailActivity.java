package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.PostDetailListViewAdapter;
import com.hblg.lookingfellow.model.ManageActivity;

//具体的公告 2013/9/27
public class PostDetailActivity extends Activity {
	private String TAG = "PostDetailActivity";
	ListView listview;
	boolean loadfinish = true;// 是否加载完毕

	/** 底部刷新布局 */
	View refresh;
	/** listView头部布局 */
	View headView;

	/** Handler What加载数据完毕 **/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/** Handler What更多数据完毕 **/
	private static final int WHAT_DID_MORE = 1;
	/** Handler What加载数据失败 **/
	private static final int WHAT_DID_FAILED = 2;

	/** 加载更多页码，默认为第二页，当刷新时重置为2，当一次加载更多完成*时加1 */
	int page = 2;
	/** 避免首次加载时多次加载 */
	private boolean isFlow = false;

	private PostDetailListViewAdapter listViewAdapter;
	private ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("PostDetailActivity", this);
		setContentView(R.layout.activity_postdetail);

		headView = this.getLayoutInflater().inflate(
				R.layout.posts_detail_header, null);
		refresh = getLayoutInflater().inflate(R.layout.posts_detail_footer,
				null);
		listview = (ListView) findViewById(R.id.post_detail_list);
		listview.addHeaderView(headView);
		listview.addFooterView(refresh);
		listViewAdapter = new PostDetailListViewAdapter(
				getApplicationContext(), listview);

		initHeadView();

		// 返回
		findViewById(R.id.post_detail_back_btn).setOnClickListener(
				new OnClickListener() {
					public void onClick(View arg0) {
						finish();
					}
				});

		loadData();

		/*
		 * listview.setAdapter(adapter);
		 * listview.removeFooterView(refresh);//删除页脚；因为我们只有在下拉加载更多的时候才会有页脚
		 */
		listview.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// scrollState有3种值
				// 0表示listview没有滑动
				// 1表示listview在手带动下有滑动
				// 2表示listview在手释放后由于惯性在滑动
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (isFlow == false) {
					return;
				}

				// firstVisibleItem表示当前屏幕上面的第一行item
				// visibleItemCount表示当前屏幕上面显示的item总和
				// totalItemCount表示总共显示的item
				Log.v(TAG, firstVisibleItem + ">>>>" + visibleItemCount
						+ ">>>>" + totalItemCount + "");

				int lastitem = listview.getLastVisiblePosition();// 得到当前屏显示的最后一个item
				Log.v(TAG, lastitem + "");
				if ((lastitem + 1) == totalItemCount) {// 如果当前屏显示的最后一个item是我们所有数据的最后一个就开始加载
					if (loadfinish) {// 如果加载完毕
						loadfinish = false;
						listview.addFooterView(refresh);// 在加载的时候显示页脚也就是加载页面
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								Message msg = new Message();
								msg.what = WHAT_DID_MORE;
								handler.sendMessage(msg);
							}
						}).start();
					}
				}
			}
		});

	}

	private void loadData() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = WHAT_DID_LOAD_DATA;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/** 初始化listview头部控件 */
	private void initHeadView() {

		listViewAdapter.setData(data);
		listview.setAdapter(listViewAdapter);
		listview.removeFooterView(refresh);

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_FAILED:
				break;
			case WHAT_DID_LOAD_DATA:
				data = getData();
				listViewAdapter.setData(data);
				listview.setAdapter(listViewAdapter);
				listview.removeFooterView(refresh);
				isFlow = true;
				loadfinish = true;
				break;
			case WHAT_DID_MORE:
				data.addAll(getData());
				listview.removeFooterView(refresh);
				loadfinish = true;
				break;
			}
		}
	};

	public ArrayList<Map<String, Object>> getData() {
		System.out.println("lalalalal");
		ArrayList<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			//
			temp.add(map);
			map = null;
		}
		return temp;
	}

}
