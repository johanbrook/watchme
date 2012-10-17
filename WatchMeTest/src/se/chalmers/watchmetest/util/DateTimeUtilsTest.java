/**
*	DateTimeUtilsTest.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchmetest.util;

import java.util.Calendar;

import android.test.suitebuilder.annotation.SmallTest;
import se.chalmers.watchme.utils.DateTimeUtils;
import se.chalmers.watchme.utils.MovieHelper;
import junit.framework.TestCase;

public class DateTimeUtilsTest extends TestCase {
	
	public void testToSimpleDate() {
		Calendar cal = Calendar.getInstance();
		// The month really is October (10)
		cal.set(2012, 9, 14);
		
		// Don't forget the correct locale
		assertEquals("14 Oct, 2012", DateTimeUtils.toSimpleDate(cal));
	}
	
	public void testToSimpleDateFormat() {
		String format = "yyyy-MM-dd";
		Calendar cal = Calendar.getInstance();
		cal.set(2012, 9, 14);
		
		assertEquals("2012-10-14", DateTimeUtils.toSimpleDate(cal, format));
	}
	
	public void testMinutesToHuman() {
		int minutes1 = 124;
		int minutes2 = 119;
		int minutes3 = 120;
		int minutes4 = 59;
		
		assertEquals("2:04", DateTimeUtils.minutesToHuman(minutes1));
		assertEquals("1:59", DateTimeUtils.minutesToHuman(minutes2));
		assertEquals("2:00", DateTimeUtils.minutesToHuman(minutes3));
		assertEquals("0:59", DateTimeUtils.minutesToHuman(minutes4));
	}
	
	public void testParseYearFromDate() {
		String correctDate = "2012-03-03";
		assertEquals(DateTimeUtils.parseYearFromDate(correctDate), "2012");
		
		String incorrectDate = "2012/03/03";
		assertEquals(DateTimeUtils.parseYearFromDate(incorrectDate), incorrectDate);
		
		assertNull(DateTimeUtils.parseYearFromDate(null), null);
	}
}
