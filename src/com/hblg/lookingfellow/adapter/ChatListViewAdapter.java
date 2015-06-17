package com.hblg.lookingfellow.adapter;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.ImageTool;


public class ChatListViewAdapter extends BaseAdapter {
	
	Context context;
	List<Map<String, Object>> list;
	LayoutInflater inflater;
	
	public ChatListViewAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			if(position%2==0) {
				convertView = inflater.inflate( R.layout.chat_item_msg_text_left, null);
			} else {
				convertView = inflater.inflate( R.layout.chat_item_msg_text_right, null);
			}
		}
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		TextView leftSendTimeTextview = (TextView)convertView.findViewById(R.id.chat_item_sendtime);
		String leftSendTime = (String)map.get("leftsendtime");
		leftSendTimeTextview.setText(leftSendTime);
		ImageView leftHeadImage = (ImageView)convertView.findViewById(R.id.chat_item_headimage);
		Bitmap bm = (Bitmap)map.get("leftheadimage");
		Bitmap output = ImageTool.toRoundCorner(bm, 15.0f);
		leftHeadImage.setImageBitmap(output);
		TextView leftChatContentTextview = (TextView)convertView.findViewById(R.id.chat_item_chatcontent);
		String leftChatContent = (String)map.get("leftchatcontent");
		leftChatContentTextview.setText(leftChatContent);
		return convertView;
	}

}
