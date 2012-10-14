/**
 * 
 */
package se.chalmers.watchme.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import se.chalmers.watchme.R;

/**
 * A utility class for converting date's to common formats
 */
public class DateTimeUtils {
	
	private final static int HOUR = 60;
	
	/**
	 * Recieves a calendar instance and returns a String with simple
	 * date format(dd-MM-yyyy)
	 * 
	 * @param calendar The calendar object to be used to create the string
	 * @returns A simple string representing a date in the format: dd-MM-yyyy
	 */
	public static String toSimpleDate(Calendar calendar) {
		
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
		String simpleDateString = simpleDate.format(calendar.getTime());
		
		return simpleDateString;
		
	}
	
	/**
	 * Convert duration in minutes to a more human readable form.
	 * 
	 * @param minutes The duration in minutes
	 * @return A string on the format "HH:mm"
	 */
	public static String minutesToHuman(int minutes) {
		int remainingMinutes = minutes % HOUR;
		int hours = minutes / HOUR;
		
		return hours+":" + ((remainingMinutes < 10) ? "0"+remainingMinutes : remainingMinutes);
	}
	
}
