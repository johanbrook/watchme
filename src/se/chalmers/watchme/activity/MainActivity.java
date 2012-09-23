package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	
	public static final int ADD_MOVIE_REQUEST = 1;
	
	private ArrayAdapter<Movie> moviesAdapter;
	private DatabaseHandler db;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.db = new DatabaseHandler(this);
        
        this.moviesAdapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, this.db.getAllMovies());
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
     * The Movie object in the list should be removed from the list and database.
     * 
     * @author Johan
     */
    private class OnDeleteListener implements OnItemLongClickListener {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			
			Movie movie = (Movie) getListView().getItemAtPosition(position);
			
			// TODO: We don't want to maintain two different datasets (the DB and the list adapter).
			// Make the adapter somehow listen to the DB instead.
			// Check out the use of Cursors here: http://developer.android.com/guide/topics/ui/binding.html
			// And we'll perhaps in the future use Content Providers instead.
			db.deleteMovie(movie);
			moviesAdapter.remove(movie);
			
			return true;
		}    	
    }
}
