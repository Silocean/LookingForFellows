package com.hblg.lookingfellow.entity;

public interface MessageType {
	int MSG_CHAT = 1; // 聊天消息
	int MSG_REQUESTADDFRIEND = 2; // 请求加好友消息
	int MSG_AGREEADDFRIEND = 3; // 同意添加好友请求
	int MSG_REQUESTCHATMSG = 4; // 请求返回服务器暂存的聊天消息
	int MSG_EXIT = 5; // 用户下线消息
}
