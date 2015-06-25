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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.selfdefinedwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.selfdefinedwidget.MyGridView;
import com.hblg.lookingfellow.selfdefinedwidget.SendpostEditText;
import com.hblg.lookingfellow.slidingmenu.fragment.MainFragment;
import com.hblg.lookingfellow.tools.ExpressionUtil;
import com.hblg.lookingfellow.tools.Expressions;
import com.hblg.lookingfellow.tools.StreamTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class SendPostActivity extends Activity implements OnClickListener{

	
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
	
	private Button faceBtn;//添加表情
	private Button faceFoucsBtn;//当表情展开时
	
	
	Button goback;
	Button sendPost;
	EditText titleEditText;
	EditText contentEditText;
	
	MainFragment mainFragment;
	
	ProgressDialog dialog;
	Message msg = new Message();
	UIHandler handler = new UIHandler(this);
	private class UIHandler extends Handler {
		SendPostActivity activity;
		public UIHandler(SendPostActivity activity) {
			this.activity = activity;
		}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "发帖成功", 0).show();
				setResult(1);
				this.activity.finish();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "发帖失败", 0).show();
				setResult(2);
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接出现问题", 0).show();
				setResult(3);
				break;
			default:
				break;
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("SendPostActivity", this);
		mainFragment = (MainFragment) getIntent().getSerializableExtra("mainFragment");
		setContentView(R.layout.activity_sendpost);
		goback = (Button)this.findViewById(R.id.sendpost_goback);
		sendPost = (Button)this.findViewById(R.id.sendpost);
		titleEditText = (EditText)this.findViewById(R.id.sendpost_title);
		titleEditText.addTextChangedListener(new MaxLengthWatcher(30, titleEditText, this));
		contentEditText = (EditText)this.findViewById(R.id.sendpost_content);
		contentEditText.addTextChangedListener(new MaxLengthWatcher(600, contentEditText, this));
		goback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		sendPost.setOnClickListener(this);
		sendPost.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					if(check()) {
						dialog = ProgressDialog.show(SendPostActivity.this, "", "请稍等...", true);
						new SendPostThread().start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		initEmotionView();
	}
	private void initEmotionView(){
		faceBtn=(Button)findViewById(R.id.senpost_bottombar_emotionbutton);
		faceFoucsBtn=(Button)findViewById(R.id.senpost_bottombar_emotionbutton_focused);
		faceBtn.setOnClickListener(this);
		faceFoucsBtn.setOnClickListener(this);
		
		page_select = (LinearLayout) findViewById(R.id.page_select);
		page0 = (ImageView) findViewById(R.id.page0_select);
		page1 = (ImageView) findViewById(R.id.page1_select);
		page2 = (ImageView) findViewById(R.id.page2_select);
		
		// 引入表情
		expressionImages = Expressions.expressionImgs;
		expressionImageNames = Expressions.expressionImgNames;
		expressionImages1 = Expressions.expressionImgs1;
		expressionImageNames1 = Expressions.expressionImgNames1;
		expressionImages2 = Expressions.expressionImgs2;
		expressionImageNames2 = Expressions.expressionImgNames2;
		// 创建ViewPager
		viewPager = (ViewPager) findViewById(R.id.sendpost_viewpager);
		initViewPager();		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.senpost_bottombar_emotionbutton://点击展开图片
			faceBtn.setVisibility(faceBtn.GONE);
			faceFoucsBtn.setVisibility(faceBtn.VISIBLE);
			viewPager.setVisibility(viewPager.VISIBLE);
			page_select.setVisibility(page_select.VISIBLE);
	        
			
			break;
		case R.id.senpost_bottombar_emotionbutton_focused://点击关闭图片
			faceBtn.setVisibility(faceBtn.VISIBLE);
			faceFoucsBtn.setVisibility(faceBtn.GONE);
			viewPager.setVisibility(viewPager.GONE);
			page_select.setVisibility(page_select.GONE);
			break;
		}
		
	}
	private void initViewPager() {
		LayoutInflater inflater = LayoutInflater.from(this);
		grids = new ArrayList<GridView>();
		gView1 = (MyGridView) inflater.inflate(R.layout.gridview_face, null);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成24个表情
		for (int i = 0; i < 24; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", expressionImages[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(SendPostActivity.this, listItems,
				R.layout.griditem_face, new String[] { "image" },
				new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		gView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),expressionImages[position % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(SendPostActivity.this, bitmap);
				SpannableString spannableString = new SpannableString(
						expressionImageNames[position].substring(1,expressionImageNames[position].length() - 1));
				spannableString.setSpan(imageSpan, 0,
						expressionImageNames[position].length() - 2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 编辑框设置数据
				contentEditText.append(spannableString);
				System.out.println("edit的内容 = " + spannableString);
			}
		});
		grids.add(gView1);

		gView2 = (MyGridView) inflater.inflate(R.layout.gridview_face, null);
		grids.add(gView2);

		gView3 = (MyGridView) inflater.inflate(R.layout.gridview_face, null);
		grids.add(gView3);
		System.out.println("GridView的长度 = " + grids.size());

		// 填充ViewPager的数据适配器
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
	// ** 指引页面改监听器 */
	private	class GuidePageChangeListener implements OnPageChangeListener {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				System.out.println("页面滚动" + arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				System.out.println("换页了" + arg0);
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
					// 生成24个表情
					for (int i = 0; i < 24; i++) {
						Map<String, Object> listItem = new HashMap<String, Object>();
						listItem.put("image", expressionImages1[i]);
						listItems.add(listItem);
					}

					SimpleAdapter simpleAdapter = new SimpleAdapter(SendPostActivity.this,
							listItems, R.layout.griditem_face,
							new String[] { "image" }, new int[] { R.id.image });
					gView2.setAdapter(simpleAdapter);
					gView2.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Bitmap bitmap = null;
							bitmap = BitmapFactory.decodeResource(getResources(),
									expressionImages1[arg2
											% expressionImages1.length]);
							ImageSpan imageSpan = new ImageSpan(SendPostActivity.this, bitmap);
							SpannableString spannableString = new SpannableString(
									expressionImageNames1[arg2]
											.substring(1,
													expressionImageNames1[arg2]
															.length() - 1));
							spannableString.setSpan(imageSpan, 0,
									expressionImageNames1[arg2].length() - 2,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							// 编辑框设置数据
							contentEditText.append(spannableString);
							System.out.println("edit的内容 = " + spannableString);
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
					// 生成24个表情
					for (int i = 0; i < 24; i++) {
						Map<String, Object> listItem = new HashMap<String, Object>();
						listItem.put("image", expressionImages2[i]);
						listItems1.add(listItem);
					}

					SimpleAdapter simpleAdapter1 = new SimpleAdapter(SendPostActivity.this,
							listItems1, R.layout.griditem_face,
							new String[] { "image" }, new int[] { R.id.image });
					gView3.setAdapter(simpleAdapter1);
					gView3.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Bitmap bitmap = null;
							bitmap = BitmapFactory.decodeResource(getResources(),
									expressionImages2[arg2
											% expressionImages2.length]);
							ImageSpan imageSpan = new ImageSpan(SendPostActivity.this, bitmap);
							SpannableString spannableString = new SpannableString(
									expressionImageNames2[arg2]
											.substring(1,expressionImageNames2[arg2].length() - 1));
							spannableString.setSpan(imageSpan, 0,
									expressionImageNames2[arg2].length() - 2,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							// 编辑框设置数据
							contentEditText.append(spannableString);
							System.out.println("edit的内容 = " + spannableString);
						}
					});
					break;

				}
			}
	}
	protected boolean check() {
		String title = titleEditText.getText().toString();
		String details = contentEditText.getText().toString();
		if(title.equals("")) {
			Toast.makeText(getApplicationContext(), "标题不能为空", 0).show();
			return false;
		} else if(details.equals("")) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();
			return false;
		}
		return true;
	}
	private class SendPostThread extends Thread {
		@Override
		public void run() {
			try {
				sendPost();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected void sendPost() throws Exception {
		String title = titleEditText.getText().toString();
		String details = contentEditText.getText().toString();
		String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("qq", User.qq);
		params.put("title", title);
		params.put("details", details);
		params.put("time", time);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey() + "=");
			sb.append(URLEncoder.encode(entry.getValue(), "utf-8") + "&");
		}
		sb.deleteCharAt(sb.length()-1);
		byte[] data = sb.toString().getBytes();
		String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/PostsServlet";
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
				msg = handler.obtainMessage(1); // 1表示发帖成功
				msg.sendToTarget();
				return;
			} else {
				msg = handler.obtainMessage(2); // 2表示发帖失败
				msg.sendToTarget();
				return;
			}
		}
		msg = handler.obtainMessage(3); // 3表示网络连接出现问题
		msg.sendToTarget();
	}
	
}
