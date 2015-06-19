package com.hblg.lookingfellow.entity;

public class Post {
	int id;
	String title;
	String details;
	String time;
	String authorId;
	String authorName;
	int replyNum;
	String imageName;
	
	public Post() {
	}

	public Post(int id, String title, String details, String time,
			String authorId, String authorName, String imageName) {
		this.id = id;
		this.title = title;
		this.details = details;
		this.time = time;
		this.authorId = authorId;
		this.authorName = authorName;
		this.imageName = imageName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", details=" + details
				+ ", time=" + time + ", authorId=" + authorId + ", authorName="
				+ authorName + ", replyNum=" + replyNum + ", imageName="
				+ imageName + "]";
	}

}
