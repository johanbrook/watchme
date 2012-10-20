/**
*	NotificationClient.java
*
*	The client responsible for handling notifications. This client
*	should be bound to a service. See the methods connectToService()
*	and disconnectFromService().
*
*	@author Johan
*/

package se.chalmers.watchme.notifications;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class NotificationClient {
	
	private Context ctx;
	private NotificationService service;
	private boolean isBound;
	private ServiceConnection connection;

	/**
	 * Create a new client from a context.
	 * 
	 * @param context The context
	 */
	public NotificationClient(Context context) {
		this.ctx = context;
		this.isBound = false;
		
		/**
		 * The connection to the notification service.
		 */
		this.connection = new ServiceConnection() {

			public void onServiceConnected(ComponentName name, IBinder s) {
				service = ((NotificationService.ServiceBinder) s).getService();
				
			}

			public void onServiceDisconnected(ComponentName name) {
				service = null;
			}
			
		};
	}
	
	/**
	 * Initiate the connection to the service. 
	 */
	public void connectToService() {
		Log.i("Custom", "Connecting to service");
		
		// Bind to NotificationService with the connection.
		this.isBound = this.ctx.bindService(
								new Intent(this.ctx, NotificationService.class), 
								this.connection, 
								Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * Disconnect from the service
	 */
	public void disconnectService() {
		if(this.isBound) {
			this.ctx.unbindService(this.connection);
			this.isBound = false;
		}
	}
	
	/**
	 * Set a notification for a movie.
	 * 
	 * @param movie The movie
	 */
	public void setMovieNotification(Notifiable movie) {
		
		if(this.service != null){
			Log.i("Custom", "Set date for notification");
			this.service.setAlarmTaskForMovie(movie);
		}
	}
	
	/**
	 * Cancel a notification for a notifiable object
	 * 
	 * @param obj The object to cancel notification for
	 */
	public void cancelNotification(Notifiable obj) {
		cancelNotification(this.ctx, obj);
	}
	
	/**
	 * Cancel a notification for a notifiable object from
	 * a certain context.
	 * 
	 * @param context The context
	 * @param obj The object to cancel notification for
	 */
	public static void cancelNotification(Context context, Notifiable obj) {
		NotificationManager manager = (NotificationManager) context.
				getSystemService(Context.NOTIFICATION_SERVICE);
		
		int id = obj.getNotificationId();
		manager.cancel(id);
		Log.i("Custom", "** Notification "+id+" was cancelled");
	}
}
