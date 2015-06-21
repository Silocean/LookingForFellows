package com.hblg.lookingfellow.adapter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageClientConnServer;
import com.hblg.lookingfellow.tools.ImageUtils;
import com.hblg.lookingfellow.tools.TimeConvertTool;
import com.hblg.lookingfellow.tools.ImageUtils.ImageCallBack;

public class SearchfriendsListViewAdapter extends BaseAdapter {
	
	ArrayList<Map<String, String>> list;
	int resourceId;
	LayoutInflater inflater;
	Context context;
	ListView listView;
	
	Bitmap bm;
	
	ViewHolder holder;
	
	ObjectOutputStream oos = null;
	
	public SearchfriendsListViewAdapter(Context context, ArrayList<Map<String, String>> list, int resourceId, ListView listView) {
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
			holder.headImage = (ImageView)convertView.findViewById(R.id.searchfriendslayout_headimage);
			holder.nameTextView = (TextView)convertView.findViewById(R.id.searchfriendslayout_name);
			holder.hometownTextView = (TextView)convertView.findViewById(R.id.searchfriendslayout_hometown);
			holder.addFriend = (Button)convertView.findViewById(R.id.searchfriendslayout_add);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		//qq
		final String friendQq = (String)map.get("friendQq");
		// 姓名
		String name = (String)map.get("friendName");
		holder.nameTextView.setText(name);
		// 家乡
		String hometown = (String)map.get("friendHometown");
		holder.hometownTextView.setText(hometown);
		
		holder.addFriend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendAddFriendRequest(User.qq, friendQq);
			}

		});
		//头像
		ImageUtils.ImageCallBack callBack = new ImageCallBack() {
			public void loadImage(Bitmap bitMap, String imageTag) {
				ImageView imageView = (ImageView)listView.findViewWithTag(imageTag);
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
		ImageUtils.setImageView(holder.headImage, headUrl, context, callBack);
		return convertView;
	}
	/**
	 * 发送请求添加好友请求
	 * @param qq
	 * @param friendQq
	 */
	protected void sendAddFriendRequest(String qq, String friendQq) {
		try {
			oos = new ObjectOutputStream(ManageClientConnServer.getClientConServerThread(User.qq).getS().getOutputStream());
			Message msg = new Message();
			msg.setType(MessageType.MSG_REQUESTADDFRIEND);
			msg.setSender(qq);
			msg.setReceiver(friendQq);
			msg.setDetails("我是XXX");
			String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
			msg.setTime(time);
			oos.writeObject(msg);
			System.out.println("send add friend message");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ViewHolder {
		private ImageView headImage;
		private TextView  nameTextView;
		private TextView  hometownTextView;
		private Button addFriend;
	}
}
