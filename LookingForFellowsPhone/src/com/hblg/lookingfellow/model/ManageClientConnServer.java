package com.hblg.lookingfellow.model;
import java.util.HashMap;

public class ManageClientConnServer {
	private static HashMap<String, ClientConnServerThread> hm = new HashMap<String, ClientConnServerThread>();
	//�Ѵ����õ�ClientConServerThread���뵽hm
	public static void addClientConServerThread(String qq,ClientConnServerThread ccst){
		hm.put(qq, ccst);
	}
	
	//����ͨ��qqȡ�ø��߳�
	public static ClientConnServerThread getClientConServerThread(String qq){
		return (ClientConnServerThread)hm.get(qq);
	}
}
