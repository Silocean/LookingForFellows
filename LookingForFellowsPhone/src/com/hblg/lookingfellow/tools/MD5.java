package com.hblg.lookingfellow.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
		public static String  getMD5(String num) {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(num.getBytes());
				return getHash(md);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String getHash(MessageDigest md){
			StringBuilder sb= new StringBuilder();
			for(byte b : md.digest()){
				sb.append(Integer.toHexString((b>>6)& 0xf));
				sb.append(Integer.toHexString(b& 0xf));
			}
			return sb.toString();
		}
}
