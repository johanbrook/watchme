/**
*	AlarmTask.java
*
*	Class representing an alarm task for a notification. Responsible for
*	triggering an alarm on a date from a movie object.
*
*	@author Johan
*/

package se.chalmers.watchme.notifications;

import se.chalmers.watchme.model.Movie;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmTask implements Runnable {
	
	private final Context ctx;
	private final Movie movie;
	private final AlarmManager manager;
	
	/**
	 * Create a new alarm task for a movie.
	 * 
	 * @param context The context
	 * @param movie The movie
	 */
	public AlarmTask(Context context, Movie movie) {
		this.ctx = context;
		this.movie = movie;
		
		// Get the Android alarm service
		this.manager = (AlarmManager) this.ctx.getSystemService(Context.ALARM_SERVICE);
	}
	
	/**
	 * Run the alarm task on a specific date from the movie object.
	 */
	public void run() {
		Log.i("Custom", "Run alarm task");
		// Create a new intent to send to the NotifyService class
		Intent intent = new Intent(this.ctx, NotifyService.class);

		// Add extra data
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
		intent.putExtra(NotifyService.INTENT_MOVIE, movie);
		
		PendingIntent pending = PendingIntent.getService(this.ctx, 0, intent, 0);
		
		// Set the alarm, along with the pending intent to call when triggered
		this.manager.set(AlarmManager.RTC, this.movie.getDate().getTimeInMillis(), pending);
	}

}
