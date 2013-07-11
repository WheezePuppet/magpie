package edu.umw.cpsc.magpie.util;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateHelper {
	private static final int MS_IN_DAY = 1000 * 60 * 60 * 24;

	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	public static Calendar today() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		return calendar;
	}

	public static Calendar dateCal(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar;
	}

	public static Calendar addDays(Date date, int days) {
		Calendar calendar = stripTime(date);

		calendar.add(Calendar.DATE, days);

		return calendar;
	}

	public static Calendar stripTime(Date date) {
		Calendar calendar = toCalendar(date);

		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		return calendar;
	}

	public static Calendar stripDate(Date date) {
		Calendar calendar = toCalendar(date);

		calendar.set(Calendar.YEAR, 0);
		calendar.set(Calendar.DAY_OF_YEAR, 0);

		return calendar;
	}

	public static int daysBetween(Date date1, Date date2) {
		long interval = date2.getTime() - date1.getTime();

		int days = (int)(interval / MS_IN_DAY);
		if (interval % MS_IN_DAY != 0)
			days++;

		return days;
	}

	public static boolean onSameDay(Date date1, Date date2) {
		Calendar cal1 = stripTime(date1);
		Calendar cal2 = stripTime(date2);

		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
			cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	public static boolean todayOrEarlier(Calendar calendar) {
		return todayOrEarlier(calendar.getTime());
	}

	public static boolean todayOrEarlier(Date date) {
		Calendar today = today();
		Calendar dateCal = stripTime(date);

		return dateCal.getTimeInMillis() <= today.getTimeInMillis();
	}

	public static java.sql.Date nowSqlDate() {
		return new java.sql.Date(stripTime(new Date()).getTimeInMillis());
	}

	public static java.sql.Time nowSqlTime() {
		return new java.sql.Time(stripDate(new Date()).getTimeInMillis());
	}

	public static String format(Calendar calendar) {
		return format(calendar.getTime());
	}

	public static String format(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE M/d");

		return formatter.format(date);
	}

	public static String toDatetimeString(java.sql.Date date, java.sql.Time time) {
		return "'" + date.toString() + " " + time.toString() + "'";
	}
}
