/**
 *	SearchableActivity.java
 *
 * The Activity that receives the search query, and then forwards to its 
 * fragment to search the data, and then displays the search results.
 *
 *	@author lisastenberg
 *	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
 *	@license MIT
 */

package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.ui.MovieListFragment;
import se.chalmers.watchme.ui.ContentListFragment;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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

		/*
		 * Get the intent, verify the action and put the query as an argument to
		 * the fragment.
		 */
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			setTitle("Result: " + query);
			
			b.putString(getString(R.string.search), query);
		}

		fragment.setArguments(b);

		ft.add(android.R.id.content, fragment);
		ft.commit();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		/*
		 * Add necessary functionality for the search widget
		 */
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(
				R.id.menu_search_button).getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget;
													// expand it by default

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
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
