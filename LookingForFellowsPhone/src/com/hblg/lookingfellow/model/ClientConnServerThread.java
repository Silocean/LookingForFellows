package com.hblg.lookingfellow.model;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
/**
 * 此线程用于接收来自服务器的消息
 * @author Silocean
 *
 */
public class ClientConnServerThread extends Thread {
	
	Context context;
	Socket s;
	
	public ClientConnServerThread(Context context, Socket s) {
		this.context = context;
		this.s = s;
	}
	
	public Socket getS() {
		return s;
	}

	@Override
	public void run() {
		while(true) {
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message msg = (Message)ois.readObject();
				int type = msg.getType();
				switch (type) {
				case MessageType.MSG_CHAT: // 如果接收到的消息是聊天信息
					System.out.println(msg);
					String sender = msg.getSender();
					String details = msg.getDetails();
					String time = msg.getTime();
					Notification notification = new Notification(android.R.drawable.stat_notify_chat, details, System.currentTimeMillis());
					Intent intent = new Intent(context, ChatActivity.class);
			    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			    	notification.setLatestEventInfo(context, sender, details, pendingIntent);
			    	notification.defaults = Notification.DEFAULT_SOUND;
			    	notification.flags = Notification.FLAG_AUTO_CANCEL;
			    	NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			    	manager.notify(100, notification);
					break;
				case MessageType.MSG_REQUESTADDFRIEND: // 如果接收到的消息是加好友信息
					
					break;
				case MessageType.MSG_REQUESTCHATMSG: // 如果接收到的是服务器暂存的消息
					String msgStr = msg.getDetails();
					
					ArrayList<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
					JSONArray array = new JSONArray(msgStr);
					for(int i=0; i<array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("msgType", obj.getInt("msgType"));
						map.put("msgSender", obj.getString("msgSender"));
						map.put("msgSenderName", obj.getString("msgSenderName"));
						map.put("msgReceiver",obj.getString("msgReceiver"));
						map.put("msgDetails", obj.getString("msgDetails"));
						map.put("msgTime", obj.getString("msgTime"));
						messages.add(map);
					}
					// 保存聊天信息到本地
					SQLiteService service = new SQLiteService(context);
					service.saveMessage(messages);
					
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
