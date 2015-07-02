package com.hblg.lookingfellow.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	
	int type; //消息类型
	String details; // 消息内容
	String sender; // 消息发送者
	String receiver; // 消息接收者
	String time; // 消息发送时间
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Message [type=" + type + ", details=" + details + ", sender="
				+ sender + ", reveiver=" + receiver + ", time=" + time + "]";
	}
	
}
