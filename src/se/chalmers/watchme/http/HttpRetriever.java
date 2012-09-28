/**
*	HttpRetriever.java
*
*	@author Johan
*/

package se.chalmers.watchme.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRetriever {

	private DefaultHttpClient client = new DefaultHttpClient();
	
	public String get(String url) {
		
		HttpGet request = new HttpGet(url);
		
		try {
			HttpResponse res = this.client.execute(request);
			final int statusCode = res.getStatusLine().getStatusCode();
			
			Log.i("Custom", "Retrieving " + url + ", status: "+statusCode);
			
			if(statusCode != HttpStatus.SC_OK) {
				return null;
			}
			
			HttpEntity entity = res.getEntity();
			
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
