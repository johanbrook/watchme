package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.utils.MovieItemAdapter;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	public static final int ADD_MOVIE_REQUEST = 1;
	
	private MovieItemAdapter moviesAdapter;
	private DatabaseHandler db;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.db = new DatabaseHandler(this);
        
        this.moviesAdapter = new MovieItemAdapter(this, R.layout.list_item_movie, this.db.getAllMovies());
        setListAdapter(this.moviesAdapter);
		
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
    		this.moviesAdapter.add((Movie) data.getSerializableExtra("movie"));
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
			
			final Movie movie = (Movie) getListView().getItemAtPosition(position);
			
			// TODO: We don't want to maintain two different datasets (the DB and the list adapter).
			// Make the adapter somehow listen to the DB instead.
			// Check out the use of Cursors here: http://developer.android.com/guide/topics/ui/binding.html
			// And we'll perhaps in the future use Content Providers instead.
			
            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
            alertbox.setMessage("Are you sure you want to delete " + movie + "?");           
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                	db.deleteMovie(movie);
        			moviesAdapter.remove(movie);
                    Toast.makeText(getApplicationContext(), "The Movie " + movie + " was deleted" , Toast.LENGTH_SHORT).show();
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
}
