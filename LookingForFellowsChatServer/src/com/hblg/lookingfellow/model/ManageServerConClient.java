/**
 * 管理客户端连接的类
 */
package com.hblg.lookingfellow.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



public class ManageServerConClient {
	public static HashMap<String, ServerConnClientThread> hm = new HashMap<String, ServerConnClientThread>();
	
	//添加一个客户端通信线程
	public static void addClientThread(String qq, ServerConnClientThread cc){
		hm.put(qq,cc);
	}
	//得到一个客户端通信线程
	public static ServerConnClientThread getClientThread(String qq){
		return (ServerConnClientThread)hm.get(qq);
	}
	//删除一个客户端线程
	public static void removeClientThread(String qq) {
		hm.remove(qq);
	}
	//移除所有客户端线程
	public static void removeAllClientThread() {
		hm.clear();
	}
}
