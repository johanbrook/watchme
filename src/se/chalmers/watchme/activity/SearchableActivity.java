package se.chalmers.watchme.activity;

import java.io.File;
import java.net.ResponseCache;
import java.util.Calendar;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseAdapter;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.GenericCursorLoader;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.net.ImageDownloadTask;
import se.chalmers.watchme.notifications.NotificationClient;
import se.chalmers.watchme.ui.MovieListFragment;
import se.chalmers.watchme.ui.ContentListFragment;
import se.chalmers.watchme.utils.DateTimeUtils;
import se.chalmers.watchme.utils.ImageCache;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

//TODO Important! API level required does not match with what is used
@TargetApi(11)
public class SearchableActivity extends FragmentActivity {

	private ContentListFragment fragment;
	
	/*
	 * Receiving the query
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        
        fragment = new MovieListFragment();
        Bundle b = new Bundle();
        
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			b.putString(getString(R.string.search), query);
		}
        
        fragment.setArguments(b);
        
        ft.add(android.R.id.content, fragment);
        ft.commit();
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_button).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
    	switch(item.getItemId()) {
    	case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
    	
    	case R.id.menu_add_movie:
    		Intent intent = new Intent(this, AddMovieActivity.class);
            startActivity(intent);
            return true;
    		
    	case R.id.menu_search_button:
            onSearchRequested();
            return true;
            
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
