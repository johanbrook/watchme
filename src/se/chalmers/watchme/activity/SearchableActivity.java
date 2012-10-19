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
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

//TODO Important! API level required does not match with what is used
@TargetApi(11)
public class SearchableActivity extends FragmentActivity {

	private DatabaseAdapter db;
	
	private AsyncTask<String, Void, Bitmap> imageTask;

	
	/*
	 * 1. Receiving the Query
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_list_fragment_view);
		// TODO Create R.layout.search?

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			System.out.println("Intent.ACTION_SEARCH");
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
		
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        
        ft.add(android.R.id.content, new MovieListFragment());
        ft.commit();
	}

	/*
	 * 2. Searching the data
	 */
	private void doMySearch(String query) {
		System.out.println("--- doMySearch ---");
		db = new DatabaseAdapter(getContentResolver());
		
		showResults(db.searchForMovies(query));
	}
	
	/*
	 * 3. Presenting the results
	 */
	private void showResults(Cursor result) {
		System.out.println("--- doMySearch ---");
		ContentListFragment fragment = (ContentListFragment) getSupportFragmentManager().findFragmentById(R.id.vPager);
		fragment.showResult(result);
	}
}
