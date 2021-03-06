package com.hblg.lookingfellow.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.StreamTool;

public class GetUserInfoService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		getUserInfo();
	}

	private void getUserInfo() {
		try {
			String path = Common.PATH + "GetUserInfoServlet?qq=";
			String qq = User.qq;
			path = path + qq;
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] data = StreamTool.read(in);
				String json = new String(data, "utf-8");
				Student student = parseJson(json);
				SQLiteService service = new SQLiteService(getApplicationContext());
				service.deleteStuInfo();
				service.saveStuInfo(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Student parseJson(String json) throws JSONException {
		System.out.println(json);
		JSONObject obj = new JSONObject(json);
		Student student = new Student();
		student.setQq(obj.getString("stuQQ"));
		student.setName(obj.getString("stuName"));
		student.setPassword(obj.getString("stuPassword"));
		student.setProvince(obj.getString("stuPro"));
		student.setCity(obj.getString("stuCity"));
		student.setSex(obj.getString("stuSex"));
		student.setSigns(obj.getString("stuSigns"));
		student.setPhone(obj.getString("stuPhone"));
		return student;
	}
	
}
