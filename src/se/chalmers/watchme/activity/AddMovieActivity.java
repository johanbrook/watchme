package se.chalmers.watchme.activity;

import java.util.Calendar;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.id;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.ui.DatePickerFragment;
import se.chalmers.watchme.ui.DatePickerFragment.DatePickerListener;
import se.chalmers.watchme.utils.DateConverter;
import se.chalmers.watchme.notifications.NotificationClient;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;

public class AddMovieActivity extends FragmentActivity implements DatePickerListener {
	
	private TextView dateField;
	private Button datePickerButton;
	private TextView noteField;
	
	private final Context context = this;
	private TextView titleField;
	private DatePicker picker;
	
	// The handler to interface with the notification system and scheduler
	private NotificationClient notifications;
	
	// The database handler
	private DatabaseHandler db;
	
	private Calendar releaseDate;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.releaseDate = Calendar.getInstance();
        
        //TODO Use the XML-value although it is overwritten here?
        this.dateField = (TextView) findViewById(R.id.release_date_label);
        dateField.setText(DateConverter.toSimpleDate(this.releaseDate));
        
        this.titleField = (TextView) findViewById(R.id.title_field);
        this.datePickerButton = (Button) findViewById(R.id.release_date_button);
        this.noteField = (TextView) findViewById(R.id.note_field);
        
        this.db = new DatabaseHandler(this);
        this.notifications = new NotificationClient(this);
        this.notifications.connectToService();
        
        /**
         * Click callback. Shows the date picker for a movies release date
         */
        this.datePickerButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(),
                		"datePicker");
        	}
        });
    }
    
    /**
     * Click callback. Create a new Movie object and set it on
     * the Intent, and then finish this Activity.
     */
    public void onAddButtonClick(View view) {
    	
    	addMovie();
		finish();
    }
    
    private void addMovie() {
    	String movieTitle = this.titleField.getText().toString();
    	String movieNote = this.noteField.getText().toString();
    	
    	Movie movie = new Movie(movieTitle);
    	movie.setNote(movieNote);
    	movie.setDate(this.releaseDate);
    	
		db.addMovie(movie);
		
		Intent home = new Intent(this, MainActivity.class);
		setResult(RESULT_OK, home);
		home.putExtra("movie", movie);
		
		// Set a notification for the date picked
    	setNotification(movie);
    }
    
    
    private void setNotification(Movie movie) {
    	
    	this.notifications.setMovieNotification(movie);
    	Toast.makeText(this, "Notification set for " + DateConverter.toSimpleDate(movie.getDate()), Toast.LENGTH_LONG).show();
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
    
    // @Override is not allowed in Java 1.5 for inherited interface methods
    public void setDate(Calendar pickedDate) {
		
		this.releaseDate = pickedDate;

		dateField.setText(DateConverter.toSimpleDate(this.releaseDate));
		
	}

}
