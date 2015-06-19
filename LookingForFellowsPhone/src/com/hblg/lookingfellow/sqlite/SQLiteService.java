package com.hblg.lookingfellow.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;

public class SQLiteService {
	
	DBOpenHelper openHelper;
	
	String imagePath = "http://192.168.1.152:8080/lookingfellowWeb0.2/head/";
	
	public SQLiteService(Context context) {
		openHelper = new DBOpenHelper(context);
	}
	/**
	 * 获取全国所有省份
	 * @return
	 */
	public List<String> getAllProvinces() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select proName from province", null);
		while(cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("proName")));
		}
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * 根据省份名获取该省份二级城市
	 * @param proName
	 * @return
	 */
	public List<String> getAllCities(String proName) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select cityName from city where proID = (select proID from province where proName = ?)",
				new String[]{proName});
		while(cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("cityName")));
		}
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * 保存用户信息到本地
	 * @param stu
	 */
	public void saveStuInfo(Student stu) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("stuQQ", stu.getQq());
		values.put("stuName", stu.getName());
		values.put("stuHometown", stu.getHometown());
		values.put("stuPassword", stu.getPassword());
		values.put("stuSex", stu.getSex());
		values.put("stuSigns", stu.getSigns());
		values.put("stuPhone", stu.getPhone());
		db.insert("student", null, values);
		db.close();
	}
	/**
	 * 从本地删除用户信息
	 */
	public void deleteStuInfo() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from student");
		db.close();
	}
	/**
	 * 检查该用户是否是第一次登录
	 * @param qq
	 * @return
	 */
	public boolean checkStuInfo(String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from student where stuQQ = ?";
		Cursor cursor = db.rawQuery(sql, new String[]{qq});
		if(cursor.moveToFirst()) {
			cursor.close();
			db.close();
			return true; //表示已有用户登录过
		}
		cursor.close();
		db.close();
		return false; // 表示还没有用户登录过
	}
	/**
	 * 根据QQ号码获取用户信息
	 * @param qq
	 * @return
	 */
	public Student getStuInfo(String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Student stu = null;
		String sql = "select * from student where stuQQ = ?";
		Cursor cursor = db.rawQuery(sql, new String[]{qq});
		if(cursor.moveToFirst()) {
			stu = new Student();
			stu.setQq(qq);
			stu.setName(cursor.getString(cursor.getColumnIndex("stuName")));
			stu.setHometown(cursor.getString(cursor.getColumnIndex("stuHometown")));
			stu.setPassword(cursor.getString(cursor.getColumnIndex("stuPassword")));
			stu.setSex(cursor.getString(cursor.getColumnIndex("stuSex")));
			stu.setSigns(cursor.getString(cursor.getColumnIndex("stuSigns")));
			stu.setPhone(cursor.getString(cursor.getColumnIndex("stuPhone")));
		}
		cursor.close();
		db.close();
		return stu;
	}
	/**
	 * 根据QQ号码更改本地用户手机号
	 * @param qq
	 */
	public void modifyMobile(String mobile, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuPhone = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{mobile, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * 根据QQ号码更改本地用户名字
	 * @param qq
	 */
	public void modifyName(String name, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuName = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{name, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * 根据QQ号码更改本地用户个性签名
	 * @param qq
	 */
	public void modifySigns(String signs, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuSigns = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{signs, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * 根据QQ号码更改本地用户性别
	 * @param qq
	 */
	public void modifySex(String sex, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuSex = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{sex, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * 根据QQ号码更改本地用户家乡
	 * @param qq
	 */
	public void modifyHometown(String hometown, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuHometown = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{hometown, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * 根据QQ号码更改本地用户密码
	 * @param qq
	 */
	public void modifyPassword(String newPassword, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuPassword = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{newPassword, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * 把聊天记录保存到本地
	 * @param messages
	 */
	public void saveMessage(ArrayList<Map<String, Object>> messages) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into message values (?, ?, ?, ?, ?, ?, ?)";
		for(int i=0; i<messages.size(); i++) {
			Map<String, Object> map = messages.get(i);
			// 这里注意发送者，接收者位置要颠倒一下，在客户端看来，用户以外的人都是接收者
			db.execSQL(sql, new Object[]{null, map.get("msgType"), map.get("msgReceiver"), map.get("msgSender"), map.get("msgSenderName"), map.get("msgDetails"), map.get("msgTime")});
		}
		System.out.println("添加聊天记录成功");
		db.close();
	}
	/**
	 * 取得本地所有聊天信息
	 * @return
	 */
	public ArrayList<Map<String, Object>> getMessages() {
		List<String> chatToPersons = this.getAllChatTo();
		ArrayList<Map<String, Object>> messages = new ArrayList<Map<String,Object>>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		for(int i=0; i<chatToPersons.size(); i++) {
			String sql = "select * from message where msgSender = ? and msgReceiver = ?";
			Cursor cursor = db.rawQuery(sql, new String[]{User.qq, chatToPersons.get(i)});
			cursor.moveToFirst();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("headimage", imagePath + "head_" + cursor.getString(cursor.getColumnIndex("msgReceiver")) + ".jpg");
			map.put("msgType", cursor.getInt(cursor.getColumnIndex("msgType")));
			map.put("msgSender", cursor.getString(cursor.getColumnIndex("msgSender")));
			map.put("msgReceiver", cursor.getString(cursor.getColumnIndex("msgReceiver")));
			map.put("msgReceiverName", cursor.getString(cursor.getColumnIndex("msgReceiverName")));
			map.put("msgDetails", cursor.getString(cursor.getColumnIndex("msgDetails")));
			map.put("msgTime", cursor.getString(cursor.getColumnIndex("msgTime")));
			messages.add(map);
			cursor.close();
		}
		db.close();
		return messages;
	}
	/**
	 * 取得所有聊天对象（相对于客户端而言）
	 * @return
	 */
	public List<String> getAllChatTo() {
		List<String> senders = new ArrayList<String>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select msgReceiver from message group by msgReceiver";
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext()) {
			senders.add(cursor.getString(cursor.getColumnIndex("msgReceiver")));
		}
		cursor.close();
		db.close();
		return senders;
	}
	
}
