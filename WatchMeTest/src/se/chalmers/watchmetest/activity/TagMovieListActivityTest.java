package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TagMovieListActivity;
import se.chalmers.watchme.ui.MovieListFragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;

public class TagMovieListActivityTest extends
		ActivityInstrumentationTestCase2<TagMovieListActivity> {

	public TagMovieListActivityTest() {
		super(TagMovieListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test if code in TagMovieListActivity.onCreate() is working properly, i.e.
	 * that the tag id that is sent into the activity with an intent is passed
	 * on to the MovieListFragment that is created there.
	 */
	public void testOnCreate() {
		Long randomTagId = (long) (Math.random() * 10000);

		// Inject intent with a tag id into activity
		Intent intent = new Intent();
		intent.putExtra(MainActivity.TAG_ID, randomTagId);
		this.setActivityIntent(intent);
		
		TagMovieListActivity tmla = this.getActivity();
		
		// Get fragment created in activity
		FragmentManager fragmentManager = tmla.getSupportFragmentManager();
		MovieListFragment movieListFragment = (MovieListFragment) fragmentManager
				.findFragmentById(android.R.id.content);

		assertTrue(randomTagId == movieListFragment.getTagId());
	}

}
