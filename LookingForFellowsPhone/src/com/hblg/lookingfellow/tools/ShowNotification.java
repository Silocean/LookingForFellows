package com.hblg.lookingfellow.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
/**
 * ��ʾ֪ͨ��
 * @author Silocean
 *
 */
public class ShowNotification {
	
	private static Notification notification;
	private static NotificationManager manager;
	private static PendingIntent pendingIntent;
	
	public static void showNotification(Context context, Message msg) {
		String details = msg.getDetails();
		String sender = msg.getSender();
		notification = new Notification(android.R.drawable.stat_notify_chat, details, System.currentTimeMillis());
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra("friendQq", sender);
		pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, sender, details, pendingIntent);
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(100, notification);
	}
	
}