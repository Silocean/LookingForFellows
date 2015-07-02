package com.hblg.lookingfellow.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	
	int type; //��Ϣ����
	String details; // ��Ϣ����
	String sender; // ��Ϣ������
	String receiver; // ��Ϣ������
	String time; // ��Ϣ����ʱ��
	
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
