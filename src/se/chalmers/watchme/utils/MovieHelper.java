/**
*	MovieHelper.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;

public class MovieHelper {
	
	private MovieHelper() {}
	
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
	
	/**
	 * Return a String that represents a Cursor.
	 * 
	 * Creates a string with the elements in column 1 (with 0 being the first),
	 * with the elements separated by ","
	 * 
	 * @author lisastenberg
	 * @param cursor The Cursor.
	 * @return a String that represents the cursor.
	 */
	public static String getCursorString(Cursor cursor) {
		String s = "";
		if (cursor.moveToFirst()) {
			s = s + cursor.getString(1);

			while (cursor.moveToNext()) {
				s = s + ", " + cursor.getString(1);
			}
		}
		cursor.close();

		if (s.equals("null")) {
			s = "";
		}
		return s;
	}
	
	/**
	 * Helper method for converting a stringArray with tag names to a list with
	 * Tag objects
	 * 
	 * @param tagStrings The array with tag-titles to be converted to tags
	 * @return A list with Tag objects
	 */
	public static List<Tag> stringArrayToTagList(String[] tagStrings) {
		
		List<Tag> tags = new LinkedList<Tag>();
		
		for(String tagString : tagStrings) {
			if (!tagString.equals("")) {

				/*
				 * Remove whitespaces from the beginning and end of each string
				 * to allow for multi-word tags.
				 */
				tags.add(new Tag(tagString.trim()));
			}
		}
		
		return tags;
	}
}
