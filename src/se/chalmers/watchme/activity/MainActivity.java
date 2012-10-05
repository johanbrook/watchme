package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import android.net.Uri;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


// TODO Important! Change minimum required API nr. App needs 11, is set to 8 
@TargetApi(11)
public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public static final int ADD_MOVIE_REQUEST = 1;
	
	private Uri uri = WatchMeContentProvider.CONTENT_URI_MOVIES;
	private SimpleCursorAdapter adapter;
	
	public static final String MOVIE_DETAILS_ID = "se.chalmers.watchme.DETAILS_ID";
	public static final String MOVIE_DETAILS_TITLE = "se.chalmers.watchme.DETAILS_TITLE";
	public static final String MOVIE_DETAILS_RATING = "se.chalmers.watchme.DETAILS_RATING";
	public static final String MOVIE_DETAILS_NOTE = "se.chalmers.watchme.DETAILS_NOTE";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        
        //TODO Add MoviesTable.COLUMN_DATE and android.R.id.date when implemented in database
        String[] from = new String[] { MoviesTable.COLUMN_MOVIE_ID, MoviesTable.COLUMN_TITLE, /* MoviesTable.COLUMN_RATING*/ /*, MoviesTable.COLUMN_DATE*/ };
        int[] to = new int[] { 0 , R.id.title /*, R.id.raiting */ /*, R.id.date */  };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_item_movie , null, from, to, 0);
        setListAdapter(adapter);
		
        this.getListView().setOnItemClickListener(new OnDetailsListener());
        this.getListView().setOnItemLongClickListener(new OnDeleteListener());
    }
    
    /**
     * Callback for getting data from the "Add movie" activity.
     * 
     * On successful creation, add the created Movie object to this list.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == ADD_MOVIE_REQUEST && resultCode == RESULT_OK) {
    		Movie m = (Movie) data.getSerializableExtra("movie");
    		System.out.println(m);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * When the user clicks the 'Add Movie' button in the Action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setClass(this, AddMovieActivity.class);
        
        startActivityForResult(intent, ADD_MOVIE_REQUEST);
        
        return true;
    }
    
    /**
     * Listener for when the user clicks an item in the list
     * 
     * The movie object in the list is used to fill a new activity with data
     * 
     * @author Robin
     */
    private class OnDetailsListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			final long movieId = id;
			
			Cursor movieCursor = getContentResolver().query(uri, null,
					"_id = " + movieId, null, null);
			
			if (movieCursor != null) {
		        movieCursor.moveToFirst();
			}
			
			final Movie movie = new Movie(movieCursor.getString(1));
			movie.setId(movieId);
			movie.setRating(movieCursor.getInt(2));
			movie.setNote(movieCursor.getString(3));
			
			//final Movie movie = (Movie) getListView().getItemAtPosition(arg2);
			Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
			
			// TODO Fetch all data from database in DetailsActivity instead?
			intent.putExtra(MOVIE_DETAILS_ID, movie.getId());
			intent.putExtra(MOVIE_DETAILS_TITLE, movie.getTitle());
			intent.putExtra(MOVIE_DETAILS_RATING, movie.getRating());
			intent.putExtra(MOVIE_DETAILS_NOTE, movie.getNote());
			
			startActivity(intent);
			
		}
    	
    	
    }
    
    /**
     * The listener for when the user does a long-tap on an item in the list.
     * 
     * The Movie object in the list is removed if the user confirms that he wants to remove the Movie.
     * 
     * @author Johan
     */
    private class OnDeleteListener implements OnItemLongClickListener {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			final long movieId = id;
			
			String[] projection = { MoviesTable.COLUMN_TITLE };
			Cursor movieCursor = getContentResolver().query(uri, projection, "_id = " + movieId, null, null);
			
			if (movieCursor != null) {
		        movieCursor.moveToFirst();
			}
			
			final String movieTitle = movieCursor.getString(0);
			
            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
            alertbox.setMessage("Are you sure you want to delete the movie \"" + movieTitle + "\"?");           
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                	getContentResolver().delete(uri, "_id = " + movieId , null);
                    Toast.makeText(getApplicationContext(), "\"" + movieTitle + "\" was deleted" , Toast.LENGTH_SHORT).show();
                }
            });
            alertbox.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    
                }
            });
            
            alertbox.show();
			return true;
		}    	
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { MoviesTable.COLUMN_MOVIE_ID, MoviesTable.COLUMN_TITLE };
	    CursorLoader cursorLoader = new CursorLoader(this,
	        uri, projection, null, null, null);
	    return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);		
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// data is not available anymore, delete reference
	    adapter.swapCursor(null);
		
	}
}
