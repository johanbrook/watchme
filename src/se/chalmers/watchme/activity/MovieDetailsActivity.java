package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

// TODO IMPORTANT! Minimum allowed API is 11 by resources used,
// although it is specified as 8. Research and fix it
@TargetApi(11)
public class MovieDetailsActivity extends Activity {
	
	private TextView noteField;
	private RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // TODO Why does getMovie return a long from Movie object but database returns int?
        // Get the movie id. Is set to -1 if no value is returned
        long  movieID = getIntent().getLongExtra(MainActivity.MOVIE_DETAILS_ID, -1);
        
         
        /*
         * If no movie id was received earlier then finish this activity before
         * anything else is done
         */
        if(movieID == -1) {
        	// TODO Why does this cause a crash?
        	finish();
        }
        
        String title = getIntent().getStringExtra(MainActivity.MOVIE_DETAILS_TITLE);
        int rating = getIntent().getIntExtra(MainActivity.MOVIE_DETAILS_ID, -1);
        String note = getIntent().getStringExtra(MainActivity.MOVIE_DETAILS_NOTE);
        
        
        // TODO Fetch data from database (Create movie object from database)
        
        
        
        setContentView(R.layout.activity_movie_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);
        
        noteField = (TextView) findViewById(R.id.note_field);
        ratingBar = (RatingBar) findViewById(R.id.my_rating_bar);
        
        /*
        
		tagField = (TextView) findViewById(R.id.tag_field);
		
        String tags = null;
        for(Tag tag : movie.getTags()) {
        	tags = tags + ", " + tag.toString();
        }
        
        tagField.setText(tags);
        */
        
        noteField.setText(note);
        ratingBar.setRating(rating);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_movie_details, menu);
        return true;
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
