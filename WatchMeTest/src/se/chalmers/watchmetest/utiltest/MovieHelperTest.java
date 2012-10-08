/**
 *	MovieHelperTest.java
 *
 *	@author Johan Brook
 *	@copyright (c) 2012 Johan Brook
 *	@license MIT
 */

package se.chalmers.watchmetest.utiltest;

import junit.framework.TestCase;
import se.chalmers.watchme.utils.MovieHelper;
import android.test.suitebuilder.annotation.SmallTest;


public class MovieHelperTest extends TestCase {
	
	public MovieHelperTest() {
		super();
	}
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@SmallTest
	public void testParseYearFromDate() {
		String correctDate = "2012-03-03";
		assertEquals(MovieHelper.parseYearFromDate(correctDate), "2012");
		
		String incorrectDate = "2012/03/03";
		assertEquals(MovieHelper.parseYearFromDate(incorrectDate), incorrectDate);
		
		assertNull(MovieHelper.parseYearFromDate(null), null);
	}
}
