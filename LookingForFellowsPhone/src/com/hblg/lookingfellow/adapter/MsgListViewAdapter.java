package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ExpressionUtil;
import com.hblg.lookingfellow.tools.ImageTool;

public class MsgListViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	ListView listView;
	
	Bitmap bm;
	
	ViewHolder holder;
	
	public MsgListViewAdapter(Context context, ArrayList<Map<String, Object>> list, int resourceId, ListView listView) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		this.listView = listView;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(ArrayList<Map<String, Object>> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new ViewHolder();
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			holder.headImage = (ImageView)convertView.findViewById(R.id.msglayout_headimage);
			holder.nameTextView = (TextView)convertView.findViewById(R.id.msglayout_name);
			holder.contentTextView = (TextView)convertView.findViewById(R.id.msglayout_content);
			holder.timeTextView = (TextView)convertView.findViewById(R.id.msglayout_time);
			holder.newMsgImageView = (Button)convertView.findViewById(R.id.msglayout_newMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		
		// ����
		String qq;
		if(User.qq.equals((String)map.get("msgSender"))) {
			qq = (String)map.get("msgReceiver");
		} else {
			qq = (String)map.get("msgSender");
		}
		// ����
		String name = new SQLiteService(context).getFriendNameByQq(qq);
		if(name != null) {
			holder.nameTextView.setText(name); // �����Ϊ�գ� ��ʾ����
		} else {
			holder.nameTextView.setText(qq); // ���Ϊ�գ���ʾqq����
		}
		// ����
		String content = (String)map.get("msgDetails");
		//���ݣ��������ݣ�
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // ������ʽ�������ж���Ϣ���Ƿ��б���
		try {
			SpannableString spannableString = 
					ExpressionUtil.getExpressionString(context, content, zhengze);
			holder.contentTextView.setText(spannableString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ʱ��
		String time = (String)map.get("msgTime");
		holder.timeTextView.setText(time);
		
		bm = ImageTool.getHeadImageFromLocalOrNet(context, (String)map.get("headimage"));
		// ����(������Ϣ��������ͷ��)
		int type = (Integer)map.get("msgType");
		if(type == MessageType.MSG_REQUESTADDFRIEND) {
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.addfriend_msg_icon_unchecked);; 
			holder.nameTextView.setText("ϵͳ��Ϣ");// ���ݺ���qq���ҵ���������
			if(name != null) {
				holder.contentTextView.setText(name + "���������Ϊ����"); // �����Ϊ�գ� ��ʾ����
			} else {
				holder.contentTextView.setText(qq + "���������Ϊ����"); // ���Ϊ�գ���ʾqq����
			}
		}
		bm = ImageTool.toRoundCorner(bm, 15);
		holder.headImage.setImageBitmap(bm);
		
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView headImage;
		private TextView nameTextView;
		private TextView contentTextView;
		private TextView timeTextView;
		private Button newMsgImageView;
	}

}
