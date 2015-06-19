package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.fragment.SettingsFragment;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.ImageUtils;
import com.hblg.lookingfellow.tools.ImageUtils.ImageCallBack;

public class PostsListViewAdapter extends BaseAdapter {
	private String TAG="PostsListViewAdapter";
	private ListView listview;
	List<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	private Holder holder;
	Context context;
	
	public PostsListViewAdapter(Context mcontext, List<Map<String, Object>> list, int resourceId,ListView listview) {
		this.context=mcontext;
		this.list = list;
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listview=listview;
	}
	public void setData(ArrayList<Map<String, Object>> data) {
		this.list=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
		}
		
		ImageView headImage = (ImageView)convertView.findViewById(R.id.postlayout_headimage);
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		Bitmap bm = (Bitmap)map.get("headimage");
		Bitmap output = ImageTool.toRoundCorner(bm, 360.0f);
		headImage.setImageBitmap(output);
		
		TextView titleTextView = (TextView)convertView.findViewById(R.id.postlayout_title);
		String title = (String)map.get("title");
		titleTextView.setText(title);
		
		TextView contentTextView = (TextView)convertView.findViewById(R.id.postlayout_content);
		String content = (String)map.get("content");
		contentTextView.setText(content);
		
		
		TextView replycountTextView = (TextView)convertView.findViewById(R.id.postlayout_replay_count);
		String replycount = (String)map.get("replaycount");
		replycountTextView.setText(replycount);
		
		
		TextView publishnameTextView = (TextView)convertView.findViewById(R.id.postlayout_publish_name);
		String publishname = (String)map.get("publishname");
		publishnameTextView.setText(publishname);
		
		
		TextView publishtimeTextView = (TextView)convertView.findViewById(R.id.postlayout_publish_time);
		String publishtime = (String)map.get("publishtime");
		publishtimeTextView.setText(publishtime);*/
		holder=new Holder();
		if(null==convertView){
			convertView = inflater.inflate(resourceId, null);
			holder.headImage = (ImageView)convertView.findViewById(R.id.postlayout_headimage);
			holder.titleTextView = (TextView)convertView.findViewById(R.id.postlayout_title);
			holder.contentTextView = (TextView)convertView.findViewById(R.id.postlayout_content);
			holder.replycountTextView = (TextView)convertView.findViewById(R.id.postlayout_replay_count);
			holder.replycountTextView = (TextView)convertView.findViewById(R.id.postlayout_replay_count);
			holder.publishnameTextView = (TextView)convertView.findViewById(R.id.postlayout_publish_name);
			holder.publishtimeTextView = (TextView)convertView.findViewById(R.id.postlayout_publish_time);
			
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
	
		//标题
		String title = (String)map.get("title");
		holder.titleTextView.setText(title);
		//内容
		String content = (String)map.get("content");
		holder.contentTextView.setText(content);
		//回复
		String replycount = (String)map.get("replaycount");
		holder.replycountTextView.setText(replycount);
		//名字
		String publishname = (String)map.get("publishname");
		holder.publishnameTextView.setText(publishname);
		//时间
		String publishtime = (String)map.get("publishtime");
		holder.publishtimeTextView.setText(publishtime);
		
		//头像
		ImageUtils.ImageCallBack callBack = new ImageCallBack() {
			public void loadImage(Bitmap bitMap, String imageTag) {
				ImageView imageView = (ImageView)listview.findViewWithTag(imageTag);
				if(null==bitMap){
					imageView.setBackgroundResource(R.drawable.headimage);
				}else if(null==imageView){
					holder.headImage.setBackgroundResource(R.drawable.headimage);
					return;
				}
				imageView.setImageBitmap(bitMap);
			}
		};
		String headUrl = (String)map.get("headimage");
		Log.v(TAG, "headUrl--->"+headUrl);
		ImageUtils.setImageView(holder.headImage,headUrl,context, callBack);

		
		
		
		
		/*Bitmap output = ImageTool.toRoundCorner(bm, 360.0f);
		holder.headImage.setImageBitmap(output);*/
		
		return convertView;
	}
	
	private class Holder{
		private ImageView headImage;
		private TextView  titleTextView;
		private TextView  contentTextView;
		private TextView  replycountTextView;
		private TextView  publishnameTextView;
		private TextView  publishtimeTextView;
	}
	

}
