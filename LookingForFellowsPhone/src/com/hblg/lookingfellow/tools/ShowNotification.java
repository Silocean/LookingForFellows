package com.hblg.lookingfellow.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.RequestAddFriendMsgActivity;
/**
 * 显示通知类
 * @author Silocean
 *
 */
public class ShowNotification {
	
	private static Notification notification;
	private static NotificationManager manager;
	private static PendingIntent pendingIntent;
	/**
	 * 显示通知
	 * @param context 上下文对象
	 * @param msg 通知中包含的消息
	 */
	public static void showNotification(Context context, Message msg) {
		String details = msg.getDetails();
		String sender = msg.getSender();
		int type = msg.getType();
		Intent intent = null;
		notification = new Notification(android.R.drawable.stat_notify_chat, details, System.currentTimeMillis());
		if(type == MessageType.MSG_CHAT) {
			intent = new Intent(context, ChatActivity.class);
			intent.putExtra("friendQq", sender);
		} else if(type == MessageType.MSG_REQUESTADDFRIEND) {
			intent = new Intent(context, RequestAddFriendMsgActivity.class);
		}
		System.out.println("sender:"+intent.getStringExtra("friendQq"));
		// 这里注意第四个参数一定要是FLAG_UPDATE_CURRENT，否则传过去的intent仍旧是旧值
		pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, sender, details, pendingIntent);
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(100, notification);
	}
	
}
