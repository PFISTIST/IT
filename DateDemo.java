package com.LA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateDemo {
	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(date);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = new GregorianCalendar();
		int year = c1.get(Calendar.YEAR);
		int month = c1.get(Calendar.MONTH);
		int day = c1.get(Calendar.DAY_OF_MONTH);
		int hour = c1.get(Calendar.HOUR_OF_DAY);
		int minute = c1.get(Calendar.MINUTE);
		int second = c1.get(Calendar.SECOND);
		int millisecond = c1.get(Calendar.MILLISECOND);
		StringBuilder sb = new StringBuilder(55);
		sb.append(year).append("��").append(month).append("��").append(day).append("��").append(hour).append("��")
				.append(minute).append("��").append(second).append("��").append(millisecond).append("����");
		System.out.println(sb);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
		String nowDate = df.format(new Date());
		System.out.println(nowDate);
	}
}
