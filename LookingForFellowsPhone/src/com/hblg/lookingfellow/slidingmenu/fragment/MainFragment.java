package com.hblg.lookingfellow.slidingmenu.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.Post;
import com.hblg.lookingfellow.pla.util.Helper;
import com.hblg.lookingfellow.pla.util.ImageFetcher;
import com.hblg.lookingfellow.pla.view.ScaleImageView;
import com.hblg.lookingfellow.pla.view.XListView;
import com.hblg.lookingfellow.pla.view.XListView.IXListViewListener;
import com.hblg.lookingfellow.slidingmenu.activity.ChangeProActitity;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.PostDetailActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SendPostActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.MySharePreferences;
import com.hblg.lookingfellow.tools.TimeConvertTool;
import com.hblg.lookingfellow.tools.UIMode;

public class MainFragment extends Fragment implements IXListViewListener {
	private View thisLayout;
	private TextView titleBarvTextView;
	private ImageView titlebarLeftmenu;
	private ImageView titlebarRightmenu;
	
	String imagePath = Common.PATH + "head/";
	
	private String province = "公告板";
	
	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	private int currentPage = 0;
	ContentTask task = new ContentTask(getActivity(), 2);
	
	public static boolean showPic = true;
	
	private class ContentTask extends
			AsyncTask<String, Integer, LinkedList<Map<String, Object>>> {
		
		private Context mContext;
		private int mType = 1;

		public ContentTask(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected LinkedList<Map<String, Object>> doInBackground(
				String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(LinkedList<Map<String, Object>> result) {
			if (mType == 1) {
				mAdapter.addItemTop(result);
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
			} else if (mType == 2) {
				mAdapterView.stopLoadMore();
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();
			}

		}

		@Override
		protected void onPreExecute() {
		}

		public LinkedList<Map<String, Object>> parseNewsJSON(String url)
				throws IOException {
			LinkedList<Map<String, Object>> postData = new LinkedList<Map<String, Object>>();
			String json = "";
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);
				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return postData;
				}
			}
			Log.d("MainActiivty", "json:" + json);

			try {
				if (null != json) {
					JSONArray array = new JSONArray(json);
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("headimage", imagePath + "head_" + obj.get("authorId") + ".jpg");
						map.put("postId", obj.getInt("id"));
						map.put("authorId", obj.getString("authorId"));
						map.put("title", obj.getString("title"));
						map.put("content", obj.getString("details"));
						map.put("replycount", obj.getInt("replyNum"));
						map.put("publishname", obj.getString("authorName"));
						map.put("publishtime", obj.getString("time"));
						map.put("imageName", obj.getString("imageName"));
						postData.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return postData;
		}
	}

	/**
	 * 添加内容
	 * @param pageindex
	 * @param type
	 *            1为下拉刷新 2为加载更多
	 */
	private void AddItemToContainer(int pageindex, int type) {
		if (task.getStatus() != Status.RUNNING) {
			String url = Common.PATH + "PostsServlet?province=" + province + "&page=";
			url = url + pageindex;
			Log.d("MainActivity", "current url:" + url);
			ContentTask task = new ContentTask(getActivity(), type);
			task.execute(url);
		}
	}

	public class StaggeredAdapter extends BaseAdapter {
		private Context mContext;
		private LinkedList<Map<String, Object>> mInfos;
		private XListView mListView;

		public StaggeredAdapter(Context context, XListView xListView) {
			this.mContext = context;
			this.mInfos = new LinkedList<Map<String, Object>>();
			this.mListView = xListView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			final Map<String, Object> map = mInfos.get(position);

			if (convertView == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(parent
						.getContext());
				convertView = layoutInflator.inflate(R.layout.infos_list, null);
				holder = new ViewHolder();
				holder.headimageView = (ImageView) convertView.findViewById(R.id.headimageview);
				holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
				holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
				holder.replycountView = (TextView) convertView.findViewById(R.id.replycountView);
				holder.timeView = (TextView) convertView.findViewById(R.id.timeView);
				holder.nameView = (TextView) convertView.findViewById(R.id.authorNameView);
				
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			final String qq = (String)map.get("authorId");
			String imageName = ((String)map.get("imageName")).split(";")[0];
			int height = 0;
			int width = 0;
			if(showPic) {
				height = 200; // 默认高度为200
				width = 200;
				if(imageName.equals("")) {
					mImageFetcher.loadImage(Common.PATH + "head/defaultbg.png", holder.imageView);
				} else {
					height = Integer.parseInt(imageName.substring(imageName.indexOf('_')+1, imageName.indexOf('.')));
					System.out.println("imageName:" + imageName);
					mImageFetcher.loadImage(Common.PATH + "post/" + imageName, holder.imageView);
				}
			} else {
				// 不获取图片，也不设置图片大小
			}
			String content = (String) map.get("content");
			content = this.replaceContent(content);
			holder.imageView.setImageWidth(width);
			holder.imageView.setImageHeight(height);
			holder.nameView.setText((String)map.get("publishname"));
			String time = (String)map.get("publishtime");
			time = TimeConvertTool.calDateTime(time);
			holder.timeView.setText(time);
			holder.contentView.setText(content);
			holder.replycountView.setText((Integer)map.get("replycount")+"");
			Bitmap bm = ImageTool.getHeadImageFromLocalOrNet(mContext, qq);
			holder.headimageView.setImageBitmap(bm);
			
			holder.headimageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(mContext, FriendInfoActivity.class);
					// 防止 Calling startActivity() from outside of an Activity问题发生
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("qq", qq);
					SQLiteService service = new SQLiteService(mContext);
					boolean flag = service.checkIsFriend(qq);
					if(flag) {
						intent.putExtra("tag", "unfriendRequest");
					} else {
						intent.putExtra("tag", "addRequest");
					}
					mContext.startActivity(intent);
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Post post = new Post();
					post.setId((Integer)map.get("postId"));
					post.setAuthorId((String)map.get("authorId"));
					post.setAuthorName((String)map.get("publishname"));
					post.setTitle((String)map.get("title"));
					post.setDetails((String)map.get("content"));
					post.setReplyNum((Integer)map.get("replycount"));
					post.setTime((String)map.get("publishtime"));
					Intent intent = new Intent(getActivity(), PostDetailActivity.class);
					intent.putExtra("post", post);
					startActivity(intent);
				}
			});
			return convertView;
		}
		/**
		 * 替换掉图片名字
		 * @param content
		 * @return
		 */
		private String replaceContent(String content) {
			String zhengze = "\\[[0-9a-z]{32}\\]_\\d+.jpg";
			Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()) {
				String str = matcher.group();
				content = content.replace(str, "");
			}
			return content;
		}

		class ViewHolder {
			ScaleImageView imageView;
			ImageView headimageView;
			TextView contentView;
			TextView nameView;
			TextView timeView;
			TextView replycountView;
		}

		@Override
		public int getCount() {
			return mInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void addItemLast(LinkedList<Map<String, Object>> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(LinkedList<Map<String, Object>> datas) {
			mInfos.clear();
			for (int i=datas.size()-1; i>=0; i--) {
				mInfos.addFirst(datas.get(i));
			}
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1) {
			mImageFetcher.setExitTasksEarly(false);
			mAdapterView.setAdapter(mAdapter);
			AddItemToContainer(0, 1);
		} else if(requestCode == 5) { // 更改省份
			System.out.println("=========更改省份");
			if(data != null) {
				province = data.getStringExtra("pro");
				titleBarvTextView.setText(province);
				mImageFetcher.setExitTasksEarly(false);
				mAdapterView.setAdapter(mAdapter);
				AddItemToContainer(0, 1);
			}
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisLayout = inflater.inflate(R.layout.main_content_posts, null);
		//initMode();
		return thisLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapterView = (XListView) thisLayout.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new StaggeredAdapter(getActivity(), mAdapterView);
		mImageFetcher = new ImageFetcher(getActivity(), 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mAdapterView.setAdapter(mAdapter);
		AddItemToContainer(0, 1);
		mImageFetcher.setExitTasksEarly(false);
		titleBarvTextView = (TextView)thisLayout.findViewById(R.id.titlebar_title);
		titleBarvTextView.setText(province);
		titleBarvTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ChangeProActitity.class);
				startActivityForResult(intent, 5);
			}
		});
		titlebarLeftmenu = (ImageView) thisLayout
				.findViewById(R.id.main_titlebar_leftmenu);
		titlebarLeftmenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});
		titlebarRightmenu = (ImageView) thisLayout
				.findViewById(R.id.main_titlebar_rightmenu);
		titlebarRightmenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SendPostActivity.class);
				startActivityForResult(intent, 1); // 1表示发帖activity撤销，并发帖成功
			}
		});
	}
	
	public void initMode() {
		SharedPreferences sf = MySharePreferences.getShare(getActivity());
		//亮度模式
		int flag = sf.getInt(MySharePreferences.UI, 2);
		
		if(2 == flag) {
			UIMode.changeUIMode(getActivity(), 0);
		} else {
			UIMode.changeUIMode(getActivity(), 2);
		}
		
		int status=MySharePreferences.getShare(getActivity()).getInt(MySharePreferences.PHOTODEAL, 1);
		if (1==status) {
			showPic = true;
		}else{
			showPic = false;
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("+++++++++++" + province);
		titleBarvTextView.setText(province);
		//initMode();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRefresh() {
		AddItemToContainer(0, 1);
	}

	@Override
	public void onLoadMore() {
		AddItemToContainer(++currentPage, 2);

	}
}