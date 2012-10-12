package se.chalmers.watchme.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseAdapter;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
import se.chalmers.watchme.net.IMDBHandler;
import se.chalmers.watchme.net.ImageDownloadTask;
import se.chalmers.watchme.net.MovieSource;
import se.chalmers.watchme.ui.DatePickerFragment;
import se.chalmers.watchme.ui.ImageDialog;
import se.chalmers.watchme.ui.DatePickerFragment.DatePickerListener;
import se.chalmers.watchme.utils.DateTimeUtils;
import se.chalmers.watchme.utils.MovieHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class MovieDetailsActivity extends FragmentActivity implements DatePickerListener {
	
	public static final String MOVIE_EXTRA = "movie";
	
	private Movie movie;
	private MovieSource imdb;
	
	private AsyncTask<String, Void, Bitmap> imageTask;
	
	// Timeout for fetching IMDb info (in milliseconds)
	private final static int IMDB_FETCH_TIMEOUT = 10000;
	
	private ImageView poster;
	
	private ImageDialog dialog;
	
	private DatabaseAdapter db;
	
	private Calendar tempReleaseDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.movie = (Movie) getIntent().getSerializableExtra(MOVIE_EXTRA);
        this.imdb = new IMDBHandler();
        
        this.poster = (ImageView) findViewById(R.id.poster);
        this.poster.setOnClickListener(new OnPosterClickListener());
        
        this.dialog = new ImageDialog(this);
        
        /*
    	 * Create a new image download task for the poster image
    	 */
    	this.imageTask = new ImageDownloadTask(new ImageDownloadTask.TaskActions() {
			
			public void onFinished(Bitmap image) {
				if(image != null) {
					poster.setImageBitmap(image);
				}
			}
		});
    	
        
        /*
         * If no movie id was received earlier then finish this activity before
         * anything else is done
         */
        if(this.movie == null) {
        	// TODO Why does this cause a crash?
        	finish();
        }
        
        // Kick off the fetch for IMDb info IF there's a set API id
        // set.
        if(this.movie.hasApiIDSet()){
        	final AsyncTask<Integer, Void, JSONObject> t = new IMDBTask()
        		.execute(new Integer[] {this.movie.getApiID()});
        	
        	// Cancel the task after a timeout
        	Handler handler = new Handler();
        	handler.postDelayed(new Runnable() {
				
				public void run() {
					if(t.getStatus() == AsyncTask.Status.RUNNING) {
						t.cancel(true);
						System.err.println("Fetching IMDb info did timeout");
					}
				}
			}, IMDB_FETCH_TIMEOUT);
        }
        
        // Populate various view fields from the Movie object
        populateFieldsFromMovie(this.movie);
        
        tempReleaseDate = this.movie.getDate();
        
    }
    
	/**
	 * Populate various view fields with data from a Movie.
	 * 
	 * @param m The movie to fill the fields with
	 */
    public void populateFieldsFromMovie(Movie m) {
    	db = new DatabaseAdapter(this.getContentResolver());
    	
		setTitle(m.getTitle());
		
		TextView noteField = (TextView) findViewById(R.id.note_field_details);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.my_rating_bar);
        TextView tagField = (TextView) findViewById(R.id.tag_field_details);
        TextView releaseDateLabel = (TextView) findViewById(R.id.releaseDate);
		
    	noteField.setText(m.getNote());
        ratingBar.setRating(m.getRating());
        releaseDateLabel.setText(DateTimeUtils.toSimpleDate(m.getDate()));
        
        String tags = MovieHelper.getCursorString(db.getAttachedTags(m));
        tagField.setText(tags.toString());
    }
    
    /*
     * TODO: These JSON-to-Android view parsing is too tight coupled to the
     * Activity I think .. I'd like to put this stuff somewhere else
     * where it's easier to test. 
     */
    
    public void populateFieldsFromJSON(JSONObject json) {
    	TextView rating = (TextView) findViewById(R.id.imdb_rating_number_label);
    	TextView plot = (TextView) findViewById(R.id.plot_content);
    	TextView cast = (TextView) findViewById(R.id.cast_list);
    	TextView duration = (TextView) findViewById(R.id.duration);
    	TextView genres = (TextView) findViewById(R.id.genres);
    	
    	double imdbRating = json.optDouble("rating");
    	if(!Double.isNaN(imdbRating)) {
    		rating.setText(String.valueOf(imdbRating));
    	}
    	
    	String imdbPlot = json.optString("overview");
    	if(!imdbPlot.isEmpty()) {
    		plot.setText(imdbPlot);
    	}
    	
    	int runtime = json.optInt("runtime");
    	if(runtime != 0) {
    		duration.setText(DateTimeUtils.minutesToHuman(runtime));
    	}
    	
    	
    	JSONArray imdbGenres = json.optJSONArray("genres");
    	
    	if(imdbGenres != null && imdbGenres.length() > 0) {
    		String genreString = "";
    		
    		for(int i = 0; i < imdbGenres.length(); i++) {
    			genreString += imdbGenres.optJSONObject(i).optString("name") + ", ";
    		}
    		
    		genres.setText(genreString);
    	}
    	
    	
    	JSONArray posters = json.optJSONArray("posters");
    	String imageURL = MovieHelper.getPosterFromCollection(posters, Movie.PosterSize.MID);
    	
    	// Fetch movie poster
    	this.imageTask.execute(new String[] {imageURL});
    	
    	
    	JSONArray imdbCast = json.optJSONArray("cast");
    	String actors = "";
    	
    	if(imdbCast != null) {
    		for(int i = 0; i < imdbCast.length(); i++) {
    			JSONObject o = imdbCast.optJSONObject(i);
    			if(o.optString("department").equalsIgnoreCase("actors")) {
    				actors += o.optString("name") + ", ";
    			}
    		}
    		
    		cast.setText(actors);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_movie_details, menu);
        
        View itemView = menu.findItem(R.id.toggle_edit).getActionView();
        
        itemView.setOnClickListener(new OnEditClickListener());
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	Log.i("Custom-toggle", "Selected");
    	
    	switch (item.getItemId()) {
        
        	case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            
        	case R.id.toggle_edit:
                
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Click callback. Shows the date picker for a movie's release date
     */
    public void onDatePickerButtonClick(View v) {
		DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(),
        		"datePicker");
	}
    
	public void setDate(Calendar pickedDate) {
		this.tempReleaseDate = pickedDate;
		
		TextView releaseDateLabel = (TextView) findViewById(R.id.releaseDate);
		releaseDateLabel.setText(DateTimeUtils.toSimpleDate(tempReleaseDate));
	}


    /**
     * The IMDb info fetch task.
     * 
     *  <p>This async task calls the IMDb API in order to fetch and
     *  show detailed JSON data from a single movie ID.</p>
     * 
     * @author Johan
     */
    private class IMDBTask extends AsyncTask<Integer, Void, JSONObject> {

    	private ProgressDialog dialog;
    	
    	public IMDBTask() {
    		this.dialog = new ProgressDialog(MovieDetailsActivity.this);
    	}
    	
    	@Override
    	protected void onPreExecute() {
    		this.dialog.setMessage("Loading from IMDb ...");
    		this.dialog.show();
    	}
    	
    	@Override
		public void onCancelled() {
    		this.dialog.dismiss();
    		Toast.makeText(getBaseContext(), 
					"An error occurred while fetching from IMDb", 
					Toast.LENGTH_SHORT)
			.show();
		}
    	
		@Override
		protected JSONObject doInBackground(Integer... params) {
			JSONObject response = imdb.getMovieById(params[0]);
			
			return response;
		}
    	
		@Override
		protected void onPostExecute(JSONObject res) {
			if(this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			
			// Update the UI with the JSON data
			if(res != null) {
				populateFieldsFromJSON(res);
			}
			else {
				Toast.makeText(getBaseContext(), 
						"An error occurred while fetching from IMDb", 
						Toast.LENGTH_LONG)
				.show();
			}
		}
    }
    
    /**
     * Listener class for when user clicks on the poster.
     * 
     *  <p>Gets the bitmap image from the poster and set it to the
     *  custom full screen overlay, then show it.</p>
     * 
     * @author Johan
     */
    private class OnPosterClickListener implements OnClickListener {

		public void onClick(View v) {
			ImageView view = (ImageView) v;
			Bitmap bm = ((BitmapDrawable) view.getDrawable()).getBitmap();
			
			dialog.setImage(bm);
			dialog.show();
		}
    	
    }
    
    /**
     * Listener class for when user clicks the edit toggle button
     * 
     * <p>If the toggle button is checked only the data that is be editable is
     * shown. The remaining data is hidden.</p>
     * 
     * <p>If the toggle button is unchecked everything is shown and the editable
     * data is made uneditable. </p>
     * 
     * @author Robin
     *
     */
    private class OnEditClickListener implements OnClickListener {

		public void onClick(View v) {
			
			Button releaseDateButton = (Button) findViewById(R.id.release_date_button);
			TextView genres = (TextView) findViewById(R.id.genres);
			TextView duration = (TextView) findViewById(R.id.duration);
			
			TextView myRatingLabel = (TextView) findViewById(R.id.my_rating_label);
			RatingBar myRatingBar = (RatingBar) findViewById(R.id.my_rating_bar);
			TextView imdbRatinglabel = (TextView) findViewById(R.id.imdb_rating_label);
			TextView imdbRating = (TextView) findViewById(R.id.imdb_rating_number_label);
			
			EditText tags = (EditText) findViewById(R.id.tag_field_details);
			EditText note = (EditText) findViewById(R.id.note_field_details);
			
			TextView plotTitle = (TextView) findViewById(R.id.plot_title);
			TextView plotContent = (TextView) findViewById(R.id.plot_content);
			TextView castTitle = (TextView) findViewById(R.id.cast_title);
			TextView castList = (TextView) findViewById(R.id.cast_list);
			
			if(((ToggleButton) v).isChecked()) {
				
				releaseDateButton.setEnabled(true);
				myRatingBar.setIsIndicator(false);
				
				/*
				 * setFocusable(true) does not work on EditText if it were
				 * previously set to 'false'. This is a reported bug and at the
				 * time of writing there is no fix. setFocusableInTouchMode(true)
				 * gets the job done for now.
				 */
				tags.setFocusableInTouchMode(true);
				tags.setFocusable(true);
				tags.setEnabled(true);
				note.setFocusableInTouchMode(true);
				note.setFocusable(true);
				note.setEnabled(true);
    			
    		}
    		
    		else {
				
    			releaseDateButton.setEnabled(false);
				myRatingBar.setIsIndicator(true);
				
				/*
				 * Both disallows the user from interacting with the text field
				 * and removes the focus from it (if focus is 'set')  
				 */
				tags.setFocusableInTouchMode(false);
				tags.setFocusable(false);
				tags.setEnabled(false);
				note.setFocusableInTouchMode(false);
				note.setFocusable(false);
				note.setEnabled(false);
				
    		}
			
		}
    	
    }


    
    
    
}
