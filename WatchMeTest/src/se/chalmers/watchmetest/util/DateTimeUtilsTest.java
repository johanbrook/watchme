/**
*	DateTimeUtilsTest.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchmetest.util;

import se.chalmers.watchme.utils.DateTimeUtils;
import junit.framework.TestCase;

public class DateTimeUtilsTest extends TestCase {
	
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
}
