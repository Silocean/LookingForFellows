package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.PostDetailListViewAdapter;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.Post;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.selfdefinedwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.selfdefinedwidget.MyGridView;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.Expressions;
import com.hblg.lookingfellow.tools.StreamTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;

//����Ĺ��� 2013/9/27
public class PostDetailActivity extends Activity implements OnClickListener {

	// ����������ӵ�2013.10.18
	private Button faceBtn;// ��ӱ���
	private Button faceFoucsBtn;// ������չ��ʱ
	private Button commentBtn;
	private EditText commentEdt;
	private Button backBtn;
	private TextView titleTextView;

	private int[] expressionImages;
	private String[] expressionImageNames;
	private int[] expressionImages1;
	private String[] expressionImageNames1;
	private int[] expressionImages2;
	private String[] expressionImageNames2;
	private ViewPager viewPager;
	private ArrayList<GridView> grids;
	private MyGridView gView1;
	private MyGridView gView2;
	private MyGridView gView3;
	private LinearLayout page_select;
	private ImageView page0;
	private ImageView page1;
	private ImageView page2;

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
	
	private static final int REPLY_SUCCESS = 3;
	private static final int REPLY_FAILED = 4;
	private static final int NETWORK_ERROR = 5;

	/** �����״μ���ʱ��μ��� */
	private boolean isFlow = false;

	private PostDetailListViewAdapter listViewAdapter;
	private ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	Post post;
	
	int page = 0;

	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("PostDetailActivity", this);
		setContentView(R.layout.activity_postdetail);
		post = (Post)getIntent().getSerializableExtra("post");
		headView = this.getLayoutInflater().inflate(
				R.layout.posts_detail_header, null);
		
		refresh = getLayoutInflater().inflate(R.layout.posts_detail_footer,
				null);
		listview = (ListView) findViewById(R.id.post_detail_list);
		listview.addHeaderView(headView);
		listview.addFooterView(refresh);
		listViewAdapter = new PostDetailListViewAdapter(
				getApplicationContext(), listview);

		initView();
		
		initHeadView();

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

	/** ��ʼ���ؼ� */
	private void initView() {
		faceBtn = (Button) findViewById(R.id.post_detail_add_btn);
		faceFoucsBtn = (Button) findViewById(R.id.post_detail_add_btn_focused);
		commentBtn = (Button) findViewById(R.id.post_detail_comment);
		backBtn = (Button) findViewById(R.id.post_detail_back_btn);
		commentEdt = (EditText) findViewById(R.id.chat_bottombar_edittext);
		commentEdt.addTextChangedListener(new MaxLengthWatcher(600, commentEdt, this));
		titleTextView = (TextView) findViewById(R.id.postdetail_title);
		titleTextView.setText(post.getAuthorName());

		faceBtn.setOnClickListener(this);
		faceFoucsBtn.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		page_select = (LinearLayout) findViewById(R.id.page_select);
		page0 = (ImageView) findViewById(R.id.page0_select);
		page1 = (ImageView) findViewById(R.id.page1_select);
		page2 = (ImageView) findViewById(R.id.page2_select);

		// �������
		expressionImages = Expressions.expressionImgs;
		expressionImageNames = Expressions.expressionImgNames;
		expressionImages1 = Expressions.expressionImgs1;
		expressionImageNames1 = Expressions.expressionImgNames1;
		expressionImages2 = Expressions.expressionImgs2;
		expressionImageNames2 = Expressions.expressionImgNames2;
		// ����ViewPager
		viewPager = (ViewPager) findViewById(R.id.post_detail_viewpager);
		initViewPager();
	}

	private void initViewPager() {
		LayoutInflater inflater = LayoutInflater.from(this);
		grids = new ArrayList<GridView>();
		gView1 = (MyGridView) inflater.inflate(R.layout.gridview_face, null);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// ����24������
		for (int i = 0; i < 24; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", expressionImages[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(
				PostDetailActivity.this, listItems, R.layout.griditem_face,
				new String[] { "image" }, new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		gView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),
						expressionImages[position % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(PostDetailActivity.this,
						bitmap);
				SpannableString spannableString = new SpannableString(
						expressionImageNames[position].substring(1,
								expressionImageNames[position].length() - 1));
				spannableString.setSpan(imageSpan, 0,
						expressionImageNames[position].length() - 2,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// �༭����������
				commentEdt.append(spannableString);
				System.out.println("edit������ = " + spannableString);
			}
		});
		grids.add(gView1);

		gView2 = (MyGridView) inflater.inflate(R.layout.gridview_face, null);
		grids.add(gView2);

		gView3 = (MyGridView) inflater.inflate(R.layout.gridview_face, null);
		grids.add(gView3);
		System.out.println("GridView�ĳ��� = " + grids.size());

		// ���ViewPager������������
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return grids.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(grids.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(grids.get(position));
				return grids.get(position);
			}
		};

		viewPager.setAdapter(mPagerAdapter);
		// viewPager.setAdapter();

		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	// ** ָ��ҳ��ļ����� */
	private class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			System.out.println("ҳ�����" + arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			System.out.println("��ҳ��" + arg0);
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				break;
			case 1:
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				page2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
				// ����24������
				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", expressionImages1[i]);
					listItems.add(listItem);
				}

				SimpleAdapter simpleAdapter = new SimpleAdapter(
						PostDetailActivity.this, listItems,
						R.layout.griditem_face, new String[] { "image" },
						new int[] { R.id.image });
				gView2.setAdapter(simpleAdapter);
				gView2.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bitmap bitmap = null;
						bitmap = BitmapFactory.decodeResource(getResources(),
								expressionImages1[arg2
										% expressionImages1.length]);
						ImageSpan imageSpan = new ImageSpan(
								PostDetailActivity.this, bitmap);
						SpannableString spannableString = new SpannableString(
								expressionImageNames1[arg2]
										.substring(1,
												expressionImageNames1[arg2]
														.length() - 1));
						spannableString.setSpan(imageSpan, 0,
								expressionImageNames1[arg2].length() - 2,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						// �༭����������
						commentEdt.append(spannableString);
						System.out.println("edit������ = " + spannableString);
					}
				});
				break;
			case 2:
				page2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				List<Map<String, Object>> listItems1 = new ArrayList<Map<String, Object>>();
				// ����24������
				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", expressionImages2[i]);
					listItems1.add(listItem);
				}

				SimpleAdapter simpleAdapter1 = new SimpleAdapter(
						PostDetailActivity.this, listItems1,
						R.layout.griditem_face, new String[] { "image" },
						new int[] { R.id.image });
				gView3.setAdapter(simpleAdapter1);
				gView3.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bitmap bitmap = null;
						bitmap = BitmapFactory.decodeResource(getResources(),
								expressionImages2[arg2
										% expressionImages2.length]);
						ImageSpan imageSpan = new ImageSpan(
								PostDetailActivity.this, bitmap);
						SpannableString spannableString = new SpannableString(
								expressionImageNames2[arg2]
										.substring(1,
												expressionImageNames2[arg2]
														.length() - 1));
						spannableString.setSpan(imageSpan, 0,
								expressionImageNames2[arg2].length() - 2,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						// �༭����������
						commentEdt.append(spannableString);
						System.out.println("edit������ = " + spannableString);
					}
				});
				break;

			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.post_detail_add_btn:// ��ӣ�չ�����飩����
			faceBtn.setVisibility(faceBtn.GONE);
			faceFoucsBtn.setVisibility(faceBtn.VISIBLE);
			viewPager.setVisibility(viewPager.VISIBLE);
			page_select.setVisibility(page_select.VISIBLE);
			break;
		case R.id.post_detail_add_btn_focused:// �رձ���
			faceBtn.setVisibility(faceBtn.VISIBLE);
			faceFoucsBtn.setVisibility(faceBtn.GONE);
			viewPager.setVisibility(viewPager.GONE);
			page_select.setVisibility(page_select.GONE);
			break;
		case R.id.post_detail_comment:// ����
			
			String content = commentEdt.getText().toString().trim();

			if(check(content)) {
				dialog = ProgressDialog.show(PostDetailActivity.this, "", "���Ե�...", true);
				new ReplyThread(content).start();
				commentEdt.setText("");
				viewPager.setVisibility(ViewPager.GONE);
				page_select.setVisibility(page_select.GONE);
				faceBtn.setVisibility(faceBtn.VISIBLE);
				faceFoucsBtn.setVisibility(View.GONE);
			} else {
				Toast.makeText(getApplicationContext(), "�����������", 0).show();
			}
			break;
		case R.id.post_detail_back_btn:// ����
			finish();
			break;
		}

	}

	private class ReplyThread extends Thread {
		String content;
		public ReplyThread(String content) {
			this.content = content;
		}

		@Override
		public void run() {
			try {
				rely(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private boolean check(String content) {
		if(content.equals("")) {
			return false;
		}
		return true;
	}

	public void rely(String content) throws Exception {
		String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("authorId", User.qq);
		params.put("time", time);
		params.put("details", content);
		params.put("toPostId", post.getId()+"");
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey() + "=");
			sb.append(URLEncoder.encode(entry.getValue(), "utf-8") + "&");
		}
		sb.deleteCharAt(sb.length()-1);
		System.out.println("content:" + content);
		System.out.println(sb);
		byte[] data = sb.toString().getBytes();
		String path = Common.PATH + "ReplyServlet";
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		out.write(data);
		if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream in = conn.getInputStream();
			String str = new String(StreamTool.read(in));
			if(str.equals("success")) {
				Message msg = handler.obtainMessage(REPLY_SUCCESS); // 3��ʾ�����ɹ�
				msg.sendToTarget();
				// ���»ظ��б�
				updataList(User.qq, time, content);
				return;
			} else {
				Message msg = handler.obtainMessage(REPLY_FAILED); // 4��ʾ����ʧ��
				msg.sendToTarget();
				return;
			}
		}
		Message msg = handler.obtainMessage(NETWORK_ERROR); // 5��ʾ�������ӳ�������
		msg.sendToTarget();
	}

	private void updataList(String qq, String time, String content) {
		Student student = new SQLiteService(getApplicationContext()).getStuInfo(User.qq);
		ArrayList<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("authorId", qq);
		map.put("time", time);
		map.put("details", content);
		map.put("authorName", student.getName());
		temp.add(map);
		data.addAll(temp);
		listViewAdapter.setData(data);
		listview.setAdapter(listViewAdapter);
		listview.setSelection(listview.getCount()-1);
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
		TextView titleTextView = (TextView)headView.findViewById(R.id.postdetails_title);
		TextView timetTextView = (TextView)headView.findViewById(R.id.comment_time);
		TextView replycountTextView = (TextView)headView.findViewById(R.id.comment_count);
		titleTextView.setText(post.getTitle());
		timetTextView.setText(post.getTime());
		replycountTextView.setText(post.getReplyNum()+"");
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_FAILED:
				break;
			case WHAT_DID_LOAD_DATA:
				ArrayList<Map<String, Object>> tempData = getData(0);
				data.addAll(tempData);
				listViewAdapter.setData(data);
				listview.setAdapter(listViewAdapter);
				listview.removeFooterView(refresh);
				if(tempData.size()<5) {
					loadfinish = false;
				} else {
					isFlow = true;
					loadfinish = true;
				}
				break;
			case WHAT_DID_MORE:
				page += 1;
				ArrayList<Map<String, Object>> tempData2 = getData(page);
				System.out.println(page+"===="+tempData2);
				tempData2.remove(0); // ȥ����һ��¥��
				data.addAll(tempData2);
				listViewAdapter.setData(data);
				listview.setAdapter(listViewAdapter);
				listview.removeFooterView(refresh);
				listview.setSelection(listview.getCount()-1);
				if(tempData2.size()<=0) {
					loadfinish = false;
				} else {
					loadfinish = true;
				}
				break;
			case REPLY_SUCCESS:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "�ظ��ɹ�", 0).show();
				break;
			case REPLY_FAILED:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "�ظ�ʧ��", 0).show();
				break;
			case NETWORK_ERROR:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "�������ӳ�������", 0).show();
				break;
			}
		}
	};

	public ArrayList<Map<String, Object>> getData(int page) {
		ArrayList<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		Map<String, Object> louzhu = new HashMap<String, Object>();
		louzhu.put("authorId", post.getAuthorId());
		louzhu.put("details", post.getDetails());
		louzhu.put("time", post.getTime());
		louzhu.put("authorName", post.getAuthorName());
		temp.add(louzhu);
		try {
			String path = Common.PATH + "ReplyServlet?postId=" + post.getId() + "&page=" + page;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] data = StreamTool.read(in);
				String str = new String(data);
				System.out.println("�ظ����ݣ�"+str);
				if(str.equals("]")) {
					Toast.makeText(getApplicationContext(), "���޸���ظ�", 0).show();
				} else {
					JSONArray array = new JSONArray(str);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("replyId", obj.getInt("id"));
						map.put("details", obj.getString("details"));
						map.put("time", obj.getString("time"));
						map.put("authorId", obj.getString("authorId"));
						map.put("authorName", obj.getString("authorName"));
						map.put("toPostId", obj.getInt("toPostId"));
						temp.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

}
