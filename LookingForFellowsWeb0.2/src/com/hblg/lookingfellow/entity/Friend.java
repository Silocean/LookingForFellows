package com.hblg.lookingfellow.entity;

public class Friend {
	String userQq;
	String friendQq;
	public String getUserQq() {
		return userQq;
	}
	public void setUserQq(String userQq) {
		this.userQq = userQq;
	}
	public String getFriendQq() {
		return friendQq;
	}
	public void setFriendQq(String friendQq) {
		this.friendQq = friendQq;
	}
	@Override
	public String toString() {
		return "Friend [userQq=" + userQq + ", friendQq=" + friendQq + "]";
	}
	
}
