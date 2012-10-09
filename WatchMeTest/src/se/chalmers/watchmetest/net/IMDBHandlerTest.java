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
	
	public void testGetMovie() {
		JSONArray json = this.imdb.getMoviesByTitle("casino royale");
		
		JSONObject movie = json.optJSONObject(0);
		assertNotNull(movie);
	}
	
	public void testGetNonExistingMovie() {
		JSONArray json = this.imdb.getMoviesByTitle("awdkaowidoawijdwoaijdawoidjaowid");
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
}
