package com.hblg.lookingfellow.model;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.sqlite.SQLiteService;
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
				case MessageType.MSG_CHAT: // ������յ�����Ϣ��������Ϣ
					
					System.out.println(msg);
					// �Ѵӷ�������õ���Ϣͨ���㲥����
					Intent intent = new Intent("com.hblg.lookingfellow.msg");
					intent.putExtra("msg", msg);
					context.sendBroadcast(intent);
					
					// ����Է������¼������
					SQLiteService service = new SQLiteService(context);
					service.saveMessage(msg);
					
					break;
				case MessageType.MSG_REQUESTADDFRIEND: // ������յ�����Ϣ�ǼӺ�����Ϣ
					
					break;
				case MessageType.MSG_REQUESTCHATMSG: // ������յ����Ƿ������ݴ����Ϣ
					
					String msgStr = msg.getDetails();
					
					if(msgStr.equals("]")) {
						// ��ʾû��δ����Ϣ
					} else {
						ArrayList<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
						JSONArray array = new JSONArray(msgStr);
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
						}
						// ����������Ϣ������
						SQLiteService service2 = new SQLiteService(context);
						service2.saveMessage(messages);
						
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
}
