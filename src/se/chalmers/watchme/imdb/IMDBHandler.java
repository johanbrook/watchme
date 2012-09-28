/**
*	IMDBHandler.java
*
*	@author Johan
*/

package se.chalmers.watchme.imdb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.watchme.http.HttpRetriever;

public class IMDBHandler {
	
	public static final String API_URL = "http://api.themoviedb.org/2.1/";
	public static final String SEARCH_METHOD = "Movie.search/";
	public static final String LANGUAGE = "en/";
	public static final String JSON_FORMAT = "json/";
	
	private static final String API_KEY = "6af2e4697c90e9c6e4a8f2434eb3c5fe";
	
	private HttpRetriever http = new HttpRetriever();
	
	private String buildURL(String query) {
		StringBuilder s = new StringBuilder();
		
		try {
			s.append(API_URL)
				.append(SEARCH_METHOD)
				.append(LANGUAGE)
				.append(JSON_FORMAT)
				.append(API_KEY)
				.append("/")
				.append(URLEncoder.encode(query, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return s.toString();
	}
	
	public JSONArray searchForMovieTitle(String title) {
		final String url = buildURL(title);
		String response = this.http.get(url);
		JSONArray movies = null;
		
		try{
			movies = new JSONArray(response);
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		return movies;
	}
	
	
}
