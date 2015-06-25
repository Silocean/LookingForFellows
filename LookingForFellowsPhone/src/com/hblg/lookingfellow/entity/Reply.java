package com.hblg.lookingfellow.entity;

public class Reply {
	int replyId;
	String details;
	String time;
	String authorId;
	String authorName;
	int toPostId;
	public Reply() {
		
	}
	public Reply(int replyId, String details, String time, String authorId,
			String authorName, int toPostId) {
		this.replyId = replyId;
		this.details = details;
		this.time = time;
		this.authorId = authorId;
		this.authorName = authorName;
		this.toPostId = toPostId;
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
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public int getToPostId() {
		return toPostId;
	}
	public void setToPostId(int toPostId) {
		this.toPostId = toPostId;
	}
	@Override
	public String toString() {
		return "Reply [replyId=" + replyId + ", details=" + details + ", time="
				+ time + ", authorId=" + authorId + ", authorName="
				+ authorName + ", toPostId=" + toPostId + "]";
	}
	
	
}
