/**
*	ImageDownloadTask.java
*
*	Async task for downloading the movie's poster. Using a ResponseCache
*	to cache the images downloaded.
*
*	@see ImageCache
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

	private TaskActions task;
	
	/**
	 * Defines an interface to invoke when this ImageTask
	 * is finished.
	 *  
	 * @author Johan
	 */
	public interface TaskActions {
		/**
		 * The callback which is invoked when the image is
		 * done downloading. The image is available as a parameter.
		 * 
		 * @param image The image as a Bitmap
		 */
		public void onFinished(Bitmap image);
	}
	
	/**
	 * Create a new download task.
	 * 
	 * @param task The action to invoke when task is finished
	 */
	public ImageDownloadTask(TaskActions task) {
		this.task = task;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		String url = params[0];
		
		try {
			URL imageURL = new URL(url);
			URLConnection connection = imageURL.openConnection();
			// Use local caches
			connection.setUseCaches(true);
			
			return BitmapFactory.decodeStream(connection.getInputStream());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap bm) {
		this.task.onFinished(bm);
	}
	
}