package com.hblg.lookingfellow.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvertTool {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 日期转换成字符串
	 * @param date
	 * @return
	 */
	public static String convertToString(Date date) {
		return sdf.format(date);
	}
	/**
	 * 字符串转换成日期
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
	 * 根据日期字符串计算距现在时间差
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
				return "一周前";
			} else if(day>0 && day<=7) {
				return day + "天前";
			} else if(day<=0 && hour>0) {
				return hour + "小时前";
			} else if(hour<=0 && minute>0) {
				return minute + "分钟前";
			} else if(minute<=0 && second>0) {
				return second + "秒前";
			} else if(second == 0) {
				return "刚刚";
			}
			return time.substring(5, 10);
		}
		return time.substring(0, 10);
	}
	
}
