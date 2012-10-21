package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.ui.ContentListFragment;
import se.chalmers.watchme.ui.MovieListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

/**
 * 
 * 
 * @author lisastenberg
 */
public class TagMovieListActivity extends FragmentActivity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        /*
         * Add fragment to this activity and attach the tagId to the fragment
         * which were sent from TagListFragment
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        
        ContentListFragment fragment = new MovieListFragment();
        fragment.setArguments(getIntent().getExtras());
        
        ft.add(android.R.id.content, fragment);
        ft.commit();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem mailItem = menu.findItem(R.id.menu_send_email_button);
		mailItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		/*
		 * Add necessary functionality for the search widget
		 */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_button).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onBackPressed() {
	    this.finish();
	    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	    return;
	}
	
}
