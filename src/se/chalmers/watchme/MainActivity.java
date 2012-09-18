package se.chalmers.watchme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	
	private List<Movie> list;
	private ArrayAdapter<Movie> moviesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Movie movie = (Movie)getIntent().getSerializableExtra("movie");
        
        list = new LinkedList<Movie>();
        list.add(new Movie("Example Movie"));
        
        if (movie != null) {
        	list.add(movie);
        }
        
        moviesAdapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, list);
        setListAdapter(moviesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AddMovieActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        
        return true;
    }
}
