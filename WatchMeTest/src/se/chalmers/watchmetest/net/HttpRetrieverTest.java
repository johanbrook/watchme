package se.chalmers.watchmetest.net;

import junit.framework.TestCase;
import se.chalmers.watchme.net.HttpRetriever;

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
		String response = http.get("http://google.com");
		
		assertNotNull(response);
	}
	
	public void testIncorrectURL() {
		String response = http.get("http://www.google.com/404");
		
		assertNull(response);
	}
}
