package se.chalmers.watchme.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.id;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class AddMovieActivity extends FragmentActivity implements DatePickerListener {
	
	private TextView dateField;
	private TextView tagField;
	private TextView noteField;
	
	private TextView titleField;
	
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
        this.tagField = (TextView) findViewById(R.id.tag_field);
        this.noteField = (TextView) findViewById(R.id.note_field);
        
        this.db = new DatabaseHandler(this);
        
        // Create a notification client and hook up to the notification service
        this.notifications = new NotificationClient(this);
        this.notifications.connectToService();
        
        // Disable add movie button on init
        final Button addButton = (Button) findViewById(R.id.add_movie_button);
        addButton.setEnabled(false);
        
        /**
         * Disable "add button" if no Title on Movie has been set.
         */
        this.titleField.addTextChangedListener(new TextWatcher() {
        	
        	public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(s.toString().equals("")) {
            		addButton.setEnabled(false);
            	} else {
            		addButton.setEnabled(true);
            	}
            }

			public void afterTextChanged(Editable arg0) {
				// Empty. Needs to be here
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Empty. Needs to be here
				
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
    	
    	// TODO Better suited list for tags?
		List<Tag> newTags = new ArrayList<Tag>();
		
		/* 
		 * Split the text input into separate strings input at
		 * commas (",") from tag-field
		 */
		String [] tagStrings = tagField.getText().toString().split(",");
		
		for(String tagString : tagStrings) {
			
			/* Remove whitespaces from the beginning and end of each
			 * string to allow for multi-word tags.
			 */
			newTags.add(new Tag(tagString.trim()));
		}
		
		/*
		 * Extract the rating from the ratingBar and convert it to
		 * an integer
		 */
		RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar); 
		int rating = (int) ratingBar.getRating();
		
		Movie movie = new Movie(movieTitle, releaseDate, rating, movieNote);
		
		db.addMovie(movie);
		
		Intent home = new Intent(this, MainActivity.class);
		setResult(RESULT_OK, home);
		home.putExtra("movie", movie);
		
		// Set a notification for the date picked
    	setNotification(movie);
    }
    
    /**
     * Queue a notification for the added movie
     * 
     * @param movie The movie
     */
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
    
    /**
     * Click callback. Shows the date picker for a movies release date
     */
    public void onDatePickerButtonClick(View v) {
		DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(),
        		"datePicker");
	}

}
