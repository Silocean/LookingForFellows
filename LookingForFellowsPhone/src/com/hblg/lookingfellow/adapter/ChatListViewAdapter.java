package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;


public class ChatListViewAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<Map<String, Object>> list;
	LayoutInflater inflater;
	ListView listView;
	ChatActivity activity;
	
	ViewHolder holder;
	
	Bitmap bitmap;
	
	PopupWindow popupWindow;
	View popupView;
	WindowManager windowManager;
	ClipboardManager clipboardManager;
	int[] location = new int[2];
	TextView copyTextView;
	TextView deletetTextView;
	
	public ChatListViewAdapter(Context context, ArrayList<Map<String, Object>> list, ListView listView, ChatActivity activity) {
		this.context = context;
		this.list = list;
		this.activity = activity;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		initPopupWindow();
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
		
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		
		System.out.println(position+"====="+list.get(position).get("msgSender"));
		
		if(list.get(position).get("msgSender").equals(User.qq)) {
			// ������Լ����ģ�����������ʾ�����
			convertView = inflater.inflate( R.layout.chat_item_msg_text_right, null);
			bitmap = ImageTool.getHeadImageFromLocalOrNet(context, User.qq);
		} else {
			// ����ǶԷ����ģ�����������ʾ���ұ�
			convertView = inflater.inflate( R.layout.chat_item_msg_text_left, null);
			bitmap = ImageTool.getHeadImageFromLocalOrNet(context, (String)list.get(position).get("msgSender"));
		}
		holder.headImage = (ImageView)convertView.findViewById(R.id.chat_item_headimage);
		holder.sendTimeTextView = (TextView)convertView.findViewById(R.id.chat_item_sendtime);
		holder.contentTextView = (TextView)convertView.findViewById(R.id.chat_item_chatcontent);
		//convertView.setTag(holder);
		
		
		// ID
		final int id = (Integer)map.get("msgId");
		
		// ʱ��
		String sendTime = (String)map.get("msgTime");
		holder.sendTimeTextView.setText(sendTime);
		
		// ����
		final String chatContent = (String)map.get("msgDetails");
		holder.contentTextView.setText(chatContent);
		holder.contentTextView.setOnLongClickListener(new OnLongClickListener() {
		
			@Override
			public boolean onLongClick(View v) {
				v.getLocationOnScreen(location); // ȡ�øÿؼ�����Ļ�е�λ��,���������location������
				showPopwindow(v, id, chatContent);
				return false;
			}
		});
		
		// ͷ��
		holder.headImage.setImageBitmap(bitmap);
		
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView headImage;
		private TextView sendTimeTextView;
		private TextView contentTextView;
	}
	private void showPopwindow(View v, final int id, final String chatContent) {
		if(popupWindow.isShowing()) {
			// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
			popupWindow.dismiss();
		} else {
			int x = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
			// ���õ���������ʾ��λ��
			popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, location[1]-popupWindow.getHeight());
			copyTextView = (TextView)popupView.findViewById(R.id.copyTextView);
			deletetTextView = (TextView)popupView.findViewById(R.id.deleteTextView);
			copyTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) { // �������ݵ�������
					clipboardManager.setText(chatContent);
				}
			});
			deletetTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) { // ����msgIdɾ����Ϣ
					SQLiteService service = new SQLiteService(context);
					service.deleteMsg(id);
					activity.updateChatView(); // �����������
					popupWindow.dismiss();
				}
			});
		}
	}
	private void initPopupWindow() {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.msg_popupwindow, null);
		popupWindow = new PopupWindow(popupView, 100, 50, false);
		// ���ô˲�����ý��㣬�����޷����
		popupWindow.setFocusable(true);
		// ��Ҫ����һ�´˲����������߿���ʧ 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //���õ��������ߴ�����ʧ  
		popupWindow.setOutsideTouchable(true);
		// ���ô��ڶ���Ч��
		//popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}

}
