package com.hblg.lookingfellow.entity;

public class User {
	private String qq;
	private String name;
	private String hometown;
	
	public User() {
		super();
	}

	public User(String qq, String name, String hometown) {
		this.qq = qq;
		this.name = name;
		this.hometown = hometown;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	@Override
	public String toString() {
		return "User [qq=" + qq + ", name=" + name + ", hometown=" + hometown
				+ "]";
	}
	
}
