/**
 *	MovieHelperTest.java
 *
 *	@author Johan Brook
 *	@copyright (c) 2012 Johan Brook
 *	@license MIT
 */

package se.chalmers.watchmetest.util;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import se.chalmers.watchme.model.Tag;
import se.chalmers.watchme.utils.MovieHelper;
import android.test.suitebuilder.annotation.SmallTest;


public class MovieHelperTest extends TestCase {
	
	public MovieHelperTest() {
		super();
	}
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testStringArrayToTagList() {
		
		String[] tagStrings = {"tag1", "tag2", "tag3", "tag4", "tag5"};
		
		List<Tag> tags = MovieHelper.stringArrayToTagList(tagStrings);
		
		for(String tagString : tagStrings) {
			assertTrue(tags.contains(new Tag(tagString)));
		}
		
	}
	
}
