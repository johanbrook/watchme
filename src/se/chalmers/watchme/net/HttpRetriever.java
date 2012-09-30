/**
*	HttpRetriever.java
*
*	Class responsible for sending simple GET HTTP request
*	to a URL and return the result as a String.
*
*	@author Johan
*/

package se.chalmers.watchme.net;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRetriever {

	// Create a new HTTP client from the Apache library
	private DefaultHttpClient client = new DefaultHttpClient();
	
	/**
	 * Send a GET request to a URL.
	 * 
	 * <p>The <code>url</code> parameter must be well formatted, as
	 * no validations are made in this method.</p>
	 * 
	 * @param url The url
	 * @return String with the response on success, otherwise null
	 */
	public String get(String url) {
		
		HttpGet request = new HttpGet(url);
		
		try {
			HttpResponse res = this.client.execute(request);
			final int statusCode = res.getStatusLine().getStatusCode();
			
			Log.i("Custom", "Retrieving " + url + ", status: "+statusCode);
			
			// Return null if the status code isn't 200 OK
			if(statusCode != HttpStatus.SC_OK) {
				return null;
			}
			
			HttpEntity entity = res.getEntity();
			
			// Return a stringified version of the response
			if(entity != null) {
				return EntityUtils.toString(entity);
			}
		}
		catch(IOException ex) {
			request.abort();
			Log.i("Custom", "Error retrieving " + url);
		}
		
		return null;
	}
	
}
