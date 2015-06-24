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
 * ���߳����ڽ������Է���������Ϣ
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
				case MessageType.MSG_CHAT: // ������յ�����Ϣ��������Ϣ��ע�⣬�ȱ�����Ϣ�����أ�Ȼ���ٹ㲥��
					
					// ����Է������¼������
					new SQLiteService(context).saveMessage(msg);
					// �����ҵ���Ϣ�б�
					new SQLiteService(context).updateMyMessageList(msg.getSender());
					// ȫ�ֱ���:�Ƿ���δ����Ϣ����������Ϊtrue
					Common.newMsg = true;
					
					// �Ѵӷ�������õ���Ϣͨ���㲥����
					Intent intent = new Intent("com.hblg.lookingfellow.msg");
					intent.putExtra("msg", msg);
					context.sendBroadcast(intent);
					
					break;
				case MessageType.MSG_REQUESTADDFRIEND: // ������յ�����Ϣ������Ӻ�����Ϣ
					
					// ����������Ӻ�����Ϣ������
					new SQLiteService(context).saveRequestAddFriendMessage(msg);
					
					// �Ѵӷ�������õ���Ϣͨ���㲥����
					Intent intent2 = new Intent("com.hblg.lookingfellow.msg");
					intent2.putExtra("msg", msg);
					context.sendBroadcast(intent2);
					
					// ȫ�ֱ���:�Ƿ���δ����Ϣ����������Ϊtrue
					Common.newMsg = true;
					break;
				case MessageType.MSG_UNFRINEDMSG: // ����ǽ�����ѹ�ϵ��Ϣ
					
					// �ӱ���ɾ��������Ϣ
					new SQLiteService(context).deleteFriendInfo(msg.getSender());
					
					break;
				case MessageType.MSG_AGREEADDFRIEND: // ����յ�����Ϣ��ͬ����Ӻ�����Ϣ
					// ����ͬ����Ӻ�����Ϣ������
					new SQLiteService(context).saveMessage(msg);
					// �����ҵ���Ϣ�б�
					new SQLiteService(context).updateMyMessageList(msg.getSender());
					// ��Ӻ�����Ϣ������
					Friend friend = getFriend(msg.getSender());
					new SQLiteService(context).addFriend(friend);
					// ȫ�ֱ���:�Ƿ���δ����Ϣ����������Ϊtrue
					Common.newMsg = true;
					break;
				case MessageType.MSG_REQUESTRETURNCHATMSG: // ������յ����Ƿ������ݴ����Ϣ
					String str = msg.getDetails();
					System.out.println("���յ��������ݴ����Ϣ" + str);
					if(str.equals("]")) {
						// ��ʾû��δ����Ϣ
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
							// �����ҵ���Ϣ�б�
							new SQLiteService(context).updateMyMessageList(obj.getString("msgSender"));
							Common.msgSenders.add(obj.getString("msgSender"));
						}
						// ����������Ϣ������
						new SQLiteService(context).saveMessages(messages);
					}
					break;
				case MessageType.MSG_REQUESTRETURNADDFRIENDMSG: // ������յ����Ƿ������ݴ��������Ӻ�����Ϣ
					String str2 = msg.getDetails();
					System.out.println("���յ��������ݴ����Ӻ�����Ϣ" + str2);
					if(str2.equals("]")) {
						// ��ʾû��δ��������Ӻ�����Ϣ
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
						// ����������Ӻ�����Ϣ������
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
