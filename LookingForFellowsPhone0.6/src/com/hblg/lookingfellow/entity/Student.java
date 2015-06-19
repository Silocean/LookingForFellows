package com.hblg.lookingfellow.entity;

public class Student {
	
	private String qq;
	private String name;
	private String hometown;
	private String password;
	private String sex;
	private String signs;
	private String phone;
	
	public Student() {
		super();
	}
	
	public Student(String qq, String name, String hometown, String password,
			String sex, String signs, String phone) {
		this.qq = qq;
		this.name = name;
		this.hometown = hometown;
		this.password = password;
		this.sex = sex;
		this.signs = signs;
		this.phone = phone;
	}

	public Student(String qq, String name, String hometown, String password) {
		this.qq = qq;
		this.name = name;
		this.hometown = hometown;
		this.password = password;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return "Student [qq=" + qq + ", name=" + name + ", hometown="
				+ hometown + ", password=" + password + ", sex=" + sex
				+ ", signs=" + signs + ", phone=" + phone + "]";
	}

	
}
