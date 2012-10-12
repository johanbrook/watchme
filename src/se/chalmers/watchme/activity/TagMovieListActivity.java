package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseAdapter;
import se.chalmers.watchme.ui.MovieListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class TagMovieListActivity extends FragmentActivity {

	private DatabaseAdapter db;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        db = new DatabaseAdapter(this.getContentResolver());
        
        //Recieve tagId sent from TagListFragment
        Intent intent = getIntent();
        Long tagId = intent.getLongExtra(MainActivity.TAG_ID, -1);
        System.out.println("TAGID: " + tagId);
        Cursor cursor = db.getAttachedMovies(tagId);
        
        System.out.println("CURSORCOUNT: " + cursor.getCount());
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        
        //Put cursor as a parameter to a new MovieListFragment
        MovieListFragment mlf = new MovieListFragment(cursor);
        mlf.onCreate(savedInstanceState);
        ft.add(android.R.id.content, mlf);
        ft.commit();
        //mlf.onLoadFinished(null, cursor);
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
	    System.out.println("--- H€R ----");
	    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	    return;
	}
	
}
