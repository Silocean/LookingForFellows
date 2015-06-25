package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ExpressionUtil;
import com.hblg.lookingfellow.tools.ImageTool;

public class PostDetailListViewAdapter extends BaseAdapter{
	private ListView listview;
	Context context;
	List<Map<String, Object>>list;
	Map<String,Object> map = null;
	Holder holder=null;
	LayoutInflater inflater;
	LinearLayout linearLayout;
	Bitmap bitmap;
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
		map = list.get(position);
		if(null==convertView){
			convertView=inflater.inflate(R.layout.listitem_postsdetail, null);
			
			holder.headImg=(ImageView)convertView.findViewById(R.id.posts_list_headImg);
			holder.nameTxt=(TextView)convertView.findViewById(R.id.posts_list_nameTxt);
			holder.timeTxt=(TextView)convertView.findViewById(R.id.posts_list_time);
			holder.floorTxt=(TextView)convertView.findViewById(R.id.posts_list_floor);
			holder.contentTxt=(TextView)convertView.findViewById(R.id.posts_list_content);

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
		// 头像
		final String authorId = (String)map.get("authorId");
		bitmap = ImageTool.getHeadImageFromLocalOrNet(context, authorId);
		bitmap = ImageTool.toRoundCorner(bitmap, 15);
		holder.headImg.setImageBitmap(bitmap);
		if(!authorId.equals(User.qq)) {
			holder.headImg.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(context, FriendInfoActivity.class);
					// 防止 Calling startActivity() from outside of an Activity问题发生
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("qq", authorId);
					SQLiteService service = new SQLiteService(context);
					boolean flag = service.checkIsFriend(authorId);
					if(flag) {
						intent.putExtra("tag", "unfriendRequest");
					} else {
						intent.putExtra("tag", "addRequest");
					}
					context.startActivity(intent);
				}
			});
		}
		
		// 名字
		String authorName = (String)map.get("authorName");
		holder.nameTxt.setText(authorName);
		
		// 时间
		String time = (String)map.get("time");
		holder.timeTxt.setText(time);
		
		// 内容
		String details = (String)map.get("details");
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断回复里是否有表情
		SpannableString spannableString = ExpressionUtil.getExpressionString(context, details, zhengze);
		holder.contentTxt.setText(spannableString);
		
		return convertView;
	}
	
	//优化
	private class Holder{
		ImageView headImg;//层主头像
		TextView nameTxt;//层主昵称
		TextView timeTxt;//时间
		TextView floorTxt;//多少楼（1楼，2楼）
		TextView contentTxt;//帖子内容
		
	}
	

}
