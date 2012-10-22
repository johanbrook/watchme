/**
*	NotificationService.java
*
*	The service class which provides a server functionality for
*	clients to connect to.
*
*	@author Johan
*	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
*	@license MIT
*/

package se.chalmers.watchme.notifications;

import java.io.Serializable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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
	public void setAlarmTaskForMovie(final Notifiable movie) {
		Log.i("Custom", "Set alarm");
		
		final AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		// Start a new task for the alarm on another thread (separated from the UI thread)
		(new Runnable() {
			
			public void run() {
				Log.i("Custom", "Run alarm task");
				// Create a new intent to send to the NotifyService class
				Intent intent = new Intent(NotificationService.this, NotifyService.class);

				// Add extra data
				intent.putExtra(NotifyService.INTENT_NOTIFY, true);
				intent.putExtra(NotifyService.INTENT_MOVIE, (Serializable) movie);
				
				PendingIntent pending = PendingIntent.getService(NotificationService.this, 0, intent, 0);
				
				// Set the alarm, along with the pending intent to call when triggered
				manager.set(AlarmManager.RTC, movie.getDateInMilliSeconds(), pending);
			}
		}).run();
	}
}
