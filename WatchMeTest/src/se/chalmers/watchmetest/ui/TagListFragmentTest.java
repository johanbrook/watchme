package se.chalmers.watchmetest.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TagMovieListActivity;
import se.chalmers.watchme.database.DatabaseHelper;
import se.chalmers.watchmetest.Constants;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class TagListFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;
	DatabaseHelper dbh;

	public TagListFragmentTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		dbh = new DatabaseHelper(getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		// finish all activities that have been opened during test execution.
		solo.finishOpenedActivities();
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
	}

	public void testAddTag() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.clickOnText("Tags");
		boolean wasTagFound = solo.searchText("action");
		assertTrue(wasTagFound);
	}

	public void testClickTag() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);

		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "Dead poet society");
		solo.enterText(Constants.TAG_FIELD, "Drama");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);

		solo.clickOnText("Tags");
		solo.clickOnText("drama");

		solo.assertCurrentActivity("TagMovieListActivity expected",
				TagMovieListActivity.class);
		boolean wasDramaMoviesFound = solo.searchText("Dead poet society");
		boolean wasActionMoviesFound = solo.searchText("Batman");
		assertTrue(wasDramaMoviesFound);
		assertFalse(wasActionMoviesFound);
	}

	public void testDeleteTag() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
		ViewPager viewPager = (ViewPager) getActivity().findViewById(
				R.id.vPager);
		int viewPagerId = viewPager.getId();

		solo.waitForActivity("MainActivity");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.waitForActivity("MainActivity");

		solo.clickOnText("Tags");
		solo.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
				+ Constants.TAG_LIST_FRAGMENT_VIEW_PAGER_ID);
		solo.clickLongOnText("action");

		boolean dialogAppeared = solo
				.waitForText("Are you sure you want to delete \"action\"?");
		assertTrue(dialogAppeared);

		solo.clickOnText("Cancel");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
		boolean expected = true;
		boolean actual = solo.searchText("action");
		assertEquals("action-tag was not found", expected, actual);

		solo.clickLongOnText("action");
		solo.waitForText("Are you sure you want to delete \"action\"?");
		solo.clickOnText("Yes");
		expected = true;
		actual = solo.waitForText("action");
		assertEquals("action-tag was found", expected, actual);
	}

}
