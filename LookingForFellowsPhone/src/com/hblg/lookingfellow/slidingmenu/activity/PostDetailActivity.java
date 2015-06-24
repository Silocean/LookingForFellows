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

//����Ĺ��� 2013/9/27
public class PostDetailActivity extends Activity {
	private String TAG = "PostDetailActivity";
	ListView listview;
	boolean loadfinish = true;// �Ƿ�������

	/** �ײ�ˢ�²��� */
	View refresh;
	/** listViewͷ������ */
	View headView;

	/** Handler What����������� **/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/** Handler What����������� **/
	private static final int WHAT_DID_MORE = 1;
	/** Handler What��������ʧ�� **/
	private static final int WHAT_DID_FAILED = 2;

	/** ���ظ���ҳ�룬Ĭ��Ϊ�ڶ�ҳ����ˢ��ʱ����Ϊ2����һ�μ��ظ������*ʱ��1 */
	int page = 2;
	/** �����״μ���ʱ��μ��� */
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

		// ����
		findViewById(R.id.post_detail_back_btn).setOnClickListener(
				new OnClickListener() {
					public void onClick(View arg0) {
						finish();
					}
				});

		loadData();

		/*
		 * listview.setAdapter(adapter);
		 * listview.removeFooterView(refresh);//ɾ��ҳ�ţ���Ϊ����ֻ�����������ظ����ʱ��Ż���ҳ��
		 */
		listview.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// scrollState��3��ֵ
				// 0��ʾlistviewû�л���
				// 1��ʾlistview���ִ������л���
				// 2��ʾlistview�����ͷź����ڹ����ڻ���
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (isFlow == false) {
					return;
				}

				// firstVisibleItem��ʾ��ǰ��Ļ����ĵ�һ��item
				// visibleItemCount��ʾ��ǰ��Ļ������ʾ��item�ܺ�
				// totalItemCount��ʾ�ܹ���ʾ��item
				Log.v(TAG, firstVisibleItem + ">>>>" + visibleItemCount
						+ ">>>>" + totalItemCount + "");

				int lastitem = listview.getLastVisiblePosition();// �õ���ǰ����ʾ�����һ��item
				Log.v(TAG, lastitem + "");
				if ((lastitem + 1) == totalItemCount) {// �����ǰ����ʾ�����һ��item�������������ݵ����һ���Ϳ�ʼ����
					if (loadfinish) {// ����������
						loadfinish = false;
						listview.addFooterView(refresh);// �ڼ��ص�ʱ����ʾҳ��Ҳ���Ǽ���ҳ��
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

	/** ��ʼ��listviewͷ���ؼ� */
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
