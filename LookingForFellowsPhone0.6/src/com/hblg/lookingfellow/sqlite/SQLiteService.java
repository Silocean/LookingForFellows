package com.hblg.lookingfellow.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.hblg.lookingfellow.entity.Student;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteService {
	
	DBOpenHelper openHelper;
	
	public SQLiteService(Context context) {
		openHelper = new DBOpenHelper(context);
	}
	/**
	 * ��ȡȫ������ʡ��
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
	 * ����ʡ������ȡ��ʡ�ݶ�������
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
	 * �����û���Ϣ������
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
	 * �ӱ���ɾ���û���Ϣ
	 */
	public void deleteStuInfo() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from student");
		db.close();
	}
	/**
	 * �����û��Ƿ��ǵ�һ�ε�¼
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
			return true; //��ʾ�����û���¼��
		}
		cursor.close();
		db.close();
		return false; // ��ʾ��û���û���¼��
	}
	/**
	 * ����QQ�����ȡ�û���Ϣ
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
	 * ����QQ������ı����û��ֻ���
	 * @param qq
	 */
	public void modifyMobile(String mobile, String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "update student set stuPhone = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{mobile, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * ����QQ������ı����û�����
	 * @param qq
	 */
	public void modifyName(String name, String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "update student set stuName = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{name, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * ����QQ������ı����û�����ǩ��
	 * @param qq
	 */
	public void modifySigns(String signs, String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "update student set stuSigns = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{signs, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * ����QQ������ı����û��Ա�
	 * @param qq
	 */
	public void modifySex(String sex, String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "update student set stuSex = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{sex, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * ����QQ������ı����û�����
	 * @param qq
	 */
	public void modifyHometown(String hometown, String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "update student set stuHometown = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{hometown, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	/**
	 * ����QQ������ı����û�����
	 * @param qq
	 */
	public void modifyPassword(String newPassword, String qq) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "update student set stuPassword = ? where stuQQ = ?";
		db.execSQL(sql, new Object[]{newPassword, qq});
		System.out.println("save to local successfully");
		db.close();
	}
	
}
