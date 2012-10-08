package se.chalmers.watchmetest.httptest;

import junit.framework.TestCase;
import se.chalmers.watchme.net.HttpRetriever;
import android.test.suitebuilder.annotation.SmallTest;

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
	
	
	@SmallTest
	public void testGetURL() {
		String response = http.get("http://google.com");
		
		assertNotNull(response);
	}
}
