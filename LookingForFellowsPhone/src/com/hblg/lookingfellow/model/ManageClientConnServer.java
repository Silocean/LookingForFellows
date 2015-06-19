package com.hblg.lookingfellow.model;
import java.util.HashMap;

public class ManageClientConnServer {
	private static HashMap<String, ClientConnServerThread> hm = new HashMap<String, ClientConnServerThread>();
	//把创建好的ClientConServerThread放入到hm
	public static void addClientConServerThread(String qq,ClientConnServerThread ccst){
		hm.put(qq, ccst);
	}
	
	//可以通过qq取得该线程
	public static ClientConnServerThread getClientConServerThread(String qq){
		return (ClientConnServerThread)hm.get(qq);
	}
}
