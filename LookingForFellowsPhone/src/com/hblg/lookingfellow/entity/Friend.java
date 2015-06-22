package com.hblg.lookingfellow.entity;

public class Friend {
	
	private String qq;
	private String name;
	private String hometown;
	private String sex;
	private String signs;
	private String phone;
	
	public Friend() {
		
	}
	
	public Friend(String qq, String name, String hometown, 
			String sex, String signs, String phone) {
		this.qq = qq;
		this.name = name;
		this.hometown = hometown;
		this.sex = sex;
		this.signs = signs;
		this.phone = phone;
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
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSigns() {
		return signs;
	}

	public void setSigns(String signs) {
		this.signs = signs;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Friend [qq=" + qq + ", name=" + name + ", hometown="
				+ hometown + ", sex=" + sex
				+ ", signs=" + signs + ", phone=" + phone + "]";
	}

	
}
