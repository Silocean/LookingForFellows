package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.tools.ImageTool;

public class friendsListViewAdapter extends BaseAdapter {
	
	ArrayList<Map<String, String>> list;
	int resourceId;
	LayoutInflater inflater;
	Context context;
	ListView listView;
	
	Bitmap bm;
	
	ViewHolder holder;
	
	public friendsListViewAdapter(Context context, ArrayList<Map<String, String>> list, int resourceId, ListView listView ) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		this.listView = listView;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(ArrayList<Map<String, String>> list) {
		this.list = list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = new ViewHolder();
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			holder.headImage = (ImageView)convertView.findViewById(R.id.friendslayout_headimage);
			holder.nameTextView = (TextView)convertView.findViewById(R.id.friendslayout_name);
			holder.sexImageView = (ImageView)convertView.findViewById(R.id.friendslayout_sex);
			holder.hometownTextView = (TextView)convertView.findViewById(R.id.friendslayout_hometown);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		// qq号码
		final String qq = (String)map.get("friQQ");
		// 名字
		String name = (String)map.get("friName");
		holder.nameTextView.setText(name);
		// 性别
		String sex = (String)map.get("friSex");
		if(sex.equals("男")) {
			holder.sexImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.modifysex_male_icon));
		} else if(sex.equals("女")){
			holder.sexImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.modifysex_female_icon));
		}
		// 老家
		String hometown = (String)map.get("friPro") + " " + (String)map.get("friCity");
		holder.hometownTextView.setText(hometown);
		
		Button chat = (Button)convertView.findViewById(R.id.friendslayout_chat);
		chat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, ChatActivity.class);
				// 防止 Calling startActivity() from outside of an Activity问题发生
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("friendQq", qq);
				context.startActivity(intent);
			}
		});
		// 头像
		bm = ImageTool.getHeadImageFromLocalOrNet(context, qq);
		bm = ImageTool.toRoundCorner(bm, 15);
		holder.headImage.setImageBitmap(bm);
		holder.headImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, FriendInfoActivity.class);
				// 防止 Calling startActivity() from outside of an Activity问题发生
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("qq", qq);
				intent.putExtra("tag", "unfriendRequest");
				context.startActivity(intent);
			}
		});
		
		return convertView;
		
	}
	
	private class ViewHolder {
		private ImageView headImage;
		private TextView  nameTextView;
		private ImageView sexImageView;
		private TextView  hometownTextView;
	}

}
