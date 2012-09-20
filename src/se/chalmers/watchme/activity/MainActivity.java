package se.chalmers.watchme.activity;

import java.io.Serializable;
import java.util.List;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	
	public static final int ADD_MOVIE_REQUEST = 1;
	
	private ArrayAdapter<Serializable> moviesAdapter;
	private DatabaseHandler db;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.moviesAdapter = new ArrayAdapter<Serializable>(this, android.R.layout.simple_list_item_1);
        setListAdapter(this.moviesAdapter);
        
        this.db = new DatabaseHandler(this);
		
        refreshMovieList();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == ADD_MOVIE_REQUEST && resultCode == RESULT_OK) {
    		refreshMovieList();
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setClass(this, AddMovieActivity.class);
        
        startActivityForResult(intent, ADD_MOVIE_REQUEST);
        
        return true;
    }
    
    private void refreshMovieList() {
    	this.moviesAdapter.clear();
    	List<Movie> list = this.db.getAllMovies();
		
    	for(Movie m : list) {
			this.moviesAdapter.add(m);
		}
    }
}
