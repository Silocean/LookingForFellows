/**
 * ����ͻ������ӵ���
 */
package com.hblg.lookingfellow.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



public class ManageServerConClient {
	public static HashMap<String, ServerConnClientThread> hm = new HashMap<String, ServerConnClientThread>();
	
	//���һ���ͻ���ͨ���߳�
	public static void addClientThread(String qq, ServerConnClientThread cc){
		hm.put(qq,cc);
	}
	//�õ�һ���ͻ���ͨ���߳�
	public static ServerConnClientThread getClientThread(String qq){
		return (ServerConnClientThread)hm.get(qq);
	}
	//ɾ��һ���ͻ����߳�
	public static void removeClientThread(String qq) {
		hm.remove(qq);
	}
	//�Ƴ����пͻ����߳�
	public static void removeAllClientThread() {
		hm.clear();
	}
}
