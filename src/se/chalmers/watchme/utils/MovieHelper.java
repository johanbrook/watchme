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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.chalmers.watchme.model.Movie;

public class MovieHelper {
	
	private MovieHelper() {}
	
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
	 * Get the URL for a poster from a JSONArray of poster objects.
	 * 
	 * <p>Since Java lacks sane collection methods like select, map, etc al,
	 * we have to do this by ourselves.</p>
	 * 
	 * <p>From a JSONArray of posters, get the *first* URL that matches the 
	 * <code>size</code> parameter.</p>
	 * 
	 * @param posters A non-null JSONArray of posters. Assumes the JSONArray is
	 * organized as <code>image</code> objects with the keys <code>size</code>
	 * and <code>url</code>.
	 * @param size The desired size
	 * @return A URL as string with the first matching poster size. Otherwise null.
	 */
	public static String getPosterFromCollection(JSONArray posters, Movie.PosterSize size) {
		String url = null;
		
		if(posters != null && posters.length() > 0) {
    		for(int i = 0; i < posters.length(); i++) {
    			JSONObject image = posters.optJSONObject(i).optJSONObject("image");
    			
    			if(image.optString("size").equals(size.getSize())) {
    				url = image.optString("url");
    				break;
    			}
    		}
    	}
		
		return url;
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
			// Don't forget the ID
			movie.setApiID(o.optInt(Movie.JSON_KEY_ID));
			list.add(movie);
		}
		
		return list;
	}
}
