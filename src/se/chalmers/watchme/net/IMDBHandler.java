/**
*	IMDBHandler.java
*
*	Class responsible for making requests to the IMDb API.
*
*	@author Johan
*/

package se.chalmers.watchme.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;


public class IMDBHandler implements MovieSource {
	
	/** The TMDb API url */
	public static final String API_URL = "http://api.themoviedb.org/2.1/";
	
	/** The API search method, i.e. Movie search, Person search, etc. */
	private static final String SEARCH_METHOD = "Movie.search/";
	/** Specify language */
	private static final String LANGUAGE = "en/";
	/** Return format */
	private static final String JSON_FORMAT = "json/";
	
	/** The TMDb API key */
	private static final String API_KEY = "6af2e4697c90e9c6e4a8f2434eb3c5fe";
	
	// Initialize the HTTP handler
	private HttpRetriever http = new HttpRetriever();
	
	/**
	 * Build a URL for searching the API from a query.
	 * 
	 * <p><strong>Format:</strong></p>
	 * 
	 * <pre>
	 * <code>http://api.themoviedb.org/2.1/Movie.search/en/json/APIKEY/query</code>
	 * </pre>
	 * 
	 * @param query The query, i.e. a movie title
	 * @return A String with the complete request url
	 */
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
	
	/**
	 * Searching the API for a movie.
	 * 
	 * @param title The movie title
	 * @return A JSONArray with the movies as JSONObjects on success. Otherwise null
	 */
	public JSONArray getMoviesByTitle(String title) {
		final String url = this.buildURL(title);
		String response = this.http.get(url);
		
		/*
		 * Since the API service doesn't use sane HTTP status codes for
		 * things such as non-existing movies, we have to compare to a 
		 * static string .. 
		 */
		if(response.indexOf("Nothing found") != -1) {
			return null;
		}
		
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
