/**
*	MovieSource.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
*	@license MIT
*/

package se.chalmers.watchme.net;

import org.json.JSONArray;
import org.json.JSONObject;

public interface MovieSource {
	public JSONArray getMoviesByTitle(String title);
	public JSONObject getMovieByIMDBID(String id);
	public JSONObject getMovieById(int id);
}
