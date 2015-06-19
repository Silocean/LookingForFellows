package com.hblg.lookingfellow.tools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {

	public static byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000); //�������ӳ�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET"); //��������ʽΪGET��ע��һ��Ҫ��д
		if(conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream(); //ȡ��url���ӵ�������
			
			return StreamTool.read(inputStream);
			
		}
		return null;
	}


}
