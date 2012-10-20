package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchmetest.Constants;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MovieDetailsActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;

	public MovieDetailsActivityTest() {
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
	
	public void testViewDetails() {
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "3_TEST_MOVIE");
		solo.setProgressBar(Constants.RATING_BAR, 1);
		solo.clickOnText("Pick");
		solo.setDatePicker(Constants.DATE_PICKER, 2014 - 1, 12, 24);
		solo.clickOnText("Done");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.enterText(Constants.NOTE_FIELD, "Mum said I'd like this");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.clickOnText("3_TEST_MOVIE");
		solo.assertCurrentActivity("MovieDetailsActivty expected", MovieDetailsActivity.class);
		
		boolean isTitleFound = solo.searchText("3_TEST_MOVIE");
		assertTrue(isTitleFound);
		
		RatingBar ratingBar = (RatingBar) solo.getView(R.id.my_rating_bar);
		assertTrue(ratingBar.getRating() == 1);
		
		TextView releaseDateLabel = (TextView) solo.getView(R.id.releaseDate);
		assertTrue(releaseDateLabel.getText().equals("24 Jan, 2014"));
		
		EditText noteField = (EditText) solo.getView(R.id.note_field_details);
		assertTrue(noteField.getText().toString().equals("Mum said I'd like this"));

		EditText tagField = (EditText) solo.getView(R.id.tag_field_details);
		assertTrue(tagField.getText().toString().equals("action"));
	}
}