/**
*	IMDBHandlerTest.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchmetest.net;

import org.json.JSONArray;
import org.json.JSONObject;

import se.chalmers.watchme.net.IMDBHandler;
import se.chalmers.watchme.utils.MovieHelper;
import junit.framework.TestCase;

public class IMDBHandlerTest extends TestCase {

	IMDBHandler imdb;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		this.imdb = new IMDBHandler();
	}
	
	
	public void testGetMovies() {
		JSONArray json = this.imdb.getMoviesByTitle("casino royale");
		assertNotNull(json);
		assertTrue(json.length() > 0);
	}
	
	public void testGetMovieByTitle() {
		JSONArray json = this.imdb.getMoviesByTitle("casino royale");
		
		JSONObject movie = json.optJSONObject(0);
		assertNotNull(movie);
	}
	
	/**
	 * Get a movie by the API ID (not IMDb ID)
	 */
	public void testGetMovieByID() {
		JSONObject json = this.imdb.getMovieById(187);
		
		assertNotNull(json);
		// Check for a random JSON key
		assertNotNull(json.optString("original_name"));
	}
	
	public void testGetNonExistingMovie() {
		JSONArray json = this.imdb.getMoviesByTitle("awdkaowidoawijdwoaijdawoidjaowid");
		JSONObject movie = this.imdb.getMovieById(-10);
		
		assertNull(movie);
		assertNull(json);
	}
	
	public void testParseStringToJSON() {
		String json = "[{key: \"val\"}]";
		String json2 = "{key: \"val\"}";
		
		JSONObject res = IMDBHandler.parseStringToJSON(json);
		JSONObject res2 = IMDBHandler.parseStringToJSON(json2);
		
		assertNotNull(res);
		assertNotNull(res2);
		assertNotNull(res.optString("key"));
		assertNotNull(res2.optString("key"));
	}
	
	public void testParseStringtoJSONFail() {
		String incorrectJSON = "[{!key: val./??$$}]";
		String incorrectJSON2 = "{!key: val./??$$}";
		
		JSONObject res = IMDBHandler.parseStringToJSON(incorrectJSON);
		JSONObject res2 = IMDBHandler.parseStringToJSON(incorrectJSON2);
		
		assertNull(res);
		assertNull(res2);
		
	}
}
