/**
*	MovieSource.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.net;

import org.json.JSONArray;

public interface MovieSource {
	public JSONArray getMoviesByTitle(String title);
}
