/**
*	NotificationService.java
*
*	The service class which provides a server functionality for
*	clients to connect to.
*
*	@author Johan
*/

package se.chalmers.watchme.notifications;

import se.chalmers.watchme.model.Movie;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {
	
	private final IBinder binder = new ServiceBinder();

	/**
	 * Embedded Binder class which provides a reference
	 * to this service. 
	 * 
	 * @author Johan
	 */
	public class ServiceBinder extends Binder {
		public NotificationService getService() {
			return NotificationService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return this.binder;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startID) {
		Log.i("Custom", "Received start id " + startID + ": " + intent);
				
		// This service is sticky - it runs until it's stopped.
		return START_STICKY;
	}
	
	/**
	 * Set a new alarm task for a movie.
	 * 
	 * <p>The task is started on a separate thread.</p>
	 * 
	 * @param movie The movie
	 */
	public void setAlarmTaskForMovie(Movie movie) {
		Log.i("Custom", "Set alarm");
		// Start a new task for the alarm on another thread (separated from the UI thread)
		new AlarmTask(this, movie).run();
	}
}
