package se.chalmers.watchmetest.modeltest;

import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
import android.test.AndroidTestCase;

/**
 * This class tests methods in se.chalmers.watchme.model.Movie
 * @author mattiashenriksson
 *
 */

public class MovieTest extends AndroidTestCase {
	
	public MovieTest() {
		super();
	}
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testAddTag() {
		Movie batman = new Movie("batman");
		Tag action = new Tag("action");
		batman.addTag(action);
		assertTrue(batman.getTags().contains(action));
	}
	
	public void testRemoveTag() {
		Movie batman = new Movie("batman");
		Tag action = new Tag("action");
		batman.addTag(action);
		batman.removeTag(action);
		assertFalse(batman.getTags().contains(action));
	}
	
	public void testGetTags() {
		Movie batman = new Movie("batman");
		Tag action = new Tag("action");
		batman.addTag(action);
		assertTrue(batman.getTags().size() == 1);
	}
	
	public void testGetId() {
		Movie batman = new Movie("batman");
		batman.setId(1);
		assertTrue(batman.getId() == 1);
	}
	
	public void testGetTitle() {
		Movie batman = new Movie("batman");
		assertTrue(batman.getTitle().equals("batman"));
	}
	
	public void testGetNote() {
		Movie batman = new Movie("batman", 1, "note");
		assertTrue(batman.getNote().equals("note"));
	}
	
	public void testSetNote() {
		Movie batman = new Movie("batman");
		batman.setNote("note");
		assertTrue(batman.getNote().equals("note"));
	}
	
	public void testGetRaiting() {
		Movie batman = new Movie("batman", 5, "note");
		assertTrue(batman.getRating() == 5);
	}
	
	public void testSetRaiting() {
		Movie batman = new Movie("batman");
		batman.setRating(8);
		assertTrue(batman.getRating() == 8);
	}
	
	public void testEquals() {
		Movie batman = new Movie("batman");
		Movie compareObject = null; 
		assertFalse(batman.equals(compareObject));
		
		/*
		 * Test that Movie is not equal to possible subclasses
		 */
		class ActionMovie extends Movie {
			public ActionMovie(String title) {
				super(title);
			}			
		}		
		compareObject = new ActionMovie("batman");
		assertFalse(batman.equals(compareObject));
		
		compareObject = new Movie("spiderman");
		assertFalse(batman.equals(compareObject));

		compareObject = new Movie("batman");
		assertTrue(batman.equals(compareObject));
		
	}
}
