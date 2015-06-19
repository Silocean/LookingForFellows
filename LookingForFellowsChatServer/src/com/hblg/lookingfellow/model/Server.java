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
			//System.out.println("���ڼ���...");
			MainFrame.ta.append("������������������3456�˿ڼ���......" + "\n");
			while(true) {
				Socket s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				LoginUser user = (LoginUser)ois.readObject();
				if(ManageServerConClient.getClientThread(user.getQq()) == null) {
					//System.out.println(user.getQq() + "����");
					MainFrame.ta.append("�û���" + user.getQq() + "����" + "\n");
					//����һ���̣߳��ø��߳���ÿͻ��˱�������
					ServerConnClientThread scct = new ServerConnClientThread(s);
					// �������߳�
					scct.start();
					//���뵽��������
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
