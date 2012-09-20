package se.chalmers.watchme.activity;

import java.io.Serializable;

import se.chalmers.watchme.R;
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
	
	private ArrayAdapter<Serializable> moviesAdapter;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.moviesAdapter = new ArrayAdapter<Serializable>(this, android.R.layout.simple_list_item_1);
        setListAdapter(this.moviesAdapter);
        
        this.getListView().setOnItemLongClickListener(new OnDeleteListener());
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == ADD_MOVIE_REQUEST && resultCode == RESULT_OK) {
    		this.moviesAdapter.add(data.getSerializableExtra("movie"));
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
    
    
    private class OnDeleteListener implements OnItemLongClickListener {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			
			Movie movie = (Movie) getListView().getItemAtPosition(position);
			moviesAdapter.remove(movie);
			
			return true;
		}
    	
    }
}
