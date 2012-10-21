/**
 *	MovieHelperTest.java
 *
 *	@author Johan Brook
 *	@copyright (c) 2012 Johan Brook
 *	@license MIT
 */

package se.chalmers.watchmetest.util;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

import junit.framework.TestCase;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
import se.chalmers.watchme.utils.MovieHelper;


public class MovieHelperTest extends TestCase {
	
	public void testStringArrayToTagList() {
		
		String[] tagStrings = {"tag1", "tag2", "tag3", "tag4", "tag5"};
		
		List<Tag> tags = MovieHelper.stringArrayToTagList(tagStrings);
		
		for(String tagString : tagStrings) {
			assertTrue(tags.contains(new Tag(tagString)));
		}
		
	}
	
	public void testJsonArrayToMovieList() {
		String json = "[{\""+ Movie.JSON_KEY_NAME +"\": \"James Bond\", "+ 
						"\""+ Movie.JSON_KEY_ID + "\": 202929}]";
		
		try {
			JSONArray array = new JSONArray(json);
			
			assertNotNull(array);
			assertTrue(array.length() == 1);
			
			List<Movie> movies = MovieHelper.jsonArrayToMovieList(array);
			
			assertEquals(array.length(), movies.size());
			
			Movie movie = movies.get(0);
			
			assertEquals("James Bond", movie.getTitle());
			assertEquals(202929, movie.getApiID());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
