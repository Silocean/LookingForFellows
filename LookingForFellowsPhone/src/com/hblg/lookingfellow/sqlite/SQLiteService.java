package com.hblg.lookingfellow.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hblg.lookingfellow.entity.Friend;
import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;

public class SQLiteService {
	
	DBOpenHelper openHelper;
	
	public SQLiteService(Context context) {
		openHelper = new DBOpenHelper(context);
	}
	/**
	 * 获取全国所有省份
	 * @return
	 */
	public LinkedList<String> getAllProvinces() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		LinkedList<String> list = new LinkedList<String>();
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
		values.put("stuPro", stu.getProvince());
		values.put("stuCity", stu.getCity());
		values.put("stuPassword", stu.getPassword());
		values.put("stuSex", stu.getSex());
		values.put("stuSigns", stu.getSigns());
		values.put("stuPhone", stu.getPhone());
		db.insert("student", null, values);
		db.close();
	}
	/**
	 * 获取用户所有好友信息
	 * @param qq
	 * @return
	 */
	public ArrayList<Map<String, String>> getAllFriendsInfo(String qq) {
		ArrayList<Map<String, String>> friends = new ArrayList<Map<String,String>>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from friend where ownerId = ?";
		Cursor cursor = db.rawQuery(sql, new String[]{qq});
		while(cursor.moveToNext()) {
			Map<String, String> friend = new HashMap<String, String>();
			friend.put("friQQ", cursor.getString(cursor.getColumnIndex("friQQ")));
			friend.put("friName", cursor.getString(cursor.getColumnIndex("friName")));
			friend.put("friPro", cursor.getString(cursor.getColumnIndex("friPro")));
			friend.put("friCity", cursor.getString(cursor.getColumnIndex("friCity")));
			friend.put("friSex", cursor.getString(cursor.getColumnIndex("friSex")));
			friend.put("friSigns", cursor.getString(cursor.getColumnIndex("friSigns")));
			friend.put("friPhone", cursor.getString(cursor.getColumnIndex("friPhone")));
			friends.add(friend);
		}
		cursor.close();
		db.close();
		return friends;
	}
	/**
	 * 根据好友qq号码查询好友名字
	 * @param qq
	 * @return
	 */
	public String getFriendNameByQq(String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select friName from friend where ownerId = ? and friQQ = ?";
		Cursor cursor = db.rawQuery(sql, new String[]{User.qq, qq});
		String friName = null;
		if(cursor.moveToFirst()) {
			friName = cursor.getString(cursor.getColumnIndex("friName"));
		}
		cursor.close();
		db.close();
		return friName;
	}
	/**
	 * 添加好友信息到本地
	 * @param friend
	 */
	public void addFriend(Friend friend) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ownerId", User.qq);
		values.put("friQQ", friend.getQq());
		values.put("friName", friend.getName());
		values.put("friPro", friend.getProvince());
		values.put("friCity", friend.getCity());
		values.put("friSex", friend.getSex());
		values.put("friSigns", friend.getSigns());
		values.put("friPhone", friend.getPhone());
		db.insert("friend", null, values);
		db.close();
		System.out.println("添加好友信息到本地");
	}
	/**
	 * 删除好友信息
	 * @param sender
	 */
	public void deleteFriendInfo(String sender) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "delete from friend where ownerId = ? and friQQ = ?";
		db.execSQL(sql, new String[]{User.qq, sender});
		db.close();
		System.out.println("删除好友信息成功");
	}
	public void deleteAllFriendInfo() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "delete from friend";
		db.execSQL(sql);
		db.close();
		System.out.println("删除所有好友信息成功");
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
			stu.setProvince(cursor.getString(cursor.getColumnIndex("stuPro")));
			stu.setCity(cursor.getString(cursor.getColumnIndex("stuCity")));
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
		db.close();
	}
	/**
	 * 根据QQ号码更改本地用户家乡
	 * @param qq
	 */
	public void modifyHometown(String pro, String city, String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update student set stuPro = ?, stuCity = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{pro, city, qq});
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
		db.close();
	}
	/**
	 * 把从服务器获取的暂存消息保存到本地
	 * @param messages
	 */
	public void saveMessages(ArrayList<Map<String, Object>> messages) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into message values (?, ?, ?, ?, ?, ?)";
		for(int i=0; i<messages.size(); i++) {
			Map<String, Object> map = messages.get(i);
			db.execSQL(sql, new Object[]{null, map.get("msgType"), map.get("msgSender"), map.get("msgReceiver"), map.get("msgDetails"), map.get("msgTime")});
		}
		db.close();
	}
	/**
	 * 把从服务器获取的暂存的请求添加好友消息保存到本地
	 * @param messages
	 */
	public void saveRequestAddFriendMessages(ArrayList<Map<String, Object>> messages) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into requestAddMsg values (?, ?, ?, ?, ?, ?)";
		for(int i=0; i<messages.size(); i++) {
			Map<String, Object> map = messages.get(i);
			db.execSQL(sql, new Object[]{null, map.get("msgType"), map.get("msgSender"), map.get("msgReceiver"), map.get("msgDetails"), map.get("msgTime")});
		}
		System.out.println("把从服务器获取的暂存的请求添加好友消息保存到本地");
		db.close();
	}
	/**
	 * 把自己聊天记录保存在本地
	 * @param msg
	 */
	public void saveMessage(Message msg) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into message values (?, ?, ?, ?, ?, ?)";
		db.execSQL(sql, new Object[]{null, msg.getType(), msg.getSender(), msg.getReceiver(), msg.getDetails(), msg.getTime()});
		db.close();
		System.out.println("保存聊天信息到本地");
	}
	/**
	 * 把请求添加好友消息保存在本地
	 * @param msg
	 */
	public void saveRequestAddFriendMessage(Message msg) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into requestAddMsg values (?, ?, ?, ?, ?, ?)";
		db.execSQL(sql, new Object[]{null, msg.getType(), msg.getSender(), msg.getReceiver(), msg.getDetails(), msg.getTime()});
		db.close();
		System.out.println("保存请求添加好友消息到本地");
	}
	/**
	 * 取得本地所有聊天信息
	 * @return
	 */
	public ArrayList<Map<String, Object>> getMessages() {
		ArrayList<Map<String, Object>> messages = new ArrayList<Map<String,Object>>();
		List<String> chatToPersons = this.getAllChatTo();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from message where (msgSender=? and msgReceiver=?) or (msgSender=? and msgReceiver=?) order by msgId desc";
		Cursor cursor = null;
		for(int i=chatToPersons.size()-1; i>=0; i--) {
			cursor = db.rawQuery(sql, new String[]{User.qq, chatToPersons.get(i), chatToPersons.get(i), User.qq});
			if(cursor.moveToFirst()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("headimage", chatToPersons.get(i));
				map.put("msgType", cursor.getInt(cursor.getColumnIndex("msgType")));
				map.put("msgSender", cursor.getString(cursor.getColumnIndex("msgSender")));
				map.put("msgReceiver", cursor.getString(cursor.getColumnIndex("msgReceiver")));
				map.put("msgDetails", cursor.getString(cursor.getColumnIndex("msgDetails")));
				map.put("msgTime", cursor.getString(cursor.getColumnIndex("msgTime")));
				messages.add(map);
			}
			cursor.close();
		}
		db.close();
		return messages;
	}
	/**
	 * 取得本地所有请求添加好友信息
	 * @return
	 */
	public ArrayList<Map<String, Object>> getRequestAddFriendMessages() {
		ArrayList<Map<String, Object>> messages = new ArrayList<Map<String,Object>>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from requestAddMsg where msgReceiver = ? order by msgId desc";
		Cursor cursor = db.rawQuery(sql, new String[]{User.qq});
		while(cursor.moveToNext()) {
			boolean flag = false;
			for(int i=0; i<messages.size(); i++) {
				if(messages.get(i).get("msgSender").equals(cursor.getString(cursor.getColumnIndex("msgSender")))) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("msgType", cursor.getInt(cursor.getColumnIndex("msgType")));
				map.put("msgSender", cursor.getString(cursor.getColumnIndex("msgSender")));
				map.put("msgReceiver", cursor.getString(cursor.getColumnIndex("msgReceiver")));
				map.put("msgDetails", cursor.getString(cursor.getColumnIndex("msgDetails")));
				map.put("msgTime", cursor.getString(cursor.getColumnIndex("msgTime")));
				messages.add(map);
			}
		}
		cursor.close();
		db.close();
		return messages;
	}
	/**
	 * 根据聊天对象获取聊天记录
	 * @param receiver
	 * @return
	 */
	public ArrayList<Map<String, Object>> getChatMessages(String sender, String receiver) {
		ArrayList<Map<String, Object>> messages = new ArrayList<Map<String,Object>>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from message where (msgSender=? and msgReceiver=?) or (msgSender=? and msgReceiver=?)";
		Cursor cursor = db.rawQuery(sql, new String[]{sender, receiver, receiver, sender});
		while(cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msgId", cursor.getInt(cursor.getColumnIndex("msgId")));
			map.put("msgType", cursor.getInt(cursor.getColumnIndex("msgType")));
			map.put("msgSender", cursor.getString(cursor.getColumnIndex("msgSender")));
			map.put("msgReceiver", cursor.getString(cursor.getColumnIndex("msgReceiver")));
			map.put("msgDetails", cursor.getString(cursor.getColumnIndex("msgDetails")));
			map.put("msgTime", cursor.getString(cursor.getColumnIndex("msgTime")));
			messages.add(map);
		}
		cursor.close();
		db.close();
		return messages;
	}
	/**
	 * 更新我的消息列表
	 * @param qq
	 */
	public void updateMyMessageList(String qq) {
		List<String> list = this.getAllChatTo();
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into chatTo values (?, ?, ?)";
		boolean flag = false;
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).equals(qq)) { // 如果列表中已有此人的记录
				flag = true;
			}
		}
		if(!flag) {
			db.execSQL(sql, new Object[]{null, User.qq, qq});
		}
		db.close();
	}
	/**
	 * 把好友从消息列表中移除
	 * @param qq
	 */
	public void deleteFromMyMessageList(String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "delete from chatTo where ownerId = ? and chatToQq = ?";
		db.execSQL(sql, new Object[]{User.qq, qq});
		db.close();
	}
	/**
	 * 删除与某好友的所有聊天记录
	 * @param qq
	 * @param friendQq
	 */
	public void deleteMsg(String qq, String friendQq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "delete from message where (msgSender=? and msgReceiver=?) or (msgSender=? and msgReceiver=?)";
		db.execSQL(sql, new Object[]{qq, friendQq, friendQq, qq});
		db.close();
	}
	/**
	 * 删除某好友的请求添加好友请求
	 * @param qq
	 * @param friendQq
	 */
	public void deleteRequestAddFriendMsg(String qq, String friendQq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "delete from requestAddMsg where (msgSender=? and msgReceiver=?) or (msgSender=? and msgReceiver=?)";
		db.execSQL(sql, new Object[]{qq, friendQq, friendQq, qq});
		db.close();
	}
	/**
	 * 取得所有聊天对象
	 * @return
	 */
	public List<String> getAllChatTo() {
		List<String> senders = new ArrayList<String>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select chatToQq from chatTo where ownerId = ?";
		Cursor cursor = db.rawQuery(sql, new String[]{User.qq});
		while(cursor.moveToNext()) {
			senders.add(cursor.getString(cursor.getColumnIndex("chatToQq")));
		}
		cursor.close();
		db.close();
		return senders;
	}
	/**
	 * 获取聊天记录中最新的一条
	 * @param msg
	 * @return
	 */
	public Map<String, Object> getLastMessage(Message msg) {
		Map<String, Object> map = new HashMap<String, Object>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from message where msgSender=? and msgReceiver=? order by msgId desc";
		Cursor cursor = db.rawQuery(sql, new String[]{msg.getSender(), msg.getReceiver()});
		cursor.moveToFirst();
		map.put("msgId", cursor.getInt(cursor.getColumnIndex("msgId")));
		map.put("msgType", cursor.getInt(cursor.getColumnIndex("msgType")));
		map.put("msgSender", cursor.getString(cursor.getColumnIndex("msgSender")));
		map.put("msgReceiver", cursor.getString(cursor.getColumnIndex("msgReceiver")));
		map.put("msgDetails", cursor.getString(cursor.getColumnIndex("msgDetails")));
		map.put("msgTime", cursor.getString(cursor.getColumnIndex("msgTime")));
		cursor.close();
		db.close();
		return map;
	}
	/**
	 * 根据消息Id号删除信息
	 * @param id
	 */
	public void deleteMsg(int id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "delete from message where msgId = ?";
		db.execSQL(sql, new Object[]{id});
		db.close();
	}
	/**
	 * 检查此人是不是我的好友
	 * @param qq
	 * @return
	 */
	public boolean checkIsFriend(String qq) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "select * from friend where ownerId = ? and friQQ = ?";
		Cursor cursor = db.rawQuery(sql, new String[]{User.qq, qq});
		if(cursor.moveToFirst()) {
			return true; // 表示是好友关系
		}
		return false;
	}
	
}
