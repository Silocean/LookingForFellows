package com.hblg.lookingfellow.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvertTool {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * ����ת�����ַ���
	 * @param date
	 * @return
	 */
	public static String convertToString(Date date) {
		return sdf.format(date);
	}
	/**
	 * �ַ���ת��������
	 * @param str
	 * @return
	 */
	public static Date convertToDate(String str) {
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ���������ַ������������ʱ���
	 * @param time
	 */
	public static String calDateTime(String time) {
		Date date = convertToDate(time);
		long now = System.currentTimeMillis();
		long distance = now - date.getTime();
		long year = distance / (1000*60*60*24*365);
		System.out.println("year:"+year +" "+ distance);
		long day = distance / (1000*60*60*24);
		long hour = distance / (1000*60*60);
		long minute = distance / (1000*60);
		long second = distance / 1000;
		if(year == 0) {
			if(day>7 && day<14) {
				return "һ��ǰ";
			} else if(day>0 && day<=7) {
				return day + "��ǰ";
			} else if(day<=0 && hour>0) {
				return hour + "Сʱǰ";
			} else if(hour<=0 && minute>0) {
				return minute + "����ǰ";
			} else if(minute<=0 && second>0) {
				return second + "��ǰ";
			} else if(second == 0) {
				return "�ո�";
			}
			return time.substring(5, 10);
		}
		return time.substring(0, 10);
	}
	
}
