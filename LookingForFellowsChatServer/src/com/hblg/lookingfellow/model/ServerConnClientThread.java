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
				case MessageType.MSG_CHAT: // 如果是聊天信息，则转发给接收者
					//System.out.println(msg);
					ServerConnClientThread scct = ManageServerConClient.getClientThread(msg.getReceiver());
					if(scct == null) { // 接收者未登录，把消息暂存在web数据库中
						MessageDAO dao = new MessageDAO();
						dao.saveMessage(msg);
						//System.out.println("暂存消息到服务器");
						MainFrame.ta.append("暂存消息到服务器" + "\n");
					} else { // 接收者在线，直接把消息转发即可
						ObjectOutputStream oos = new ObjectOutputStream(scct.s.getOutputStream());
						oos.writeObject(msg);
						//System.out.println("直接转发给接收者");
						MainFrame.ta.append("直接转发给接收者" + "\n");
					}
					break;
				case MessageType.MSG_REQUESTADDFRIEND: // 如果是添加好友信息，也转发给接收者
					ServerConnClientThread scct2 = ManageServerConClient.getClientThread(msg.getReceiver());
					ArrayList<String> friends = new FriendDAO().getMyFriends(msg.getSender());
					boolean flag = false;
					for(int i=0; i<friends.size(); i++) {
						if(msg.getReceiver().equals(friends.get(i))) {
							flag = true; // 表示此人已是请求者的好友
						}
					}
					if(!flag) { // 如果双方还不是好友关系
						if(scct2 == null) { // 接收者未登录，把消息暂存在web数据库中
							MessageDAO dao = new MessageDAO();
							dao.saveRequestAddFriendMessage(msg);
							//System.out.println("暂存请求添加好友消息到服务器");
							MainFrame.ta.append("暂存请求添加好友消息到服务器" + "\n");
						} else { // 接收者在线，直接把消息转发即可
							ObjectOutputStream oos = new ObjectOutputStream(scct2.s.getOutputStream());
							oos.writeObject(msg);
							//System.out.println("直接把请求添加好友消息转发给对方");
							MainFrame.ta.append("直接把请求添加好友消息转发给对方" + "\n");
						}
					} else {
						//System.out.println("你俩已是好友了");
						MainFrame.ta.append("你俩已是好友了" + "\n");
					}
					break;
				case MessageType.MSG_AGREEADDFRIEND: // 如果是同意好友添加请求
					ServerConnClientThread scct3 = ManageServerConClient.getClientThread(msg.getReceiver());
					ArrayList<String> friends2 = new FriendDAO().getMyFriends(msg.getSender());
					boolean flag2 = false;
					for(int i=0; i<friends2.size(); i++) {
						if(msg.getReceiver().equals(friends2.get(i))) {
							flag2 = true; // 表示此人已是请求者的好友
						}
					}
					if(!flag2) { // 如果双方还不是好友关系
						if(scct3 == null) { // 接收者未登录，把消息暂存在web数据库中
							MessageDAO dao = new MessageDAO();
							dao.saveMessage(msg);
							//System.out.println("暂存同意添加好友消息到服务器");
							MainFrame.ta.append("暂存同意添加好友消息到服务器" + "\n");
						} else { // 接收者在线，直接把消息转发即可
							ObjectOutputStream oos = new ObjectOutputStream(scct3.s.getOutputStream());
							oos.writeObject(msg);
							//System.out.println("直接把同意添加好友消息转发给对方");
							MainFrame.ta.append("直接把同意添加好友消息转发给对方" + "\n");
						}
						// 把好友关系保存在服务器端数据库中
						new FriendDAO().addFriend(msg.getSender(), msg.getReceiver());
					} else {
						//System.out.println("你俩已是好友了");
						MainFrame.ta.append("你俩已是好友了" + "\n");
					}
					break;
				case MessageType.MSG_REQUESTRETURNCHATMSG: // 如果是请求返回服务器暂存信息的消息，则从数据库中取出并返回
					ServerConnClientThread scct4 = ManageServerConClient.getClientThread(msg.getSender());
					if(scct4 == null) {
						//System.out.println("未知错误");
						MainFrame.ta.append("未知错误" + "\n");
					} else {
						ObjectOutputStream oos = new ObjectOutputStream(scct4.s.getOutputStream());
						ArrayList<Map<String, Object>> list = new MessageDAO().getMessages(msg.getSender());
						String str = this.constructJson(list);
						Message message = new Message();
						message.setType(MessageType.MSG_REQUESTRETURNCHATMSG);
						message.setDetails(str);
						oos.writeObject(message);
						// 发出消息后，删除数据库中暂存的消息记录
						new MessageDAO().deleteMessages(msg.getSender());
					}
					break;
				case MessageType.MSG_REQUESTRETURNADDFRIENDMSG: // 如果是请求返回服务器暂存的请求添加好友消息，则从数据库中取出并返回
					ServerConnClientThread scct5 = ManageServerConClient.getClientThread(msg.getSender());
					if(scct5 == null) {
						//System.out.println("未知错误");
						MainFrame.ta.append("未知错误" + "\n");
					} else {
						ObjectOutputStream dos = new ObjectOutputStream(scct5.s.getOutputStream());
						ArrayList<Map<String, Object>> list = new MessageDAO().getRequestAddFriendMessages(msg.getSender());
						String str = this.constructJson(list);
						Message message = new Message();
						message.setType(MessageType.MSG_REQUESTRETURNADDFRIENDMSG);
						message.setDetails(str);
						dos.writeObject(message);
						// 发出消息后，删除数据库中暂存的消息记录
						new MessageDAO().deleteRequestAddFriendMessages(msg.getSender());
					}
					break;
				case MessageType.MSG_EXIT: // 如果是用户下线的消息
					ServerConnClientThread scct6 = ManageServerConClient.getClientThread(msg.getSender());
					scct6.flag = false; //   停止线程
					ManageServerConClient.removeClientThread(msg.getSender()); // 将线程从线程池中移除
					//System.out.println("用户:" + msg.getSender() + "下线");
					MainFrame.ta.append("用户：" + msg.getSender() + "下线" + "\n");
					break;
				case MessageType.MSG_UNFRINEDMSG: // 如果是解除好友关系消息
					ServerConnClientThread scct7 = ManageServerConClient.getClientThread(msg.getReceiver());
					if(scct7 == null) { // 接收者未登录，把消息暂存在web数据库中
						MessageDAO dao = new MessageDAO();
						dao.saveunfriendriendMessage(msg);
						//System.out.println("暂存解除好友消息到服务器");
						MainFrame.ta.append("暂存解除好友消息到服务器" + "\n");
					} else { // 接收者在线，直接把消息转发即可
						ObjectOutputStream oos = new ObjectOutputStream(scct7.s.getOutputStream());
						oos.writeObject(msg);
						//System.out.println("直接把解除好友消息转发给对方");
						MainFrame.ta.append("直接把解除好友消息转发给对方" + "\n");
					}
					// 从服务器端删除好友关系
					new FriendDAO().deleteFriend(msg.getSender(), msg.getReceiver());
					break;
				default:
					break;
				}
			} catch (Exception e) {
				Iterator<String> it = ManageServerConClient.hm.keySet().iterator();
				while(it.hasNext()) {
					ManageServerConClient.getClientThread(it.next()).flag = false; // 停止服务器端所有线程
				}
				ManageServerConClient.removeAllClientThread();// 从线程池中移除所有线程
				//System.out.println("所有用户被迫下线");
				MainFrame.ta.append("所有用户被迫下线" + "\n");
				e.printStackTrace();
			}
		}
	}
	/**
	 * 构造json格式数据
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
