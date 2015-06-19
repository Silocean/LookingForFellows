package com.hblg.lookingfellow.tools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.hblg.lookingfellow.entity.User;

import android.os.Message;

public class NetModifyStuInfoTool {
	
	public static Message connNet(String key, String value) throws Exception {
		Message msg = new Message();
		String qq = User.qq;
		String path = "http://192.168.1.152:8080/lookingfellowWeb0.2/ModifyUserInfoServlet?qq=" + qq;
		value = URLEncoder.encode(value, "utf-8");
		path = path + "&" + key + "=" + value;
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		if(conn.getResponseCode() == 200) {
			InputStream in = conn.getInputStream();
			byte[] data = StreamTool.read(in);
			String str = new String(data);
			if(str.equals("true")) {
				msg.arg1 = 1; // 表示保存成功
				return msg;
			} else {
				msg.arg1 = 2; // 表示保存失败
				return msg;
			}
		}
		return null;
	}
	
}
