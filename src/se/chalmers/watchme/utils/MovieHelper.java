/**
*	MovieHelper.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.utils;

public class MovieHelper {
	
	/**
	 * Return the release year from a date on the format
	 * "YYYY-MM-DD".
	 * 
	 * @param longDate The formatted long date
	 * @return The year as a string on the format "YYYY". If the parameter is
	 * not well formated ("YYYY-MM-DD") the parameter is returned untouched.
	 */
	public static String parseYearFromDate(String longDate) {
		if(longDate == null)
			return null;
		
		int index = longDate.indexOf("-");
		return (index != -1) ? longDate.substring(0, index) : longDate; 
	}
	
}
