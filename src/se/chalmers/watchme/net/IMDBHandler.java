/**
*	IMDBHandler.java
*
*	Class responsible for making requests to the IMDb API.
*
*	@author Johan
*/

package se.chalmers.watchme.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IMDBHandler implements MovieSource {
	
	/** The TMDb API url */
	public static final String API_URL = "http://api.themoviedb.org/2.1/";
	
	/** The API movie search method */
	private static final String MOVIE_SEARCH = "Movie.search/";
	/** The API movie info method */
	private static final String MOVIE_INFO = "Movie.getInfo/";
	/** The IMDb lookup method */
	private static final String IMDB_INFO = "Movie.imdbLookup/";
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
	private String buildURL(String query, String method) {
		StringBuilder s = new StringBuilder();
		
		try {
			s.append(API_URL)
				.append(method)
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
		final String url = this.buildURL(title, MOVIE_SEARCH);
		String response = this.getResponse(url);
		
		/*
		 * Since the API service doesn't use sane HTTP status codes for
		 * things such as non-existing movies, we have to compare to a 
		 * static string .. 
		 */
		if(response == null || response.indexOf("Nothing found") != -1) {
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
	
	
	/**
	 * Get a movie from the API movie id
	 * 
	 * @param id The API movie id. Get from either getMovieByIMDBID
	 * or getMoviesByTitle
	 * @return A JSONObject representing the movie. If nothing was found,
	 * it returns null
	 */
	public JSONObject getMovieById(int id) {
		final String url = this.buildURL(String.valueOf(id), MOVIE_INFO);
		String response = this.getResponse(url);
		
		return (response == null) ? null : parseStringToJSON(response);
	}
	
	/**
	 * Get a movie from IMDb as a JSON object.
	 * 
	 * @param id The IMDB ID
	 * @return A JSON object representing the movie. If no movie was
	 * found, it returns null
	 */
	public JSONObject getMovieByIMDBID(String id) {
		final String url = this.buildURL(id, IMDB_INFO);
		String response = this.getResponse(url);
		
		return (response == null) ? null : parseStringToJSON(response);
	}
	
	
	/**
	 * Helper function to create a new JSONObject from a string.
	 * 
	 * <p>Note that this function checks if the input string is a JSON
	 * array, e.g. checks for square brackets in the beginning and end. If
	 * the string is an array with a single JSON object, the object is parsed.</p>
	 * 
	 * @param s The input string
	 * @return A JSONObject from the string. Returns null if an error
	 * occurred while parsing the string
	 */
	public static JSONObject parseStringToJSON(String s) {
		JSONObject json = null;
		
		try{
			if(s.charAt(0) == '[' && s.charAt(s.length()-1) == ']') {
				JSONArray ar = new JSONArray(s);
				json = ar.getJSONObject(0);
			}
			else {
				json = new JSONObject(s);
			}
			
		}
		catch(JSONException e) {
			System.err.println("Error while parsing JSONObject");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	/**
	 * Helper method to get a String response from an URL.
	 * 
	 * <p>Catches eventual exceptions.</p>
	 * 
	 * @param url The URL
	 * @return A String response. Returns null if something
	 * went wrong.
	 */
	private String getResponse(String url) {
		String response = null;

		try {
			response = this.http.get(url);
		} catch (IOException e) {
			System.err.print("Error fetching "+url+": ");
			System.err.println(e.getMessage());
		} catch (NoEntityException e) {
			System.err.println(e.getMessage());
		}
		
		return response;
	}
}
