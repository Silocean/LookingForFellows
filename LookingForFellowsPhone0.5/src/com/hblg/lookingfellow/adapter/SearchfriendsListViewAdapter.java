package com.hblg.lookingfellow.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.ImageTool;

public class SearchfriendsListViewAdapter extends BaseAdapter {
	
	List<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	Context context;
	
	public SearchfriendsListViewAdapter(Context context, List<Map<String, Object>> list, int resourceId) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
		}
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		ImageView headImage = (ImageView)convertView.findViewById(R.id.searchfriendslayout_headimage);
		Bitmap bm = (Bitmap)map.get("headimage");
		Bitmap output = ImageTool.toRoundCorner(bm, 360.0f);
		headImage.setImageBitmap(output);
		TextView nameTextView = (TextView)convertView.findViewById(R.id.searchfriendslayout_name);
		String name = (String)map.get("name");
		nameTextView.setText(name);
		TextView hometownTextView = (TextView)convertView.findViewById(R.id.searchfriendslayout_hometown);
		String hometown = (String)map.get("hometown");
		hometownTextView.setText(hometown);
		Button addFriend = (Button)convertView.findViewById(R.id.searchfriendslayout_add);
		addFriend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO 处理聊天事件
				Toast.makeText(context, "addFriend"+position, 0).show();
			}
		});
		return convertView;
	}

}
