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
			System.out.println("���ڼ���...");
			while(true) {
				Socket s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				LoginUser user = (LoginUser)ois.readObject();
				System.out.println(user.getQq() + "����");
				//����һ���̣߳��ø��߳���ÿͻ��˱�������
				ServerConnClientThread scct = new ServerConnClientThread(s);
				// �������߳�
				scct.start();
				//���뵽��������
				ManageServerConClient.addClientThread(user.getQq(), scct);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
