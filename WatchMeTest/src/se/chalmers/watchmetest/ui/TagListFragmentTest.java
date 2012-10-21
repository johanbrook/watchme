package se.chalmers.watchmetest.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TagMovieListActivity;
import se.chalmers.watchme.database.DatabaseHelper;
import se.chalmers.watchmetest.Constants;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class that tests functionality implemented in the TagListFragment class.
 * Tests are performed with methods from the robotium api.
 * 
 * @author mattiashenriksson
 * 
 */
public class TagListFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;
	DatabaseHelper dbh;
	int viewPagerId;

	public TagListFragmentTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		dbh = new DatabaseHelper(getActivity());

		// Get the id of the view pager handling the tabs
		ViewPager viewPager = (ViewPager) getActivity().findViewById(
				R.id.vPager);
		viewPagerId = viewPager.getId();
	}

	@Override
	public void tearDown() throws Exception {
		// Finish all activities that have been opened during test execution.
		solo.finishOpenedActivities();

		// Clear database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
	}

	/**
	 * Test if it's possible to add a tag. Add a movie with a tag and check if
	 * it's viewed in the tag list
	 */
	public void testAddTag() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.waitForActivity("MainActivity");

		// Add movie
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);

		// Check if tag is found in tag list
		solo.waitForActivity("MainActivity");
		solo.clickOnText("Tags");
		solo.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
				+ Constants.TAG_LIST_FRAGMENT_VIEW_PAGER_ID);
		boolean wasTagFound = solo.searchText("action");
		assertTrue(wasTagFound);
	}

	/**
	 * Test if clicking a tag bring you to a list with movies that contains that
	 * tag
	 */
	public void testClickTag() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		// Add two movies with different tags.
		solo.waitForActivity("MainActivity");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.waitForActivity("MainActivity");

		solo.waitForActivity("MainActivity");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "Dead poet society");
		solo.enterText(Constants.TAG_FIELD, "Drama");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.waitForActivity("MainActivity");

		// Navigate to and click on a tag
		solo.clickOnText("Tags");
		solo.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
				+ Constants.TAG_LIST_FRAGMENT_VIEW_PAGER_ID);
		solo.clickOnText("drama");

		// Check if the correct movie was found and that the incorrect movie was
		// not
		solo.waitForActivity("TagMovieListActivity");
		solo.assertCurrentActivity("TagMovieListActivity expected",
				TagMovieListActivity.class);
		solo.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
						+ Constants.MOVIE_LIST_FRAGMENT_VIEW_PAGER_ID);
		boolean wasDramaMoviesFound = solo.searchText("Dead poet society");
		boolean wasActionMoviesFound = solo.searchText("Batman");
		assertTrue(wasDramaMoviesFound);
		assertFalse(wasActionMoviesFound);
	}

	/**
	 * Test if it's possible to delete a tag by long clicking it in the tag list
	 */
	public void testDeleteTag() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.waitForActivity("MainActivity");
		
		// Add movie
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.waitForActivity("MainActivity");
		
		// Navigate to tags list
		solo.clickOnText("Tags");
		solo.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
				+ Constants.TAG_LIST_FRAGMENT_VIEW_PAGER_ID);

		solo.clickLongOnText("action");
		
		// Test possibility to cancel removing tag process
		boolean dialogAppeared = solo
				.waitForText("Are you sure you want to delete \"action\"?");
		assertTrue(dialogAppeared);
		solo.clickOnText("Cancel");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
		boolean expected = true;
		boolean actual = solo.searchText("action");
		assertEquals("action-tag was not found", expected, actual);

		// Remove tag
		solo.clickLongOnText("action");
		solo.waitForText("Are you sure you want to delete \"action\"?");
		solo.clickOnText("Yes");
		expected = true;
		actual = solo.waitForText("action");
		assertEquals("action-tag was found", expected, actual);
	}

}
