package se.chalmers.watchme.activity;

import java.util.LinkedList;
import java.util.List;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	public static final int ADD_MOVIE_REQUEST = 1;
	private Uri uri = WatchMeContentProvider.CONTENT_URI;
	
	private ArrayAdapter<Movie> moviesAdapter;
	//private DatabaseHandler db;
	private SimpleCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        
        //this.db = new DatabaseHandler(this);
        
        List<Movie> allMovies = new LinkedList<Movie>();
        //System.out.println("-------------------BEFORE---------");
        //Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        //System.out.println(cursor.toString());
        
        //NEW TRY
        String[] from = new String[] { MoviesTable.COLUMN_MOVIE_ID, MoviesTable.COLUMN_TITLE };
        int[] to = new int[] { 1, 2 };
        
        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1 , null, from, to, 0);
        setListAdapter(adapter);
        
        //SimpleCursorAdapter s = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1 , cursor, from, to, 0);
        
        /*
        if (cursor.moveToFirst()) {
			do {
				Movie movie = new Movie(cursor.getString(1),
						Integer.parseInt(cursor.getString(2)),
						cursor.getString(3));
				movie.setId(Long.parseLong(cursor.getString(0)));
				allMovies.add(movie);
			} while (cursor.moveToNext());
		}
        
        this.moviesAdapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, allMovies);
        setListAdapter(this.moviesAdapter);
        */
		
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
    		//this.moviesAdapter.add((Movie) data.getSerializableExtra("movie"));
    		Movie m = (Movie) data.getSerializableExtra("movie");
    		System.out.println(m);
    		adapter.notifyDataSetChanged();
    		/*
    		Movie m = (Movie) data.getSerializableExtra("movie");
    		String[] projection = new String[] { MoviesTable.COLUMN_MOVIE_ID, MoviesTable.COLUMN_TITLE };
    		adapter = getContentResolver().query(Uri.withAppendedPath(uri,
                    String.valueOf(m.getId())), projection, null, null, null);
                    */
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
     * The listener for when the user does a long-tap on an item in the list.
     * 
     * The Movie object in the list is removed if the user confirms that he wants to remove the Movie.
     * 
     * @author Johan
     */
    private class OnDeleteListener implements OnItemLongClickListener {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			final long movieId = id;
			
			//getContentResolver().query(uri, projection, selection, null, null);
			
			// TODO: We don't want to maintain two different datasets (the DB and the list adapter).
			// Make the adapter somehow listen to the DB instead.
			// Check out the use of Cursors here: http://developer.android.com/guide/topics/ui/binding.html
			// And we'll perhaps in the future use Content Providers instead.
			
            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
            alertbox.setMessage("Are you sure you want to delete the movie?");           
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                	//db.deleteMovie(movie);
                	getContentResolver().delete(uri, "_id = " + movieId , null);
                    Toast.makeText(getApplicationContext(), "The Movie was deleted" , Toast.LENGTH_SHORT).show();
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
