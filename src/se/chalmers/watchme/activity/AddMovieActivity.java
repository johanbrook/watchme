package se.chalmers.watchme.activity;

import java.util.Calendar;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.id;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.notifications.NotificationClient;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;

public class AddMovieActivity extends Activity {
	
	private TextView textField;
	private Button addButton;
	private DatePicker picker;
	
	// The handler to interface with the notification system and scheduler
	private NotificationClient notifications;
	
	// The database handler
	private DatabaseHandler db;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.addButton = (Button) findViewById(R.id.add_movie_button);
        this.textField = (TextView) findViewById(R.id.movie_name_field);
        this.picker = (DatePicker) findViewById(R.id.date_picker);
        
        this.db = new DatabaseHandler(this);
        this.notifications = new NotificationClient(this);
        this.notifications.connectToService();
    }
    
    /**
     * Click callback. Create a new Movie object and set it on
     * the Intent, and then finish this Activity.
     */
    public void onAddButtonClick(View view) {
    	
    	// Set a notification for the date picked
    	
    	int day = this.picker.getDayOfMonth();
    	int month = this.picker.getMonth();
    	int year = this.picker.getYear();
    	
    	Calendar date = Calendar.getInstance();
    	date.set(year, month, day);
    	
    	// Set the timestamp to midnight
    	date.set(Calendar.HOUR_OF_DAY, 0);
    	date.set(Calendar.MINUTE, 0);
    	date.set(Calendar.SECOND, 0);
    	
    	this.notifications.setDateForNotification(date);
    	
    	Toast.makeText(this, "Notification set for " + day + "/" + (month+1) + "/"+year, Toast.LENGTH_SHORT).show();
    	
    	
    	// Create and save a new Movie object
    	
    	Movie movie = new Movie(textField.getText().toString());
		db.addMovie(movie);
		
		Intent home = new Intent(AddMovieActivity.this, MainActivity.class);
		setResult(RESULT_OK, home);
		home.putExtra("movie", movie);
		
		finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_movie, menu);
        return true;
    }
    
    
    @Override
    protected void onStop() {
    	// Disconnect the service (if started) when this activity is stopped.
    	
    	if(this.notifications != null) {
    		this.notifications.disconnectService();
    	}
    	
    	super.onStop();
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
