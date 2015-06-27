package com.hblg.lookingfellow.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.content.Context;

import com.hblg.lookingfellow.entity.LoginUser;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.entity.User;

public class ChatClient {
	Context context;
	Socket s;
	public ChatClient(Context context) {
		this.context = context;
		this.connectServer();
	}
	private void connectServer() {
		try {
			s = new Socket("192.168.1.152", 3456);
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			LoginUser user = new LoginUser();
			user.setQq(User.qq);
			user.setPassword(User.password);
			oos.writeObject(user);
			//����һ�����˺źͷ������������ӵ��߳�
			ClientConnServerThread ccst = new ClientConnServerThread(context, s);
			//�������߳�
			//ccst.start();
			//���뵽��������
			ManageClientConnServer.addClientConServerThread(User.qq, ccst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void disconnect() {
		if(s != null) {
			try {
				s.close();
				s = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
