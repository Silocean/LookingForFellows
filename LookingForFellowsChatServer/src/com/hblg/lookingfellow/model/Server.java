package com.hblg.lookingfellow.model;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.hblg.lookingfellow.entity.LoginUser;
import com.hblg.lookingfellow.view.MainFrame;

public class Server {
	
	@SuppressWarnings("resource")
	public Server() {
		try {
			ServerSocket ss = new ServerSocket(3456);
			//System.out.println("正在监听...");
			MainFrame.ta.append("服务器已启动，正在3456端口监听......" + "\n");
			while(true) {
				Socket s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				LoginUser user = (LoginUser)ois.readObject();
				if(ManageServerConClient.getClientThread(user.getQq()) == null) {
					//System.out.println(user.getQq() + "上线");
					MainFrame.ta.append("用户：" + user.getQq() + "上线" + "\n");
					//单开一个线程，让该线程与该客户端保持连接
					ServerConnClientThread scct = new ServerConnClientThread(s);
					// 启动该线程
					scct.start();
					//加入到管理类中
					ManageServerConClient.addClientThread(user.getQq(), scct);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					dos.writeUTF("success");
				} else {
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					dos.writeUTF("error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
