package se.chalmers.watchme.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.net.IMDBHandler;
import se.chalmers.watchme.net.MovieSource;
import se.chalmers.watchme.utils.DateTimeUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

// TODO IMPORTANT! Minimum allowed API is 11 by resources used,
// although it is specified as 8. Research and fix it
@TargetApi(11)
public class MovieDetailsActivity extends Activity {
	
	private Movie movie;
	private MovieSource imdb;
	
	private Uri uri_has_tags = WatchMeContentProvider.CONTENT_URI_HAS_TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.movie = (Movie) getIntent().getSerializableExtra("movie");
        this.imdb = new IMDBHandler();
        
        /*
         * If no movie id was received earlier then finish this activity before
         * anything else is done
         */
        if(this.movie == null) {
        	// TODO Why does this cause a crash?
        	finish();
        }
        
        new IMDBTask().execute(new Integer[] {this.movie.getApiID()});
        
        populateFieldsFromMovie(this.movie);
        
    }
    
	
    public void populateFieldsFromMovie(Movie m) {
		setTitle(m.getTitle());
		
		TextView noteField = (TextView) findViewById(R.id.note_field);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.my_rating_bar);
        TextView tagField = (TextView) findViewById(R.id.tag_field);
        TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
		
    	noteField.setText(m.getNote());
        ratingBar.setRating(m.getRating());
        releaseDate.setText(DateTimeUtils.toSimpleDate(m.getDate()));
        
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
    	
    	JSONArray posters = json.optJSONArray("posters");
    	
    	if(posters != null && posters.length() > 0) {
    		String url = null;
    		
    		for(int i = 0; i < posters.length(); i++) {
    			JSONObject image = posters.optJSONObject(i).optJSONObject("image");
    			if(image.optString("size").equals("mid")) {
    				new ImageDownloadTask().execute(new String[] {image.optString("url")});
    				break;
    			}
    		}
    	}
    	
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

    
    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			InputStream in = null;
			try {
				in = (InputStream) new URL(params[0]).getContent();
			} catch (MalformedURLException e) {
				Log.e(getClass().getSimpleName(), "Bad URL format for poster");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(getClass().getSimpleName(), "Error encoding image from URL");
				e.printStackTrace();
			}
			
			return BitmapFactory.decodeStream(in);
		}
		
		@Override
		protected void onPostExecute(Bitmap bm) {
			if(bm != null) {
				ImageView poster = (ImageView) findViewById(R.id.poster);
				poster.setImageBitmap(bm);
				
			}
		}
    	
    }
    
    
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
		protected JSONObject doInBackground(Integer... params) {
			JSONObject response = imdb.getMovieById(params[0]);
			
			return response;
		}
    	
		@Override
		protected void onPostExecute(JSONObject res) {
			if(this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			
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
}
