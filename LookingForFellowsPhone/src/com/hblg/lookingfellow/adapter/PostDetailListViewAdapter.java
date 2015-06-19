package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;

public class PostDetailListViewAdapter extends BaseAdapter{
	private ListView listview;
	Context context;
	List<Map<String, Object>>list;
	Map<String,Object> daoMap=null;
	Holder holder=null;
	LayoutInflater inflater;
	LinearLayout linearLayout;
	public PostDetailListViewAdapter(Context context,ListView listview){
		this.context=context;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listview=listview;
		
	}
	public void setData(ArrayList<Map<String, Object>>list){
		this.list=list;
		notifyDataSetChanged();
	}
	public void clearData(){
		list.clear();
		notifyDataSetChanged();
	}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder=new Holder();
		daoMap=list.get(position);
		if(null==convertView){
			convertView=inflater.inflate(R.layout.listitem_postsdetail, null);
			linearLayout=(LinearLayout)convertView.findViewById(R.id.posts_list_layout);
			
			holder.headImg=(ImageView)convertView.findViewById(R.id.posts_list_headImg);
			holder.contentImg=(ImageView)convertView.findViewById(R.id.posts_list_contentImg);
			holder.nameTxt=(TextView)convertView.findViewById(R.id.posts_list_nameTxt);
			holder.timeTxt=(TextView)convertView.findViewById(R.id.posts_list_time);
			holder.floorTxt=(TextView)convertView.findViewById(R.id.posts_list_floor);
			holder.contentTxt=(TextView)convertView.findViewById(R.id.posts_list_content);
			holder.oneNameTxt=(TextView)convertView.findViewById(R.id.one_name);
			holder.oneTxt=(TextView)convertView.findViewById(R.id.one_txt);
			holder.twoNameTxt=(TextView)convertView.findViewById(R.id.two_name);
			holder.twoTxt=(TextView)convertView.findViewById(R.id.two_txt);
			holder.expandTxt=(TextView)convertView.findViewById(R.id.expand_comments);
			holder.commentBtn=(Button)convertView.findViewById(R.id.posts_list_commentBtn);

			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		
		//楼
		if(0==position){
			holder.floorTxt.setText("楼主");
		}else{
			holder.floorTxt.setText(position+1+"楼");
		}
		
		//查看剩余评论
		holder.expandTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//临时测试
				Toast.makeText(context,"查看"+(position+1)+"楼剩余评论", 2000).show();
			}
		});
		
		
		//评论
		holder.commentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//临时测试
				Toast.makeText(context,""+(position+1), 2000).show();
			}
		});
		
		return convertView;
	}
	
	//优化
	private class Holder{
		ImageView headImg;//层主头像
		ImageView contentImg;//内容照片
		TextView nameTxt;//层主昵称
		TextView  timeTxt;//时间
		TextView floorTxt;//多少楼（1楼，2楼）
		TextView contentTxt;//帖子内容
		TextView oneNameTxt;//第一个评论人昵称
		TextView oneTxt;//第一个评论人评论内容
		TextView twoNameTxt;//第二个评论人昵称
		TextView twoTxt;//第二个评论人内容
		Button commentBtn;//评论
		TextView expandTxt;//展开评论
		
	}
	

}
