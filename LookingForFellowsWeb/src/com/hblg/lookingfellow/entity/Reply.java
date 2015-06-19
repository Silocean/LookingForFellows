package com.hblg.lookingfellow.entity;

public class Reply {
	int replyId;
	String details;
	String time;
	String fromId;
	String toId;
	String fromName;
	String toName;
	int postId;
	public Reply() {
		
	}
	public Reply(int replyId, String details, String time, String fromId,
			String toId, String fromName, String toName, int postId) {
		this.replyId = replyId;
		this.details = details;
		this.time = time;
		this.fromId = fromId;
		this.toId = toId;
		this.fromName = fromName;
		this.toName = toName;
		this.postId = postId;
	}
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	@Override
	public String toString() {
		return "Reply [replyId=" + replyId + ", details=" + details + ", time="
				+ time + ", fromId=" + fromId + ", toId=" + toId
				+ ", fromName=" + fromName + ", toName=" + toName + ", postId="
				+ postId + "]";
	}
	
	
}
