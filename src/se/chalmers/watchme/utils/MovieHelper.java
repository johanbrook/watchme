/**
*	MovieHelper.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.utils;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import se.chalmers.watchme.model.Movie;

public class MovieHelper {
	
	/**
	 * Return the release year from a date on the format
	 * "YYYY-MM-DD".
	 * 
	 * @param longDate The formatted long date
	 * @return The year as a string on the format "YYYY". If the parameter is
	 * not well formated ("YYYY-MM-DD") the parameter is returned untouched.
	 */
	public static String parseYearFromDate(String longDate) {
		if(longDate == null)
			return null;
		
		int index = longDate.indexOf("-");
		return (index != -1) ? longDate.substring(0, index) : longDate; 
	}
	
	/**
	 * Convert a JSONArray to a List
	 * 
	 * @param json The JSONArray to convert
	 * @return A List with the generic types specified
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonArrayToList(JSONArray json) {
		List<T> list = new ArrayList<T>();
		for(int i = 0; i < json.length(); i++) {
			list.add((T) json.opt(i));
		}
		
		return list;
	}
	
	/**
	 * Convert a JSONArray of Movies to a list of Movies
	 * 
	 * <p>Each Movie object is initialized with the attribute
	 * <code>original_name</code> from the input array. The 
	 * attribute <code>imdb_id</code> is also set on the movie.</p>
	 * 
	 * @param input The JSONArray of movies as JSONObjects
	 * @return A List of Movies
	 */
	public static List<Movie> jsonArrayToMovieList(JSONArray input) {
		List<Movie> list = new ArrayList<Movie>();
		
		// Parse the JSON objects and add to list
		for(int i = 0; i < input.length(); i++) {
			JSONObject o = input.optJSONObject(i);
			
			Movie movie = new Movie(o.optString(Movie.JSON_KEY_NAME));
			// Don't forget the IMDB ID
			movie.setImdbID(o.optString(Movie.JSON_KEY_ID));
			list.add(movie);
		}
		
		return list;
	}
}
