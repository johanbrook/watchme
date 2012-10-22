/**
*	ImageCache.java
*
*	<p>A default implementation of a ResponseCache.</p>
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
*	@license MIT
*/

package se.chalmers.watchme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class ImageCache extends ResponseCache {

	private File cacheDir;
	
	/**
	 * Create a new cache in a specified location.
	 * 
	 * @param cacheDir The directory to store the cached files
	 */
	public ImageCache(File cacheDir) {
		this.cacheDir = cacheDir;
	}
	    
	@Override
	public CacheResponse get(URI uri, String s, Map<String, List<String>> headers) throws IOException {
		// Fetch the file we're looking for
		final File file = new File(cacheDir, escape(uri.getPath()));
		
        if (file.exists()) {
            return new CacheResponse() {
                @Override
                public Map<String, List<String>> getHeaders() throws IOException {
                    return null;
                }

                @Override
                public InputStream getBody() throws IOException {
                	// Construct a stream from our cached file
                    return new FileInputStream(file);
                }
            };
        } else {
            return null;
        }
	}
	

    @Override
    public CacheRequest put(URI uri, URLConnection urlConnection) throws IOException {
        final File file = new File(cacheDir, escape(urlConnection.getURL().getPath()));
        
        return new CacheRequest() {
            @Override
            public OutputStream getBody() throws IOException {
                return new FileOutputStream(file);
            }

            @Override
            public void abort() {
                file.delete();
            }
        };
    }

    private String escape(String url) {
       return url.replace("/", "-").replace(".", "-");
    }

}
