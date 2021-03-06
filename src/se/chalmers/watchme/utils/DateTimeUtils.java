/**
 *	DateTimeUtils.java
 *
 *  A utility class for converting dates to common formats
 *
 *	@author Robin Andersson, Johan Brook
 *	@copyright (c) 2012 Robin Andersson, Johan Brook, Mattias Henriksson, Lisa Stenberg
 *	@license MIT
 */

package se.chalmers.watchme.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

	private final static int HOUR = 60;

	private DateTimeUtils() {
	}

	/**
	 * Recieves a calendar instance and returns a String with simple date format
	 * ("d MMM, yyyy" = "14 Oct, 2012")
	 * 
	 * @param calendar
	 *            The calendar object to be used to create the string
	 * @returns A simple string representing a date in the format: "d MMM, yyyy"
	 */
	public static String toSimpleDate(Calendar calendar) {
		return toSimpleDate(calendar, "d MMM, yyyy");
	}

	/**
	 * Format a calendar date to a string with a certain format.
	 * 
	 * @param calendar
	 *            The date
	 * @param format
	 *            The format
	 * @return A formatted string according to 'format'
	 * @see SimpleDateFormat
	 */
	public static String toSimpleDate(Calendar calendar, String format) {
		SimpleDateFormat simpleDate = new SimpleDateFormat(format);
		String simpleDateString = simpleDate.format(calendar.getTime());

		return simpleDateString;
	}

	/**
	 * Convert duration in minutes to a more human readable form.
	 * 
	 * @param minutes
	 *            The duration in minutes
	 * @return A string on the format "HH:mm"
	 */
	public static String minutesToHuman(int minutes) {
		int remainingMinutes = minutes % HOUR;
		int hours = minutes / HOUR;

		return hours
				+ ":"
				+ ((remainingMinutes < 10) ? "0" + remainingMinutes
						: remainingMinutes);
	}

	/**
	 * Return the release year from a date on the format "YYYY-MM-DD".
	 * 
	 * @param longDate
	 *            The formatted long date
	 * @return The year as a string on the format "YYYY". If the parameter is
	 *         not well formated ("YYYY-MM-DD") the parameter is returned
	 *         untouched.
	 */
	public static String parseYearFromDate(String longDate) {
		if (longDate == null)
			return null;

		int index = longDate.indexOf("-");
		return (index != -1) ? longDate.substring(0, index) : longDate;
	}

	/**
	 * Formats a calendar instance to relative timestamp.
	 * 
	 * <p>
	 * Such as "next year", "3 days", etc. Only works for future dates.
	 * </p>
	 * 
	 * @param cal
	 *            The input date
	 * @return A formatted duration string
	 */
	public static String toHumanDate(Calendar cal) {
		Calendar now = GregorianCalendar.getInstance();

		int years = cal.get(Calendar.YEAR) - now.get(Calendar.YEAR);
		int months = cal.get(Calendar.MONTH) - now.get(Calendar.MONTH);
		int days = cal.get(Calendar.DAY_OF_MONTH)
				- now.get(Calendar.DAY_OF_MONTH);

		if (years == 1)
			return "next year";
		else if (years > 1)
			return years + " years";
		else if (months == 1)
			return "next month";
		else if (months > 1)
			return months + " months";
		else if (days == 1)
			return "tomorrow";
		else if (days > 1)
			return days + " days";

		return "today";
	}
	
	/**
	 * Check if a given date lies in a given interval from today's date.
	 * 
	 * <p>Ex. Check if a date is 5 days from now:</p>
	 * 
	 * <pre>DateTimeUtils.isDateInInterval(aDate, 5, TimeUnit.DAYS)
	 * </pre>
	 * 
	 * @param date The input date
	 * @param interval The interval
	 * @param type The type (ex. TimeUnit.DAYS, TimeUnit.MONTHS
	 * @return True if date lies within the interval, otherwise false
	 */
	public static boolean isDateInInterval(Calendar date, int interval, TimeUnit type) {
		Calendar diff = Calendar.getInstance();
    	long today = Calendar.getInstance().getTimeInMillis();
    	
    	diff.setTimeInMillis(TimeUnit.MILLISECONDS.convert(interval, type));
    	
    	return (date.getTimeInMillis() - today < diff.getTimeInMillis());
	}
}
