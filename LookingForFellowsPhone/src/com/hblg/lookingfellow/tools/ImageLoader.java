package com.hblg.lookingfellow.tools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {

	public static byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000); //设置连接超时时间为5秒
		conn.setRequestMethod("GET"); //设置请求方式为GET，注意一定要大写
		if(conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream(); //取得url连接的输入流
			
			return StreamTool.read(inputStream);
			
		}
		return null;
	}


}
