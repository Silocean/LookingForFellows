package com.hblg.lookingfellow.model;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.Friend;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.StreamTool;
/**
 * 此线程用于接收来自服务器的消息
 * @author Silocean
 *
 */
public class ClientConnServerThread extends Thread {
	
	Context context;
	Socket s;
	public boolean flag = true;
	
	public ClientConnServerThread(Context context, Socket s) {
		this.context = context;
		this.s = s;
	}
	
	public Socket getS() {
		return s;
	}

	@Override
	public void run() {
		while(flag) {
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message msg = (Message)ois.readObject();
				int type = msg.getType();
				switch (type) {
				case MessageType.MSG_CHAT: // 如果接收到的消息是聊天信息（注意，先保存消息到本地，然后再广播）
					
					// 保存对方聊天记录到本地
					new SQLiteService(context).saveMessage(msg);
					// 更新我的消息列表
					new SQLiteService(context).updateMyMessageList(msg.getSender());
					// 全局变量:是否有未读消息到来，设置为true
					Common.newMsg = true;
					
					// 把从服务器获得的消息通过广播发送
					Intent intent = new Intent("com.hblg.lookingfellow.msg");
					intent.putExtra("msg", msg);
					context.sendBroadcast(intent);
					
					break;
				case MessageType.MSG_REQUESTADDFRIEND: // 如果接收到的消息是请求加好友信息
					
					// 保存请求添加好友消息到本地
					new SQLiteService(context).saveRequestAddFriendMessage(msg);
					
					// 把从服务器获得的消息通过广播发送
					Intent intent2 = new Intent("com.hblg.lookingfellow.msg");
					intent2.putExtra("msg", msg);
					context.sendBroadcast(intent2);
					
					// 全局变量:是否有未读消息到来，设置为true
					Common.newMsg = true;
					break;
				case MessageType.MSG_UNFRINEDMSG: // 如果是解除好友关系消息
					
					// 从本地删除好友信息
					new SQLiteService(context).deleteFriendInfo(msg.getSender());
					
					break;
				case MessageType.MSG_AGREEADDFRIEND: // 如果收到的消息是同意添加好友消息
					// 保存同意添加好友消息到本地
					new SQLiteService(context).saveMessage(msg);
					// 更新我的消息列表
					new SQLiteService(context).updateMyMessageList(msg.getSender());
					// 添加好友信息到本地
					Friend friend = getFriend(msg.getSender());
					new SQLiteService(context).addFriend(friend);
					// 全局变量:是否有未读消息到来，设置为true
					Common.newMsg = true;
					break;
				case MessageType.MSG_REQUESTRETURNCHATMSG: // 如果接收到的是服务器暂存的消息
					String str = msg.getDetails();
					System.out.println("接收到服务器暂存的信息" + str);
					if(str.equals("]")) {
						// 表示没有未读消息
					} else {
						ArrayList<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
						JSONArray array = new JSONArray(str);
						for(int i=0; i<array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("msgType", obj.getInt("msgType"));
							map.put("msgSender", obj.getString("msgSender"));
							map.put("msgReceiver",obj.getString("msgReceiver"));
							map.put("msgDetails", obj.getString("msgDetails"));
							map.put("msgTime", obj.getString("msgTime"));
							messages.add(map);
							// 更新我的消息列表
							new SQLiteService(context).updateMyMessageList(obj.getString("msgSender"));
							Common.msgSenders.add(obj.getString("msgSender"));
						}
						// 保存聊天信息到本地
						new SQLiteService(context).saveMessages(messages);
					}
					break;
				case MessageType.MSG_REQUESTRETURNADDFRIENDMSG: // 如果接收到的是服务器暂存的请求添加好友消息
					String str2 = msg.getDetails();
					System.out.println("接收到服务器暂存的添加好友信息" + str2);
					if(str2.equals("]")) {
						// 表示没有未读请求添加好友消息
					} else {
						ArrayList<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
						JSONArray array = new JSONArray(str2);
						for(int i=0; i<array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("msgType", obj.getInt("msgType"));
							map.put("msgSender", obj.getString("msgSender"));
							map.put("msgReceiver",obj.getString("msgReceiver"));
							map.put("msgDetails", obj.getString("msgDetails"));
							map.put("msgTime", obj.getString("msgTime"));
							messages.add(map);
						}
						// 保存请求添加好友信息到本地
						new SQLiteService(context).saveRequestAddFriendMessages(messages);
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Friend getFriend(String qq) {
		try {
			String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/GetUserInfoServlet?qq=";
			path = path + qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] data = StreamTool.read(in);
				String json = new String(data, "utf-8");
				Friend friend = parseJson(json);
				return friend;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private Friend parseJson(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		Friend friend = new Friend();
		friend.setQq(obj.getString("stuQQ"));
		friend.setName(obj.getString("stuName"));
		friend.setHometown(obj.getString("stuHometown"));
		friend.setSex(obj.getString("stuSex"));
		friend.setSigns(obj.getString("stuSigns"));
		friend.setPhone(obj.getString("stuPhone"));
		return friend;
	}
}
