package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.ChatListViewAdapter;
import com.hblg.lookingfellow.customwidget.MaxLengthWatcher;
import com.hblg.lookingfellow.customwidget.MyGridView;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.model.ManageClientConnServer;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.Expressions;
import com.hblg.lookingfellow.tools.MySharePreferences;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class ChatActivity extends Activity implements OnClickListener{
	
	private int photoIds[]=new int[]{
			R.drawable.bg_default,
			R.drawable.bg_01,R.drawable.bg_02,R.drawable.bg_03,
			R.drawable.bg_04,R.drawable.bg_05,R.drawable.bg_06,
			R.drawable.bg_07,R.drawable.bg_08,R.drawable.bg_09,
			R.drawable.bg_10,R.drawable.bg_11,R.drawable.bg_12,
			R.drawable.bg_13,R.drawable.bg_14
	};
	
	private Button faceBtn;//添加表情
	private Button faceFoucsBtn;//当表情展开时
	
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
	
	private RelativeLayout bgLayout;
	
	
	Button gobackButton;
	Button personinfoButton;
	
	TextView titleTextView;
	Button sendButton;
	EditText contentEditText;
	
	static ListView listView;
	static ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	static ChatListViewAdapter adapter;
	
	public static String friendQq; // 聊天对象qq号码
	
	ObjectOutputStream oos = null;
	
	public static boolean active = false; // 该activity是不是处于最顶端
	
	private InputMethodManager inputMethodManager;
	
	@Override
	protected void onResume() {
		active = true;
		super.onResume();
	}
	@Override
	protected void onStop() {
		active = false;
		super.onStop();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("ChatActivity", this);
		setContentView(R.layout.activity_chat);
		inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		initListData();
		initEmotionView();
	}
	private void initView() {
		gobackButton = (Button)this.findViewById(R.id.chat_goback_button);
		gobackButton.setOnClickListener(this);
		personinfoButton = (Button)this.findViewById(R.id.chat_personinfo_button);
		personinfoButton.setOnClickListener(this);
		titleTextView = (TextView)this.findViewById(R.id.titlebar_title);
		friendQq = getIntent().getStringExtra("friendQq");
		String friName = new SQLiteService(getApplicationContext()).getFriendNameByQq(friendQq);
		if(friName != null) {
			titleTextView.setText(friName);
		} else {
			titleTextView.setText(friendQq);
		}
		sendButton = (Button)this.findViewById(R.id.chat_bottombar_sendbutton);
		sendButton.setOnClickListener(this);
		contentEditText = (EditText)this.findViewById(R.id.chat_bottombar_edittext);
		contentEditText.addTextChangedListener(new MaxLengthWatcher(60, contentEditText, this));
		
		contentEditText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				faceBtn.setVisibility(View.VISIBLE);
				faceFoucsBtn.setVisibility(View.GONE);
				viewPager.setVisibility(View.GONE);
				page_select.setVisibility(View.GONE);
				contentEditText.setFocusable(true);
				inputMethodManager.showSoftInput(contentEditText,0);
			}
		});
		
		bgLayout=(RelativeLayout)this.findViewById(R.id.chat_bg);
		int position = MySharePreferences.getShare(getApplicationContext()).getInt(MySharePreferences.BGSKIN, 0);
		bgLayout.setBackgroundResource(photoIds[position]);
	}
	private void initEmotionView(){
		faceBtn=(Button)findViewById(R.id.chat_bottombar_addbutton);
		faceFoucsBtn=(Button)findViewById(R.id.chat_bottombar_addbutton_focused);

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
		viewPager = (ViewPager) findViewById(R.id.chat_viewpager);
		initViewPager();
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

		SimpleAdapter simpleAdapter = new SimpleAdapter(ChatActivity.this, listItems,
				R.layout.griditem_face, new String[] { "image" },
				new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		gView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),expressionImages[position % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(ChatActivity.this, bitmap);
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

					SimpleAdapter simpleAdapter = new SimpleAdapter(ChatActivity.this,
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
							ImageSpan imageSpan = new ImageSpan(ChatActivity.this, bitmap);
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

					SimpleAdapter simpleAdapter1 = new SimpleAdapter(ChatActivity.this,
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
							ImageSpan imageSpan = new ImageSpan(ChatActivity.this, bitmap);
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

	@Override
	protected void onNewIntent(Intent intent) {
		System.out.println("before:" + intent.getStringExtra("friendQq"));
		super.onNewIntent(intent);
		setIntent(intent);//must store the new intent unless getIntent() will return the old one
		System.out.println("after:" + intent.getStringExtra("friendQq"));
		initView();
		initListData();
	}
	/**
	 * 获取本地聊天记录
	 * @param receiver
	 * @return
	 */
	public ArrayList<Map<String, Object>> getChatMessages(String receiver) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		ArrayList<Map<String, Object>> tempList = service.getChatMessages(User.qq, receiver);
		if(tempList.size() == 0) { //  如果没有消息记录
			//Toast.makeText(getApplicationContext(), "暂没有消息记录", 0).show();
		}
		return tempList;
	}
	
	public void initListData() {
		ArrayList<Map<String, Object>> data = this.getChatMessages(friendQq);
		adapter = new ChatListViewAdapter(getApplicationContext(), data, listView, this);
		ChatActivity.data = data;
		adapter.setData(data);
		listView = (ListView)this.findViewById(R.id.chat_listview);
		listView.setAdapter(adapter);
		listView.setSelection(listView.getCount() - 1); // listView显示最后一项
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_bottombar_addbutton://展开表情
			faceBtn.setVisibility(View.GONE);
			faceFoucsBtn.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.VISIBLE);
			page_select.setVisibility(View.VISIBLE);
			inputMethodManager.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
			break;
		case R.id.chat_bottombar_addbutton_focused://关闭表情
			faceBtn.setVisibility(View.VISIBLE);
			faceFoucsBtn.setVisibility(View.GONE);
			viewPager.setVisibility(View.GONE);
			page_select.setVisibility(View.GONE);
			break;
		case R.id.chat_goback_button:
			this.finish();
			break;
		case R.id.chat_personinfo_button:
			Intent intent = new Intent(getApplicationContext(), FriendInfoActivity.class);
			intent.putExtra("qq", friendQq);
			intent.putExtra("tag", "unfriendRequest");
			startActivity(intent);
			break;
		case R.id.chat_bottombar_sendbutton: 
			try {
				//表情相关处理
				viewPager.setVisibility(ViewPager.GONE);
				page_select.setVisibility(View.GONE);
				faceBtn.setVisibility(View.VISIBLE);
				faceFoucsBtn.setVisibility(View.GONE);
				inputMethodManager.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
				
				// 发送消息到服务器
				oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
				Message msg = new Message();
				msg.setType(MessageType.MSG_CHAT);
				msg.setSender(User.qq);
				msg.setReceiver(friendQq);
				msg.setDetails(contentEditText.getText().toString());
				
				String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
				msg.setTime(time);
				oos.writeObject(msg);
				
				contentEditText.setText(""); // 清空输入框
				
				// 保存聊天记录到本地
				SQLiteService service = new SQLiteService(getApplicationContext());
				service.saveMessage(msg);
				
				// 更新我的消息列表
				SQLiteService service2 = new SQLiteService(getApplicationContext());
				service2.updateMyMessageList(friendQq);
				
				// 更新聊天界面
				updateChatView(getApplicationContext(),msg);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 插入信息时更新聊天界面
	 */
	public static void updateChatView(Context context, Message msg) {
		SQLiteService service = new SQLiteService(context);
		Map<String, Object> map = service.getLastMessage(msg);
		System.out.println("最后一条消息："+map);
		data.add(map);
		adapter.setData(data);
		listView.setSelection(listView.getCount() - 1); // listView显示最后一项
	}
	/**
	 * 删除信息时更新聊天界面
	 */
	public void updateChatView() {
		ArrayList<Map<String, Object>> data = this.getChatMessages(friendQq);
		adapter.setData(data);
		listView.setSelection(listView.getCount() - 1); // listView显示最后一项
	}
}
