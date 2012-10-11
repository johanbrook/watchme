package se.chalmers.watchme.activity;

import se.chalmers.watchme.ui.MovieListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TagMovieListActivity extends FragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        
        ft.add(android.R.id.content, new MovieListFragment());
        ft.commit();
	}
	
}
