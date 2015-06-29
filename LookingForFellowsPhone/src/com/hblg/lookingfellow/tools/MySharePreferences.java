package com.hblg.lookingfellow.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//��ѡ��
public class MySharePreferences {
	private static SharedPreferences preferences=null;
	private static Editor edit =  null;
	//��ѡ������
	public static final String SHARED_FILE_NAME = "MyShare";
	
	/** ��Ļ����*/
	public static final String UI="UIMode";
	/**���챳��,red,blue,black,none*/
	public static final String BGSKIN="background";
	public static final String BGSKIN_RED="red";
	public static final String BGSKIN_BLUE="blue";
	public static final String BGSKIN_BLACK="black";
	
	/**ͼƬ�Ƿ���,1������0�ر�,Ĭ�Ͽ�����1��*/
	public static final String PHOTODEAL="photodeal";
	
	/**
	 * @param context
	 * @return SharedPreferences
	 */
	public static SharedPreferences getShare(Context context) {
		if (preferences == null) {
			preferences = context.getSharedPreferences(SHARED_FILE_NAME,context.MODE_PRIVATE);
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
