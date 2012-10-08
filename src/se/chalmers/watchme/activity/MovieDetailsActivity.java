package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
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
	
	private TextView noteField;
	private TextView tagField;
	private RatingBar ratingBar;
	
	private Uri uri_has_tags = WatchMeContentProvider.CONTENT_URI_HAS_TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.movie = (Movie) getIntent().getSerializableExtra("movie");
         
        /*
         * If no movie id was received earlier then finish this activity before
         * anything else is done
         */
        if(this.movie == null) {
        	// TODO Why does this cause a crash?
        	finish();
        }
        
        initUIControls();
        populateFieldsFromMovie(this.movie);
        
    }
    
    private void initUIControls() {
    	this.noteField = (TextView) findViewById(R.id.note_field);
        this.ratingBar = (RatingBar) findViewById(R.id.my_rating_bar);
        this.tagField = (TextView) findViewById(R.id.tag_field);
	}

	
    public void populateFieldsFromMovie(Movie m) {
		setTitle(m.getTitle());
		
    	noteField.setText(m.getNote());
        ratingBar.setRating(m.getRating());
        
        Cursor tagCursor = getContentResolver().query(uri_has_tags, null,
				"_id = " + m.getId(), null, null);
        
        String tags = "";
		if (tagCursor.moveToFirst()) {
	        tags = tagCursor.getString(3);
	        while(tagCursor.moveToNext()) {
	        	tags += tagCursor.getString(3) + ", ";
	        }
		}
		
		tagField.setText(tags);
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
