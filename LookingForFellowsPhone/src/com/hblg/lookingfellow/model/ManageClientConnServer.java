package com.hblg.lookingfellow.model;
import java.util.HashMap;

public class ManageClientConnServer {
	private static HashMap<String, ClientConnServerThread> hm = new HashMap<String, ClientConnServerThread>();
	/**
	 * �Ѵ����õ�ClientConServerThread���뵽hashmap��
	 * @param qq
	 * @param ccst
	 */
	public static void addClientConServerThread(String qq,ClientConnServerThread ccst){
		hm.put(qq, ccst);
	}
	/**
	 * ����ͨ��qqȡ�ø��߳�
	 * @param qq
	 * @return
	 */
	public static ClientConnServerThread getClientConServerThread(String qq){
		return (ClientConnServerThread)hm.get(qq);
	}
	/**
	 * ɾ���ͻ����߳�
	 * @param qq
	 */
	public static void removeClientConServerThread(String qq) {
		hm.remove(qq);
	}
	
}
