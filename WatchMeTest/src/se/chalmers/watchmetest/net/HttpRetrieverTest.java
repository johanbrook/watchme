package se.chalmers.watchmetest.net;

import java.io.IOException;

import junit.framework.TestCase;
import se.chalmers.watchme.net.HttpRetriever;
import se.chalmers.watchme.net.NoEntityException;

/**
 *	HttpRetrieverTest.java
 *
 *	@author Johan
 */

public class HttpRetrieverTest extends TestCase {
	
	private HttpRetriever http;
	
	public void setUp() throws Exception {
		http = new HttpRetriever();
	}
	
	public void testGetURL() {
		String response;
		try {
			response = http.get("http://google.com");
			assertNotNull(response);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoEntityException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testIncorrectURL() {
		String response;
		try {
			response = http.get("http://www.google.com/404");
			assertNull(response);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoEntityException e) {
			e.printStackTrace();
		}
		
	}
}
