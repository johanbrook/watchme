package se.chalmers.watchmetest.ui;

import java.util.ArrayList;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.AddMovieActivity;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MovieListFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;

	public MovieListFragmentTest() {
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
	
	public void testAddMovie() {
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.assertCurrentActivity("AddMovieActivity expected",
				AddMovieActivity.class);
		solo.enterText(0, "Batman");
		// TODO: Set Raiting
		solo.clickOnText("Pick");
		solo.setDatePicker(0, 2013, 12, 24);
		solo.clickOnText("Done");
		solo.enterText(1, "Action");
		solo.enterText(2, "Mum said I'd like this");
		solo.clickOnButton(1);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		boolean movieFound = solo.searchText("Batman");
		assertTrue(movieFound);
	}

	// TODO: Can you be sure that the application is in the state as when the
	// last test. In that case you can remove first lines.
	public void testRemoveMovie() {
		final int ADD_MOVIE_BUTTON = 1;

		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(0, "TEST_MOVIE");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickLongOnText("TEST_MOVIE");

		boolean dialogAppeared = solo
				.searchText("Are you sure you want to delete \"TEST_MOVIE\"?");
		assertTrue(dialogAppeared);

		solo.clickOnText("Cancel");
		boolean expected = true;
		boolean actual = solo.searchText("TEST_MOVIE");
		assertEquals("TEST_MOVIE was not found", expected, actual);

		solo.clickLongOnText("TEST_MOVIE");
		solo.clickOnText("Yes");
		
		// Delay. Gives TEST_MOVIE time to disappear 
		solo.sleep(5000);

		expected = false;
		actual = solo.searchText("TEST_MOVIE");
		assertEquals("TEST_MOVIE was found", expected, actual);
	}

	public void testClickOnMovie() {
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(0, "TEST_MOVIE_2");
		solo.clickOnButton(1);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("TEST_MOVIE_2");
		solo.assertCurrentActivity("MovieDetailsActivity expected",
				MovieDetailsActivity.class);
	}
	
}