package com.hblg.lookingfellow.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteService {
	SQLiteDatabase db;
	
	public SQLiteService(Context context) {
		DBOpenHelper openHelper = new DBOpenHelper(context);
		db = openHelper.getReadableDatabase();
	}
	
	public List<String> getAllProvinces() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select proName from province", null);
		while(cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("proName")));
		}
		cursor.close();
		return list;
	}
	
	public List<String> getAllCities(String proName) {
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select cityName from city where proID = (select proID from province where proName = ?)",
				new String[]{proName});
		while(cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("cityName")));
		}
		cursor.close();
		return list;
	}
	
}
