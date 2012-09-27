/**
*	AlarmTask.java
*
*	@author Johan
*/

package se.chalmers.watchme.notifications;

import java.util.Calendar;

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
	
	public AlarmTask(Context context, Movie movie) {
		this.ctx = context;
		this.movie = movie;
		
		// Get the Android alarm service
		this.manager = (AlarmManager) this.ctx.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void run() {
		Log.i("Custom", "Run alarm task");
		Intent intent = new Intent(this.ctx, NotifyService.class);
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
		intent.putExtra(NotifyService.INTENT_MOVIE, movie);
		
		PendingIntent pending = PendingIntent.getService(this.ctx, 0, intent, 0);
		
		this.manager.set(AlarmManager.RTC, this.movie.getDate().getTimeInMillis(), pending);
	}

}
