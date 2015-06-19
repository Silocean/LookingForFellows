package com.hblg.lookingfellow.entity;

import java.io.Serializable;

public class User{
	public static String qq;
	public static String password;
	
	public void saveQqAndPassword(String qq, String password) {
		this.qq = qq;
		this.password = password;
	}
	
}
