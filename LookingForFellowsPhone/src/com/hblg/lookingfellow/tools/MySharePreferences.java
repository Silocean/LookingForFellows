package com.hblg.lookingfellow.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//首选项
public class MySharePreferences {
	private static SharedPreferences preferences=null;
	private static Editor edit =  null;
	//首选项名字
	public static final String SHARED_FILE_NAME = "MyShare";
	
	/** 屏幕亮度*/
	public static final String UI="UIMode";
	/**聊天背景*/
	public static final String BGSKIN="background";
	
	/**图片是否开启,1开启，0关闭,默认开启（1）*/
	public static final String PHOTODEAL="photodeal";
	
	/**
	 * @param context
	 * @return SharedPreferences
	 */
	public static SharedPreferences getShare(Context context) {
		if (preferences == null) {
			preferences = context.getSharedPreferences(SHARED_FILE_NAME,Context.MODE_PRIVATE);
		}
		return preferences;
	}
	
	/**
	 * 
	 * @param preferences
	 * @return Editor
	 */
	public static Editor getEditor(SharedPreferences preferences) {
		if (preferences != null) {
			if (edit == null) {
				edit = preferences.edit();
			}
			return edit;
		}
		return null;
	}

}
