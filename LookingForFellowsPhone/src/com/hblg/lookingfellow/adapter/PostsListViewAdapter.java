package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ExpressionUtil;
import com.hblg.lookingfellow.tools.ImageUtils;
import com.hblg.lookingfellow.tools.ImageUtils.ImageCallBack;

public class PostsListViewAdapter extends BaseAdapter {
	private String TAG="PostsListViewAdapter";
	private ListView listview;
	ArrayList<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	private ViewHolder holder;
	Context context;
	
	public PostsListViewAdapter(Context mcontext, ArrayList<Map<String, Object>> list, int resourceId, ListView listview) {
		this.context=mcontext;
		this.list = list;
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listview=listview;
	}
	public void setData(ArrayList<Map<String, Object>> data) {
		this.list=data;
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
		holder=new ViewHolder();
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
			holder=(ViewHolder)convertView.getTag();
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		
		// 发帖人ID
		final String qq = (String)map.get("authorId");
		
		//标题
		String title = (String)map.get("title");
		holder.titleTextView.setText(title);
		//内容
		String content = (String)map.get("content");
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断回复里是否有表情
		SpannableString spannableString = ExpressionUtil.getExpressionString(context, content, zhengze);
		holder.contentTextView.setText(spannableString);
		//回复
		int replycount = (Integer)map.get("replycount");
		holder.replycountTextView.setText(replycount+"");
		//名字
		String publishname = (String)map.get("publishname");
		holder.publishnameTextView.setText(publishname);
		//时间
		String publishtime = (String)map.get("publishtime");
		publishtime = publishtime.substring(5, 16);
		holder.publishtimeTextView.setText(publishtime);
		
		//头像
		ImageUtils.ImageCallBack callBack = new ImageCallBack() {
			public void loadImage(Bitmap bitMap, String imageTag) {
				ImageView imageView = (ImageView)listview.findViewWithTag(imageTag);
				if(null==bitMap){
					imageView.setBackgroundResource(R.drawable.head_default);
				}else if(null==imageView){
					holder.headImage.setBackgroundResource(R.drawable.head_default);
					return;
				}
				imageView.setImageBitmap(bitMap);
			}
		};
		String headUrl = (String)map.get("headimage");
		Log.v(TAG, "headUrl--->"+headUrl);
		ImageUtils.setImageView(holder.headImage, headUrl, context, callBack);
		
		holder.headImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FriendInfoActivity.class);
				// 防止 Calling startActivity() from outside of an Activity问题发生
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("qq", qq);
				SQLiteService service = new SQLiteService(context);
				boolean flag = service.checkIsFriend(qq);
				if(flag) {
					intent.putExtra("tag", "unfriendRequest");
				} else {
					intent.putExtra("tag", "addRequest");
				}
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView headImage;
		private TextView  titleTextView;
		private TextView  contentTextView;
		private TextView  replycountTextView;
		private TextView  publishnameTextView;
		private TextView  publishtimeTextView;
	}
	

}
