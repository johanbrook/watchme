package se.chalmers.watchmetest.modeltest;

import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
import android.test.AndroidTestCase;

/**
 * This class tests methods in se.chalmers.watchme.model.Tag
 * @author mattiashenriksson
 *
 */

public class TagTest extends AndroidTestCase {
		
	public TagTest() {
		super();
	}

	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testGetId() {
		Tag action = new Tag("action");
		action.setId(1);
		assertTrue(action.getId() == 1);
	}
	
	public void testGetName() {
		Tag action = new Tag("action");
		assertTrue(action.getName().equals("action"));
	}
	
	public void testGetSlug() {
		Tag action = new Tag("AcTiOn");
		assertTrue(action.getSlug().equals("action"));
	}
	
	public void testEquals() {
		
		Tag action = new Tag("action");
		Tag compareObject = null; 
		assertFalse(action.equals(compareObject));
		
		/*
		 * Test that Tag is not equal to possible subclasses
		 */
		class EpicTag extends Tag {
			public EpicTag(String name) {
				super(name);
			}
		}		
		compareObject = new EpicTag("action");
		assertFalse(action.equals(compareObject));
		
		compareObject = new Tag("drama");
		assertFalse(action.equals(compareObject));

		compareObject = new Tag("AcTiOn");
		assertTrue(action.equals(compareObject));
		
	}
		

}
