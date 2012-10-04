package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.database.DatabaseHandler;
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
	
	private Movie movie;
	private DatabaseHandler database;
	
	private TextView noteField;
	private TextView tagField;
	private RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // TODO Why does getMovie return a long from Movie object but database returns int?
        // Get the movie id. Is set to -1 if no value is returned
        long  movieID = getIntent().getLongExtra(MainActivity.MOVIE_DETAILS, -1);
         
        /*
         * If no movie id was received earlier then finish this activity before
         * anything else is done
         */
        if(movieID == -1) {
        	// TODO Why does this cause a crash?
        	finish();
        }
        
        database = new DatabaseHandler(this);
        movie = database.getMovie((int) movieID);
        
        setContentView(R.layout.activity_movie_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(movie.getTitle());
        
        noteField = (TextView) findViewById(R.id.note_field);
        tagField = (TextView) findViewById(R.id.tag_field);
        ratingBar = (RatingBar) findViewById(R.id.my_rating_bar);
        
        String tags = null;
        for(Tag tag : movie.getTags()) {
        	tags = tags + ", " + tag.toString();
        }
        
        noteField.setText(movie.getNote());
        tagField.setText(tags);
        
        ratingBar.setRating(movie.getRating());
        
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
