package se.chalmers.watchmetest.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TagMovieListActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class TagListFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	// TODO: make public static?
	final int ADD_MOVIE_BUTTON = 1;
	final int TITLE_FIELD = 0;
	final int TAG_FIELD = 1;

	Solo solo;

	public TagListFragmentTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		// finish all activities that have been opened during test execution.
		solo.finishOpenedActivities();
	}
	
	public void testAddTag() {	
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(TITLE_FIELD, "Batman");
		solo.enterText(TAG_FIELD, "Action");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		solo.clickOnText("Tags");
		boolean wasTagFound = solo.searchText("action");
		assertTrue(wasTagFound);
	}

	public void testClickTag() {
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(TITLE_FIELD, "Batman");
		solo.enterText(TAG_FIELD, "Action");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(TITLE_FIELD, "Dead poet society");
		solo.enterText(TAG_FIELD, "Drama");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		
		solo.clickOnText("Tags");
		solo.clickOnText("drama");
		
		solo.assertCurrentActivity("TagMovieListActivity expected", TagMovieListActivity.class);
		boolean wasDramaMoviesFound = solo.searchText("Dead poet society");
		boolean wasActionMoviesFound = solo.searchText("Batman");
		assertTrue(wasDramaMoviesFound);
		assertFalse(wasActionMoviesFound);
	}
	
	public void testDeleteTag() {
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(TITLE_FIELD, "Batman");
		solo.enterText(TAG_FIELD, "Action");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		
		solo.clickOnText("Tags");
		solo.clickLongOnText("action");
		
		boolean dialogAppeared = solo
				.searchText("Are you sure you want to delete \"action\"?");
		assertTrue(dialogAppeared);
		
		solo.clickOnText("Cancel");
		boolean expected = true;
		boolean actual = solo.searchText("action");
		assertEquals("action-tag was not found", expected, actual);
		
		solo.clickLongOnText("action");
		solo.clickOnText("Yes");
		expected = true;
		actual = solo.searchText("action");
		assertEquals("action-tag was found", expected, actual);
	}
	
	

}
