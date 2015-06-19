package com.hblg.lookingfellow.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "lookingfellow.db", null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table province (proID int primary key, proName varchar(50) not null)");
		db.execSQL("create table city (cityID VARCHAR(2) not null," +
				" cityName varchar(50) primary key," +
				" proID VARCHAR(2))");
		db.execSQL("create table student (stuQQ varchar(15) primary key, stuName varchar(20)," +
				" stuHometown varchar(50), stuPassword varchar(20), stuSex varchar(2)," +
				" stuSigns varchar(60), stuPhone varchar(11))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
