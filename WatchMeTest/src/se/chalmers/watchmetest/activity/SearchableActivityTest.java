package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.database.DatabaseHelper;
import se.chalmers.watchmetest.Constants;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class that tests functionality implemented in the SearchableActivity class.
 * Tests are performed with methods from the robotium api.
 * 
 * @author mattiashenriksson
 * 
 */
public class SearchableActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;
	DatabaseHelper dbh;

	public SearchableActivityTest() {
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
		// Finish all activities that have been opened during test execution.
		solo.finishOpenedActivities();

		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
	}

	/**
	 * Test if you can search for movies and if the application will display the correct result if you do.
	 */
	public void testSearch() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
			
		solo.waitForActivity("MainActivity");

		// Add two movies with different titles
		char movieFirstCharacter = 'A';
		for (int i = 0; i < 2; i++) {
			solo.clickOnActionBarItem(R.id.menu_add_movie);
			solo.waitForActivity("AddMovieActivity");
			solo.enterText(Constants.TITLE_FIELD, movieFirstCharacter
					+ "_MOVIE");
			solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
			solo.waitForActivity("MainActivity");

			movieFirstCharacter++;
		}
		
		// Search for one of the movies
		solo.clickOnActionBarItem(R.id.menu_search_button);
		solo.sleep(2000);
		solo.enterText(0, "A_");
		solo.sendKey(KeyEvent.KEYCODE_ENTER);
			
		boolean isSearchableActivityViewed = solo.waitForActivity("SearchableActivity");
		assertTrue(isSearchableActivityViewed);
			
		boolean isAMovieViewed = solo.searchText("A_MOVIE");
		boolean isBMovieViewed = solo.searchText("B_MOVIE");
		
		// Check that the correct result is displayed
		assertTrue(isAMovieViewed);
		assertFalse(isBMovieViewed);
			
	}
}
