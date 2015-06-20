package com.hblg.lookingfellow.model;
import java.util.HashMap;

public class ManageClientConnServer {
	private static HashMap<String, ClientConnServerThread> hm = new HashMap<String, ClientConnServerThread>();
	/**
	 * 把创建好的ClientConServerThread放入到hashmap中
	 * @param qq
	 * @param ccst
	 */
	public static void addClientConServerThread(String qq,ClientConnServerThread ccst){
		hm.put(qq, ccst);
	}
	/**
	 * 可以通过qq取得该线程
	 * @param qq
	 * @return
	 */
	public static ClientConnServerThread getClientConServerThread(String qq){
		return (ClientConnServerThread)hm.get(qq);
	}
	/**
	 * 删除客户端线程
	 * @param qq
	 */
	public static void removeClientConServerThread(String qq) {
		hm.remove(qq);
	}
	
}
