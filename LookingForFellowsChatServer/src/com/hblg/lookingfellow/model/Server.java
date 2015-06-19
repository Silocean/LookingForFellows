package com.hblg.lookingfellow.model;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import com.hblg.lookingfellow.entity.LoginUser;

public class Server {
	
	public Server() {
		try {
			ServerSocket ss = new ServerSocket(3456);
			System.out.println("正在监听...");
			while(true) {
				Socket s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				LoginUser user = (LoginUser)ois.readObject();
				System.out.println(user.getQq() + "上线");
				//单开一个线程，让该线程与该客户端保持连接
				ServerConnClientThread scct = new ServerConnClientThread(s);
				// 启动该线程
				scct.start();
				//加入到管理类中
				ManageServerConClient.addClientThread(user.getQq(), scct);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
