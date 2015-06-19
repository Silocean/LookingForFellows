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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.PostsListViewAdapter;
import com.hblg.lookingfellow.cache.ACache;
import com.hblg.lookingfellow.entity.Post;
import com.hblg.lookingfellow.selfdefinedwidget.DropDownListView;
import com.hblg.lookingfellow.selfdefinedwidget.DropDownListView.OnDropDownListener;
import com.hblg.lookingfellow.slidingmenu.activity.SendPostActivity;
import com.hblg.lookingfellow.slidingmenu.activity.SlidingActivity;
import com.hblg.lookingfellow.sqlite.DBOpenHelper;
import com.hblg.lookingfellow.tools.StreamTool;

public class MainFragment extends Fragment {
	
	DropDownListView listView;
	LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
	private PostsListViewAdapter adapter;
	ImageView titlebarLeftmenu;
	ImageView titlebarRightmenu;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_content_posts, null);
		listView = (DropDownListView)view.findViewById(R.id.contentList);
		listView.setOnDropDownListener(new OnDropDownListener() {
			
			@Override
			public void onDropDown() {
				new GetDataTask(true).execute();
			}
		});
		listView.setOnBottomListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GetDataTask(false).execute();
			}
		});
		titlebarLeftmenu = (ImageView)view.findViewById(R.id.main_titlebar_leftmenu);
		titlebarRightmenu = (ImageView)view.findViewById(R.id.main_titlebar_rightmenu);
		return view;
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, LinkedList<Map<String, Object>>> {
		private boolean isDropDown;
		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}
		@Override
		protected LinkedList<Map<String, Object>> doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}

		@Override
		protected void onPostExecute(LinkedList<Map<String, Object>> result) {
			if(isDropDown) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.headimage);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("headimage", bitmap);
				map.put("title", "上啦刷新");
				map.put("content", "在这里你可以找到你所在高校中的老乡，你可以在这里发帖召集失散已久的老乡，还可以在侧边栏里搜索你的好友们!");
				map.put("replaycount", 35+"");
				map.put("publishname", "linxiaonan");
				map.put("publishtime", "10:17");
				result.addFirst(map);
				adapter.notifyDataSetChanged();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
	            listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.headimage);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("headimage", bitmap);
				map.put("title", "下啦刷新");
				map.put("content", "在这里你可以找到你所在高校中的老乡，你可以在这里发帖召集失散已久的老乡，还可以在侧边栏里搜索你的好友们!");
				map.put("replaycount", 35+"");
				map.put("publishname", "linxiaonan");
				map.put("publishtime", "10:17");
				result.add(map);
				adapter.notifyDataSetChanged();
	            listView.onBottomComplete();
			}
		}
		
	}
	
	private List<Post> getPosts(String tag) throws Exception {
		List<Post> posts = new ArrayList<Post>();
		String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/getPostsServlet?tag=";
		path = path + tag;
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		if(conn.getResponseCode() == 200) {
			InputStream in = conn.getInputStream();
			byte[] data = StreamTool.read(in);
			String str = new String(data);
			if(str.equals("error")) {
				Toast.makeText(getActivity(), "服务器端出现问题，请稍后再试", 0).show();
			} else {
				JSONArray array = new JSONArray(str);
				//saveToCache(array); // 保存帖子条目数据到缓存
				for(int i=0; i<array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Post post = new Post();
					post.setId(obj.getInt("id"));
					post.setTitle(obj.getString("title"));
					post.setDetails(obj.getString("details"));
					post.setTime(obj.getString("time"));
					post.setAuthorId(obj.getString("authorId"));
					post.setAuthorName(obj.getString("authorName"));
					post.setReplyNum(obj.getInt("replyNum"));
					posts.add(post);
				}
				return posts;
			}
		}
		Toast.makeText(getActivity(), "网络连接出现问题", 0).show();
		return null;
	}

	private void saveToCache(JSONArray array) {
		ACache cache = ACache.get(getActivity());
		cache.put("jsonarray", array);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			
			List<Post> posts = this.getPosts("head");
			if(posts != null) {
				for(int i=0; i<posts.size(); i++) {
					Post post = posts.get(i);
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.headimage);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("headimage", bitmap);
					map.put("title", post.getTitle());
					map.put("content", post.getDetails());
					map.put("replycount", post.getReplyNum()+"");
					map.put("publishname", post.getAuthorName());
					map.put("publishtime", post.getTime());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	}

}
