package se.chalmers.watchmetest.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;

/**
 * This class tests methods in se.chalmers.watchme.model.Movie
 * 
 * @author mattiashenriksson
 * 
 */

public class MovieTest extends TestCase {

	private Movie batman;

	private final String DEFAULT_MOVIE_NAME = "batman";
	private final String DEFAULT_TAG_NAME = "action";

	public void setUp() throws Exception {
		super.setUp();
		this.batman = new Movie(DEFAULT_MOVIE_NAME);
	}

	/**
	 * Test if it's possible to add a tag to a movie
	 */
	public void testAddTag() {
		Tag action = new Tag(DEFAULT_TAG_NAME);
		batman.addTag(action);
		assertTrue(batman.getTags().contains(action));
	}

	/**
	 * Test if it's possible to remove a tag from a movie
	 */
	public void testRemoveTag() {
		Tag action = new Tag(DEFAULT_TAG_NAME);
		batman.addTag(action);
		batman.removeTag(action);
		assertFalse(batman.getTags().contains(action));
	}

	/**
	 * Test if it's possible to get a movie's tags
	 */
	public void testGetTags() {
		Tag action = new Tag(DEFAULT_TAG_NAME);
		batman.addTag(action);
		assertTrue(batman.getTags().size() == 1);
	}

	/**
	 * Test if it's possible to get a movie's id
	 */
	public void testGetId() {
		batman.setId(1);
		assertTrue(batman.getId() == 1);
	}

	/**
	 * Test if it's possible to get a movie's title
	 */
	public void testGetTitle() {
		assertTrue(batman.getTitle().equals(DEFAULT_MOVIE_NAME));
	}

	/**
	 * Test if it's possible to get a movie's note
	 */
	public void testGetNote() {
		Movie superman = new Movie("superman", Calendar.getInstance(), 1,
				"note");
		assertTrue(superman.getNote().equals("note"));
	}

	/**
	 * Test if it's possible to set a note on a movie
	 */
	public void testSetNote() {
		batman.setNote("note");
		assertTrue(batman.getNote().equals("note"));
	}

	/**
	 * Test if it's possible to get a movie's rating
	 */
	public void testGetRating() {
		Movie superman = new Movie("superman", Calendar.getInstance(), 5,
				"note");
		assertTrue(superman.getRating() == 5);
	}

	/**
	 * Test if it's possible to set rating on a movie
	 */
	public void testSetRating() {
		batman.setRating(8);
		assertTrue(batman.getRating() == 8);
	}

	/**
	 * Test if you can check if a movie has an api id
	 */
	public void testHasApiID() {
		batman.setApiID(10);
		assertTrue(batman.hasApiIDSet());
	}

	/**
	 * Movie should not have API id set on init
	 */
	public void testHasNotApiID() {
		assertFalse(batman.hasApiIDSet());
	}

	/**
	 * Test setting a movie poster's size
	 */
	public void testMoviePosterSize() {
		batman.setPosterURL("someimage.png", Movie.PosterSize.MID);

		assertEquals("someimage.png", batman.getPosterURL(Movie.PosterSize.MID));
	}

	/**
	 * Test that if a movie's poster isn't set, trying to get it will return
	 * "null"
	 */
	public void testNonExistingPosterSize() {
		assertNull(batman.getPosterURL(Movie.PosterSize.MID));
		assertNull(batman.getPosterURL(Movie.PosterSize.THUMB));
	}

	/**
	 * Test if it's possible to get all of a movie's posters, both the thumbnail
	 * and the full screen sized one, on the correct form
	 */
	public void testGetAllPosterSizes() {
		batman.setPosterURL("someimage1.png", Movie.PosterSize.MID);
		batman.setPosterURL("someimage2.png", Movie.PosterSize.THUMB);

		Map<Movie.PosterSize, String> posters = new HashMap<Movie.PosterSize, String>();
		posters.put(Movie.PosterSize.MID, "someimage1.png");
		posters.put(Movie.PosterSize.THUMB, "someimage2.png");

		assertEquals(posters, batman.getPosterURLs());
	}

	public void testEquals() {
		Movie compareObject = null;
		assertFalse(batman.equals(compareObject));

		// An Movie should not be equal to it's subclasses
		compareObject = new ActionMovie(DEFAULT_MOVIE_NAME);
		assertFalse(batman.equals(compareObject));

		compareObject = new Movie("spiderman");
		assertFalse(batman.equals(compareObject));

		compareObject = new Movie(DEFAULT_MOVIE_NAME);
		assertTrue(batman.equals(compareObject));
	}

	public void testHashCode() {
		Movie otherMovie = new Movie(DEFAULT_MOVIE_NAME);
		Movie notSameMovie = new Movie("spiderman");

		assertTrue(batman.equals(otherMovie));
		assertEquals(batman.hashCode(), otherMovie.hashCode());

		assertFalse(batman.equals(notSameMovie));
		assertFalse(batman.hashCode() == notSameMovie.hashCode());
	}

	/**
	 * Sub class of Movie used to test that a movie isn't equal to it's
	 * subclasses
	 * 
	 * @author mattiashenriksson
	 * 
	 */
	private class ActionMovie extends Movie {
		public ActionMovie(String title) {
			super(title);
		}
	}
}
