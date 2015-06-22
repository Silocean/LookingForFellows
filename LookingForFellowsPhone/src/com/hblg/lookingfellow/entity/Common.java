package com.hblg.lookingfellow.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量类，用于保存整个程序会用到的变量
 * @author Silocean
 *
 */
public class Common {
	public static boolean newMsg = false; // 是否有未读消息到来
	public static boolean ListnewMsg = false; // msgList中的item是否未读
	public static List<String> msgSenders = new ArrayList<String>(); // 存放消息发送者的list
}
