package com.hblg.lookingfellow.entity;

import java.io.Serializable;

public class LoginUser implements Serializable{
	String qq;
	String password;
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	
}
