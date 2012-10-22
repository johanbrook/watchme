package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchme.database.DatabaseHelper;
import se.chalmers.watchmetest.Constants;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class that tests functionality implemented in the MovieDetailsActivity class.
 * Tests are performed with methods from the robotium api.
 * 
 * @author mattiashenriksson
 * 
 */
public class MovieDetailsActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;
	DatabaseHelper dbh;

	public MovieDetailsActivityTest() {
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

	/**
	 * Test if details view shows the correct details of a movie
	 */
	public void testViewDetails() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.waitForActivity("MainActivity");

		// Add movie
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "TEST_MOVIE");
		solo.setProgressBar(Constants.RATING_BAR, 1);
		solo.clickOnText("Pick");
		solo.waitForText("Done");
		solo.setDatePicker(Constants.DATE_PICKER, 2014 - 1, 12, 24);
		solo.clickOnText("Done");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.enterText(Constants.NOTE_FIELD, "Mum said I'd like this");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);

		// Navigate to details view
		solo.waitForActivity("MainActivity");
		solo.clickOnText("TEST_MOVIE");
		boolean isMovieDetailsActivityViewed = solo
				.waitForActivity("MovieDetailsActivity");
		assertTrue(isMovieDetailsActivityViewed);

		// Check if correct details are viewed
		boolean isTitleFound = solo.searchText("TEST_MOVIE");
		assertTrue(isTitleFound);

		RatingBar ratingBar = (RatingBar) solo.getView(R.id.my_rating_bar);
		assertTrue(ratingBar.getRating() == 1);

		TextView releaseDateLabel = (TextView) solo.getView(R.id.releaseDate);
		assertTrue(releaseDateLabel.getText().equals("24 Jan, 2014"));

		EditText noteField = (EditText) solo.getView(R.id.note_field_details);
		assertTrue(noteField.getText().toString()
				.equals("Mum said I'd like this"));

		EditText tagField = (EditText) solo.getView(R.id.tag_field_details);
		assertTrue(tagField.getText().toString().equals("action"));
	}

	/**
	 * Test if it's possible to edit a movie's details
	 */
	public void testEditDetails() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.waitForActivity("MainActivity");
		
		// Add movie
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "TEST_MOVIE");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		
		// Navigate to details view
		solo.waitForActivity("MainActivity");
		solo.clickOnText("TEST_MOVIE");
		solo.waitForActivity("MovieDetailsActivity");

		// Check that correct details are viewed
		RatingBar ratingBar = (RatingBar) solo.getView(R.id.my_rating_bar);
		TextView releaseDateLabel = (TextView) solo.getView(R.id.releaseDate);
		EditText noteField = (EditText) solo.getView(R.id.note_field_details);
		EditText tagField = (EditText) solo.getView(R.id.tag_field_details);
		
		assertTrue(ratingBar.getRating() == 0);
		assertFalse(releaseDateLabel.getText().equals("24 Jan, 2014"));
		assertTrue(noteField.getText().toString().equals(""));
		assertTrue(tagField.getText().toString().equals(""));

		// Edit movie details
		solo.clickOnActionBarItem(R.id.menu_edit);
		solo.setProgressBar(Constants.RATING_BAR, 1);
		solo.clickOnText("Pick");
		solo.waitForText("Done");
		solo.setDatePicker(Constants.DATE_PICKER, 2014 - 1, 12, 24);
		solo.clickOnText("Done");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
		solo.enterText(0, "Action");
		solo.enterText(1, "Mum said I'd like this");
		solo.clickOnText("Save");
		
		// Navigate out from and back to details view
		solo.clickOnActionBarHomeButton();
		solo.waitForActivity("MainActivity");
		solo.clickOnText("TEST_MOVIE");
		solo.waitForActivity("MovieDetailsActivity");
		
		// Check if new details are viewed
		assertTrue(ratingBar.getRating() == 1);
		assertTrue(releaseDateLabel.getText().equals("24 Jan, 2014"));
		assertTrue(noteField.getText().toString()
				.equals("Mum said I'd like this"));
		assertTrue(tagField.getText().toString().equals("action"));
	}
}