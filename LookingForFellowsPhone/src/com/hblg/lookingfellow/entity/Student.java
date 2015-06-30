package com.hblg.lookingfellow.entity;

public class Student {
	
	private String qq;
	private String name;
	private String province;
	private String city;
	private String password;
	private String sex;
	private String signs;
	private String phone;
	
	public Student() {
		
	}

	public Student(String qq, String name, String province, String city,
			String password, String sex, String signs, String phone) {
		this.qq = qq;
		this.name = name;
		this.province = province;
		this.city = city;
		this.password = password;
		this.sex = sex;
		this.signs = signs;
		this.phone = phone;
	}

	public Student(String qq, String name, String province, String city,
			String password) {
		this.qq = qq;
		this.name = name;
		this.province = province;
		this.city = city;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
		return "Student [qq=" + qq + ", name=" + name + ", province="
				+ province + ", city=" + city + ", password=" + password
				+ ", sex=" + sex + ", signs=" + signs + ", phone=" + phone
				+ "]";
	}

	
}
