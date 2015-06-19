package com.hblg.lookingfellow.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hblg.lookingfellow.dao.FriendDAO;
import com.hblg.lookingfellow.dao.MessageDAO;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.view.MainFrame;

public class ServerConnClientThread extends Thread {
	
	Socket s;
	boolean flag = true;
	
	public ServerConnClientThread(Socket s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		while(flag) {
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message msg = (Message)ois.readObject();
				int type = msg.getType();
				switch (type) {
				case MessageType.MSG_CHAT: // �����������Ϣ����ת����������
					//System.out.println(msg);
					ServerConnClientThread scct = ManageServerConClient.getClientThread(msg.getReceiver());
					if(scct == null) { // ������δ��¼������Ϣ�ݴ���web���ݿ���
						MessageDAO dao = new MessageDAO();
						dao.saveMessage(msg);
						//System.out.println("�ݴ���Ϣ��������");
						MainFrame.ta.append("�ݴ���Ϣ��������" + "\n");
					} else { // ���������ߣ�ֱ�Ӱ���Ϣת������
						ObjectOutputStream oos = new ObjectOutputStream(scct.s.getOutputStream());
						oos.writeObject(msg);
						//System.out.println("ֱ��ת����������");
						MainFrame.ta.append("ֱ��ת����������" + "\n");
					}
					break;
				case MessageType.MSG_REQUESTADDFRIEND: // �������Ӻ�����Ϣ��Ҳת����������
					ServerConnClientThread scct2 = ManageServerConClient.getClientThread(msg.getReceiver());
					ArrayList<String> friends = new FriendDAO().getMyFriends(msg.getSender());
					boolean flag = false;
					for(int i=0; i<friends.size(); i++) {
						if(msg.getReceiver().equals(friends.get(i))) {
							flag = true; // ��ʾ�������������ߵĺ���
						}
					}
					if(!flag) { // ���˫�������Ǻ��ѹ�ϵ
						if(scct2 == null) { // ������δ��¼������Ϣ�ݴ���web���ݿ���
							MessageDAO dao = new MessageDAO();
							dao.saveRequestAddFriendMessage(msg);
							//System.out.println("�ݴ�������Ӻ�����Ϣ��������");
							MainFrame.ta.append("�ݴ�������Ӻ�����Ϣ��������" + "\n");
						} else { // ���������ߣ�ֱ�Ӱ���Ϣת������
							ObjectOutputStream oos = new ObjectOutputStream(scct2.s.getOutputStream());
							oos.writeObject(msg);
							//System.out.println("ֱ�Ӱ�������Ӻ�����Ϣת�����Է�");
							MainFrame.ta.append("ֱ�Ӱ�������Ӻ�����Ϣת�����Է�" + "\n");
						}
					} else {
						//System.out.println("�������Ǻ�����");
						MainFrame.ta.append("�������Ǻ�����" + "\n");
					}
					break;
				case MessageType.MSG_AGREEADDFRIEND: // �����ͬ������������
					ServerConnClientThread scct3 = ManageServerConClient.getClientThread(msg.getReceiver());
					ArrayList<String> friends2 = new FriendDAO().getMyFriends(msg.getSender());
					boolean flag2 = false;
					for(int i=0; i<friends2.size(); i++) {
						if(msg.getReceiver().equals(friends2.get(i))) {
							flag2 = true; // ��ʾ�������������ߵĺ���
						}
					}
					if(!flag2) { // ���˫�������Ǻ��ѹ�ϵ
						if(scct3 == null) { // ������δ��¼������Ϣ�ݴ���web���ݿ���
							MessageDAO dao = new MessageDAO();
							dao.saveMessage(msg);
							//System.out.println("�ݴ�ͬ����Ӻ�����Ϣ��������");
							MainFrame.ta.append("�ݴ�ͬ����Ӻ�����Ϣ��������" + "\n");
						} else { // ���������ߣ�ֱ�Ӱ���Ϣת������
							ObjectOutputStream oos = new ObjectOutputStream(scct3.s.getOutputStream());
							oos.writeObject(msg);
							//System.out.println("ֱ�Ӱ�ͬ����Ӻ�����Ϣת�����Է�");
							MainFrame.ta.append("ֱ�Ӱ�ͬ����Ӻ�����Ϣת�����Է�" + "\n");
						}
						// �Ѻ��ѹ�ϵ�����ڷ����������ݿ���
						new FriendDAO().addFriend(msg.getSender(), msg.getReceiver());
					} else {
						//System.out.println("�������Ǻ�����");
						MainFrame.ta.append("�������Ǻ�����" + "\n");
					}
					break;
				case MessageType.MSG_REQUESTRETURNCHATMSG: // ��������󷵻ط������ݴ���Ϣ����Ϣ��������ݿ���ȡ��������
					ServerConnClientThread scct4 = ManageServerConClient.getClientThread(msg.getSender());
					if(scct4 == null) {
						//System.out.println("δ֪����");
						MainFrame.ta.append("δ֪����" + "\n");
					} else {
						ObjectOutputStream oos = new ObjectOutputStream(scct4.s.getOutputStream());
						ArrayList<Map<String, Object>> list = new MessageDAO().getMessages(msg.getSender());
						String str = this.constructJson(list);
						Message message = new Message();
						message.setType(MessageType.MSG_REQUESTRETURNCHATMSG);
						message.setDetails(str);
						oos.writeObject(message);
						// ������Ϣ��ɾ�����ݿ����ݴ����Ϣ��¼
						new MessageDAO().deleteMessages(msg.getSender());
					}
					break;
				case MessageType.MSG_REQUESTRETURNADDFRIENDMSG: // ��������󷵻ط������ݴ��������Ӻ�����Ϣ��������ݿ���ȡ��������
					ServerConnClientThread scct5 = ManageServerConClient.getClientThread(msg.getSender());
					if(scct5 == null) {
						//System.out.println("δ֪����");
						MainFrame.ta.append("δ֪����" + "\n");
					} else {
						ObjectOutputStream dos = new ObjectOutputStream(scct5.s.getOutputStream());
						ArrayList<Map<String, Object>> list = new MessageDAO().getRequestAddFriendMessages(msg.getSender());
						String str = this.constructJson(list);
						Message message = new Message();
						message.setType(MessageType.MSG_REQUESTRETURNADDFRIENDMSG);
						message.setDetails(str);
						dos.writeObject(message);
						// ������Ϣ��ɾ�����ݿ����ݴ����Ϣ��¼
						new MessageDAO().deleteRequestAddFriendMessages(msg.getSender());
					}
					break;
				case MessageType.MSG_EXIT: // ������û����ߵ���Ϣ
					ServerConnClientThread scct6 = ManageServerConClient.getClientThread(msg.getSender());
					scct6.flag = false; //   ֹͣ�߳�
					ManageServerConClient.removeClientThread(msg.getSender()); // ���̴߳��̳߳����Ƴ�
					//System.out.println("�û�:" + msg.getSender() + "����");
					MainFrame.ta.append("�û���" + msg.getSender() + "����" + "\n");
					break;
				case MessageType.MSG_UNFRINEDMSG: // ����ǽ�����ѹ�ϵ��Ϣ
					ServerConnClientThread scct7 = ManageServerConClient.getClientThread(msg.getReceiver());
					if(scct7 == null) { // ������δ��¼������Ϣ�ݴ���web���ݿ���
						MessageDAO dao = new MessageDAO();
						dao.saveunfriendriendMessage(msg);
						//System.out.println("�ݴ���������Ϣ��������");
						MainFrame.ta.append("�ݴ���������Ϣ��������" + "\n");
					} else { // ���������ߣ�ֱ�Ӱ���Ϣת������
						ObjectOutputStream oos = new ObjectOutputStream(scct7.s.getOutputStream());
						oos.writeObject(msg);
						//System.out.println("ֱ�Ӱѽ��������Ϣת�����Է�");
						MainFrame.ta.append("ֱ�Ӱѽ��������Ϣת�����Է�" + "\n");
					}
					// �ӷ�������ɾ�����ѹ�ϵ
					new FriendDAO().deleteFriend(msg.getSender(), msg.getReceiver());
					break;
				default:
					break;
				}
			} catch (Exception e) {
				Iterator<String> it = ManageServerConClient.hm.keySet().iterator();
				while(it.hasNext()) {
					ManageServerConClient.getClientThread(it.next()).flag = false; // ֹͣ�������������߳�
				}
				ManageServerConClient.removeAllClientThread();// ���̳߳����Ƴ������߳�
				//System.out.println("�����û���������");
				MainFrame.ta.append("�����û���������" + "\n");
				e.printStackTrace();
			}
		}
	}
	/**
	 * ����json��ʽ����
	 * @param messages
	 * @return
	 */
	private String constructJson(List<Map<String, Object>> messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<messages.size(); i++) {
			Map<String, Object> map = messages.get(i);
			sb.append("{");
			for(Map.Entry<String, Object> entry : map.entrySet())  {
				sb.append(entry.getKey());
				sb.append(":\"");
				if(entry.getValue() == null) {
					sb.append("");
				} else {
					sb.append(entry.getValue());
				}
				sb.append("\",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
}
