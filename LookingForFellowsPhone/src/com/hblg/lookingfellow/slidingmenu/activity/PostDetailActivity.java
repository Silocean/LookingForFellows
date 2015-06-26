package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.PostDetailListViewAdapter;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.selfdefinedwidget.MyGridView;
import com.hblg.lookingfellow.tools.ExpressionUtil;
import com.hblg.lookingfellow.tools.Expressions;
//具体的公告 2013/9/27
public class PostDetailActivity extends Activity implements OnClickListener{
	

	//一下是新添加的2013.10.18
	private Button faceBtn;//添加表情
	private Button faceFoucsBtn;//当表情展开时
	private Button commentBtn;//评论（最下方的）
	private EditText commentEdt;//评论点什么咯
	private Button backBtn;//返回
	
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
	
	
	
	private String TAG="PostDetailActivity";
	ListView listview;
	boolean loadfinish = true;// 是否加载完毕
	
	/**底部刷新布局*/
	View refresh;
	/**listView头部布局*/
	View headView;
	
	/**Handler What加载数据完毕**/
	private static final int WHAT_DID_LOAD_DATA = 0;
	/**Handler What更多数据完毕**/
	private static final int WHAT_DID_MORE = 1;
	/**Handler What加载数据失败**/
	private static final int WHAT_DID_FAILED=2;
	
	/**加载更多页码，默认为第二页，当刷新时重置为2，当一次加载更多完成*时加1*/
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
		
		headView=this.getLayoutInflater().inflate(R.layout.posts_detail_header, null);
		refresh = getLayoutInflater().inflate(R.layout.posts_detail_footer, null);
		listview = (ListView) findViewById(R.id.post_detail_list);
	 	listview.addHeaderView(headView);
		listview.addFooterView(refresh);
		listViewAdapter = new PostDetailListViewAdapter(getApplicationContext(),listview);
		
		initHeadView();
		
		initView();
		
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
								try{
									Thread.sleep(1000);
								}catch(Exception e){
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
	/**初始化控件*/
	private void initView(){
		faceBtn=(Button)findViewById(R.id.post_detail_add_btn);
		faceFoucsBtn=(Button)findViewById(R.id.post_detail_add_btn_focused);
		commentBtn=(Button)findViewById(R.id.post_detail_comment);
		backBtn=(Button)findViewById(R.id.post_detail_back_btn);
		commentEdt=(EditText)findViewById(R.id.chat_bottombar_edittext);
		
		faceBtn.setOnClickListener(this);
		faceFoucsBtn.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		
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
		viewPager = (ViewPager) findViewById(R.id.post_detail_viewpager);
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

		SimpleAdapter simpleAdapter = new SimpleAdapter(PostDetailActivity.this, listItems,
				R.layout.griditem_face, new String[] { "image" },
				new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		gView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),expressionImages[position % expressionImages.length]);
				ImageSpan imageSpan = new ImageSpan(PostDetailActivity.this, bitmap);
				SpannableString spannableString = new SpannableString(
						expressionImageNames[position].substring(1,expressionImageNames[position].length() - 1));
				spannableString.setSpan(imageSpan, 0,
						expressionImageNames[position].length() - 2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 编辑框设置数据
				commentEdt.append(spannableString);
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

					SimpleAdapter simpleAdapter = new SimpleAdapter(PostDetailActivity.this,
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
							ImageSpan imageSpan = new ImageSpan(PostDetailActivity.this, bitmap);
							SpannableString spannableString = new SpannableString(
									expressionImageNames1[arg2]
											.substring(1,
													expressionImageNames1[arg2]
															.length() - 1));
							spannableString.setSpan(imageSpan, 0,
									expressionImageNames1[arg2].length() - 2,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							// 编辑框设置数据
							commentEdt.append(spannableString);
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

					SimpleAdapter simpleAdapter1 = new SimpleAdapter(PostDetailActivity.this,
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
							ImageSpan imageSpan = new ImageSpan(PostDetailActivity.this, bitmap);
							SpannableString spannableString = new SpannableString(
									expressionImageNames2[arg2]
											.substring(1,expressionImageNames2[arg2].length() - 1));
							spannableString.setSpan(imageSpan, 0,
									expressionImageNames2[arg2].length() - 2,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							// 编辑框设置数据
							commentEdt.append(spannableString);
							System.out.println("edit的内容 = " + spannableString);
						}
					});
					break;

				}
			}
	}

	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.post_detail_add_btn://添加（展开表情）表情
			faceBtn.setVisibility(faceBtn.GONE);
			faceFoucsBtn.setVisibility(faceBtn.VISIBLE);
			viewPager.setVisibility(viewPager.VISIBLE);
			page_select.setVisibility(page_select.VISIBLE);
			break;
		case R.id.post_detail_add_btn_focused://关闭表情
			faceBtn.setVisibility(faceBtn.VISIBLE);
			faceFoucsBtn.setVisibility(faceBtn.GONE);
			viewPager.setVisibility(viewPager.GONE);
			page_select.setVisibility(page_select.GONE);
			break;
		case R.id.post_detail_comment://评论
			String content = commentEdt.getText().toString();
			
			if(content.length()>0){
				commentEdt.setText("");
				viewPager.setVisibility(ViewPager.GONE);
				page_select.setVisibility(page_select.GONE);
				
				faceBtn.setVisibility(faceBtn.VISIBLE);
				faceFoucsBtn.setVisibility(View.GONE);
				
				
				//测试解析数据
				String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
				try {
					SpannableString spannableString = 
							ExpressionUtil.getExpressionString(getApplicationContext(), content, zhengze);
					
					Toast.makeText(getApplicationContext(), spannableString, 5000).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getApplicationContext(), "评论内容不能为空", 2000).show();
			}
			break;
		case R.id.post_detail_back_btn://返回
			finish();
			break;
		}
		
	}
	private void loadData(){
		new Thread() {
			public void run() {
				Message msg=new Message();
				msg.what=WHAT_DID_LOAD_DATA;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**初始化listview头部控件*/
	private void initHeadView(){
		
		listViewAdapter.setData(data);
		listview.setAdapter(listViewAdapter);
		listview.removeFooterView(refresh);
	
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what){
			case WHAT_DID_FAILED:
				break;
			case WHAT_DID_LOAD_DATA:
				data=getData();
				listViewAdapter.setData(data);
				listview.setAdapter(listViewAdapter);
				listview.removeFooterView(refresh);
				isFlow=true;
				loadfinish=true;
				break;
			case WHAT_DID_MORE:
				data.addAll(getData());
				listview.removeFooterView(refresh);
				loadfinish=true;
				break;
			}
		}
	};
	public ArrayList<Map<String,Object>> getData(){
		ArrayList<Map<String,Object>> temp = new ArrayList<Map<String,Object>>();
		for( int i=0;i<5;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			//
			temp.add(map);
			map=null;
		}
		return temp;
	}
	

}
