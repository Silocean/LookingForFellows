package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.RequestAddFriendMsgActivity;
import com.hblg.lookingfellow.slidingmenu.fragment.MsgFragment;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class MsgListViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<Map<String, Object>> list;
	int resourceId;
	LayoutInflater inflater;
	ListView listView;
	MsgFragment msgFragment;
	RequestAddFriendMsgActivity requestAddFriendMsgActivity;
	
	Bitmap bm;
	
	private Point startPoint,endPoint;
	private boolean loadFinish=false; 
	private int mLastPosition = -1;
	
	public MsgListViewAdapter(Context context, ArrayList<Map<String, Object>> list, int resourceId, ListView listView, MsgFragment msgFragment) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		this.listView = listView;
		this.msgFragment = msgFragment;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		startPoint=new Point();
		endPoint=new Point();
	}
	
	public MsgListViewAdapter(Context context, ArrayList<Map<String, Object>> list, int resourceId, ListView listView, RequestAddFriendMsgActivity requestAddFriendMsgActivity) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		this.listView = listView;
		this.requestAddFriendMsgActivity = requestAddFriendMsgActivity;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		startPoint=new Point();
		endPoint=new Point();
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
		ViewHolder holder=null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(resourceId, null);
			holder.headImage = (ImageView)convertView.findViewById(R.id.msglayout_headimage);
			holder.nameTextView = (TextView)convertView.findViewById(R.id.msglayout_name);
			holder.contentTextView = (TextView)convertView.findViewById(R.id.msglayout_content);
			holder.timeTextView = (TextView)convertView.findViewById(R.id.msglayout_time);
			holder.newMsgImageView = (Button)convertView.findViewById(R.id.msglayout_newMsg);
			holder.relativeLayout=(RelativeLayout)convertView.findViewById(R.id.msg_rel);
			holder.deleteBtn=(Button)convertView.findViewById(R.id.msglayout_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)this.getItem(position);
		
		// 名字
		final String qq;
		if(User.qq.equals((String)map.get("msgSender"))) {
			qq = (String)map.get("msgReceiver");
		} else {
			qq = (String)map.get("msgSender");
		}
		// 名字
		String name = new SQLiteService(context).getFriendNameByQq(qq);
		if(name != null) {
			holder.nameTextView.setText(name); // 如果不为空， 显示名字
		} else {
			holder.nameTextView.setText(qq); // 如果为空，显示qq号码
		}
		// 内容
		String content = (String)map.get("msgDetails");
		holder.contentTextView.setText(content);
		// 时间
		String time = (String)map.get("msgTime");
		time = TimeConvertTool.calDateTime(time);
		holder.timeTextView.setText(time);
		
		bm = ImageTool.getHeadImageFromLocalOrNet(context, (String)map.get("headimage"));
		// 类型(根据消息类型设置头像)
		final int type = (Integer)map.get("msgType");
		if(type == MessageType.MSG_REQUESTADDFRIEND) {
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.addfriend_msg_icon_unchecked);; 
			holder.nameTextView.setText("系统消息");// 根据好友qq号找到好友名字
			if(name != null) {
				holder.contentTextView.setText(name + "请求添加你为好友"); // 如果不为空， 显示名字
			} else {
				holder.contentTextView.setText(qq + "请求添加你为好友"); // 如果为空，显示qq号码
			}
		}
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
		holder.deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(type == MessageType.MSG_REQUESTADDFRIEND) {
					// 从数据库中删除与该好友的所有聊天记录
					new SQLiteService(context).deleteRequestAddFriendMsg(User.qq, qq);
					requestAddFriendMsgActivity.updateListView();
				} else {
					// 把聊天记录移除出我的消息列表
					new SQLiteService(context).deleteFromMyMessageList(qq);
					// 从数据库中删除与该好友的所有聊天记录
					new SQLiteService(context).deleteMsg(User.qq, qq);
					msgFragment.updataListView();
				}
			}
		});
		holder.relativeLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(mLastPosition != -1){
					mLastPosition=-1;
					notifyDataSetChanged();
				} else {
					if(type == MessageType.MSG_CHAT) {
						Intent intent = new Intent(context, ChatActivity.class);
						// 防止 Calling startActivity() from outside of an Activity问题发生
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("friendQq", qq);
						context.startActivity(intent);
					} else if(type == MessageType.MSG_REQUESTADDFRIEND) {
						Intent intent = new Intent(context, FriendInfoActivity.class);
						// 防止 Calling startActivity() from outside of an Activity问题发生
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("qq", qq);
						intent.putExtra("tag", "agreeRequest");
						context.startActivity(intent);
					} else if(type == MessageType.MSG_AGREEADDFRIEND) {
						Intent intent = new Intent(context, ChatActivity.class);
						// 防止 Calling startActivity() from outside of an Activity问题发生
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("friendQq", qq);
						context.startActivity(intent);
					}
				}
			}
		});
		
		if(position == mLastPosition) {  
			holder.deleteBtn.setVisibility(View.VISIBLE);  
	    } else {  
	        holder.deleteBtn.setVisibility(View.GONE);  
	    }
		
		final int finalPosition=position;
		holder.relativeLayout.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					loadFinish=false;
					
					Log.v("Gesture", "down"+finalPosition);
					startPoint.set((int)event.getX(),(int)event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					Log.v("Gesture", "move");
					endPoint.set((int)event.getX(), (int)event.getY());
					if(Math.abs(endPoint.x-startPoint.x)>30){
						if(loadFinish==false){
							loadFinish=true;
							
							if(finalPosition != mLastPosition) {  
					            mLastPosition = finalPosition;  
					        } else {  
					            mLastPosition = -1;  
					        }  
					        notifyDataSetChanged();  
							  
							Log.v("Gesture", "move success"+finalPosition);
						}else{
							return true;
						}
						return true ;
					}
					if(Math.abs(endPoint.y-startPoint.y)>30){
						return false;
					}
					break;
				case MotionEvent.ACTION_UP:
					Log.v("Gesture", "up"+finalPosition);
					break;
				}
				return false;
			}
		});
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView headImage;
		private TextView nameTextView;
		private TextView contentTextView;
		private TextView timeTextView;
		@SuppressWarnings("unused")
		private Button newMsgImageView;
		private RelativeLayout relativeLayout;
		private Button deleteBtn;
	}

}
