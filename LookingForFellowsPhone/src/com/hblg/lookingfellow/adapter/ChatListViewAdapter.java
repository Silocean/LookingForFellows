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
			// 如果是自己发的，聊天内容显示在左边
			convertView = inflater.inflate( R.layout.chat_item_msg_text_right, null);
			bitmap = ImageTool.getHeadImageFromLocalOrNet(context, User.qq);
		} else {
			// 如果是对方发的，聊天内容显示在右边
			convertView = inflater.inflate( R.layout.chat_item_msg_text_left, null);
			bitmap = ImageTool.getHeadImageFromLocalOrNet(context, (String)list.get(position).get("msgSender"));
		}
		holder.headImage = (ImageView)convertView.findViewById(R.id.chat_item_headimage);
		holder.sendTimeTextView = (TextView)convertView.findViewById(R.id.chat_item_sendtime);
		holder.contentTextView = (TextView)convertView.findViewById(R.id.chat_item_chatcontent);
		//convertView.setTag(holder);
		
		
		// ID
		final int id = (Integer)map.get("msgId");
		
		// 时间
		String sendTime = (String)map.get("msgTime");
		holder.sendTimeTextView.setText(sendTime);
		
		// 内容
		final String chatContent = (String)map.get("msgDetails");
		holder.contentTextView.setText(chatContent);
		holder.contentTextView.setOnLongClickListener(new OnLongClickListener() {
		
			@Override
			public boolean onLongClick(View v) {
				v.getLocationOnScreen(location); // 取得该控件在屏幕中的位置,将坐标放在location数组中
				showPopwindow(v, id, chatContent);
				return false;
			}
		});
		
		// 头像
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
			// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏  
			popupWindow.dismiss();
		} else {
			int x = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
			// 设置弹出窗口显示的位置
			popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, location[1]-popupWindow.getHeight());
			copyTextView = (TextView)popupView.findViewById(R.id.copyTextView);
			deletetTextView = (TextView)popupView.findViewById(R.id.deleteTextView);
			copyTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) { // 复制内容到剪贴板
					clipboardManager.setText(chatContent);
				}
			});
			deletetTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) { // 根据msgId删除消息
					SQLiteService service = new SQLiteService(context);
					service.deleteMsg(id);
					activity.updateChatView(); // 更新聊天界面
					popupWindow.dismiss();
				}
			});
		}
	}
	private void initPopupWindow() {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.msg_popupwindow, null);
		popupWindow = new PopupWindow(popupView, 100, 50, false);
		// 设置此参数获得焦点，否则无法点击
		popupWindow.setFocusable(true);
		// 需要设置一下此参数，点击外边可消失 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失  
		popupWindow.setOutsideTouchable(true);
		// 设置窗口动画效果
		//popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}

}
