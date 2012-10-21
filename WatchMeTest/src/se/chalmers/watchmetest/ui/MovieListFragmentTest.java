package se.chalmers.watchmetest.ui;

import java.util.ArrayList;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.AddMovieActivity;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchme.database.DatabaseHelper;
import se.chalmers.watchmetest.Constants;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class that tests functionality implemented in the MovieListFragment class.
 * Tests are performed with methods from the robotium api.
 * 
 * @author mattiashenriksson
 * 
 */
public class MovieListFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	Solo solo;
	DatabaseHelper dbh;

	public MovieListFragmentTest() {
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
	 * Test if it's possible to add a movie.
	 * 
	 * Add a movie in the add movie activity and check if it is really added in
	 * the movie list fragment's list.
	 */
	public void testAddMovie() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		// Check pre-conditions
		solo.waitForActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);

		// Input movie data
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.assertCurrentActivity("AddMovieActivity expected",
				AddMovieActivity.class);
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.setProgressBar(Constants.RATING_BAR, 1);
		solo.clickOnText("Pick");
		solo.waitForText("Done");
		solo.setDatePicker(Constants.DATE_PICKER, 2013, 12, 24);
		solo.clickOnText("Done");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.enterText(Constants.NOTE_FIELD, "Mum said I'd like this");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);

		// Check if movie is added in movie list
		solo.waitForActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		boolean movieFound = solo.searchText("Batman");
		assertTrue(movieFound);
	}

	/**
	 * Test if it's possible to remove a movie from movie list fragment's movie
	 * list by performing a "long click" on it.
	 */
	public void testRemoveMovie() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
		
		solo.waitForActivity("MainActivity");

		// Add movie
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "TEST_MOVIE");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		
		solo.waitForActivity("MainActivity");
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		
		// Perform long click on movie
		solo.clickLongOnText("TEST_MOVIE");
		boolean dialogAppeared = solo
				.waitForText("Are you sure you want to delete \"TEST_MOVIE\"?");
		assertTrue(dialogAppeared);

		// Try possibility to cancel remove process 
		solo.clickOnText("Cancel");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
		boolean expected = true;
		boolean actual = solo.searchText("TEST_MOVIE");
		assertEquals("TEST_MOVIE was not found", expected, actual);

		// Remove movie
		solo.clickLongOnText("TEST_MOVIE");
		solo.waitForText("Are you sure you want to delete \"TEST_MOVIE\"?");
		solo.clickOnText("Yes");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);

		// Delay. Gives TEST_MOVIE time to disappear
		solo.sleep(5000);

		expected = false;
		actual = solo.searchText("TEST_MOVIE");
		assertEquals("TEST_MOVIE was found", expected, actual);
	}

	/**
	 * Test if a click on a movie brings you to the movie details view.
	 */
	public void testClickOnMovie() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);

		solo.waitForActivity("MainActivity");
		
		// Add movie
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.waitForActivity("AddMovieActivity");
		solo.enterText(Constants.TITLE_FIELD, "TEST_MOVIE");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		
		solo.waitForActivity("MainActivity");
		
		// Check if brought to details activity
		solo.clickOnText("TEST_MOVIE");
		solo.waitForActivity("MovieDetailsActivity");
		solo.assertCurrentActivity("MovieDetailsActivity expected",
				MovieDetailsActivity.class);
	}

	// TODO: add date sorting. hard to compare dates..
	// TODO: refactor?
	public void testSort() {
		// Clear list of movies stored in database
		dbh.onUpgrade(dbh.getWritableDatabase(), 1, 1);
		
		solo.waitForActivity("MainActivity");

		// Add three movies with different titles, rating and dates
		char movieFirstCharacter = 'A';
		int year = 2012;
		int rating = 1;
		for (int i = 0; i < 3; i++) {
			solo.clickOnActionBarItem(R.id.menu_add_movie);
			solo.waitForActivity("AddMovieActivity");
			solo.enterText(Constants.TITLE_FIELD, movieFirstCharacter
					+ "_MOVIE");
			solo.setProgressBar(Constants.RATING_BAR, rating);
			solo.clickOnText("Pick");
			solo.waitForText("Done");
			solo.setDatePicker(Constants.DATE_PICKER, year, 12, 24);
			solo.clickOnText("Done");
			solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);
			solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
			solo.waitForActivity("MainActivity");

			movieFirstCharacter++;
			year++;
			rating++;
		}

		// Order movies by title
		solo.clickOnActionBarItem(R.id.menu_sort_button);
		boolean dialogAppeared = solo.waitForText("Order by");
		assertTrue(dialogAppeared);
		solo.clickOnText("Title");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);

		// Extract movie titles from text fields found in the
		// MovieListFragment's list.
		View listView = solo.getView(android.R.id.list);
		ArrayList<TextView> textViews = solo.getCurrentTextViews(listView);
		ArrayList<String> movieTitleList = new ArrayList<String>();

		// Every second text view holds a movie title.
		for (int i = 2; i < textViews.size(); i += 2) {
			String movieTitle = textViews.get(i).getText().toString();
			movieTitleList.add(movieTitle);
		}

		// Check if movies really are sorted by title
		boolean areMoviesInCorrectOrder = true;
		for (int i = 1; i < movieTitleList.size(); i++) {
			String thisTitle = movieTitleList.get(i);
			String lastTitle = movieTitleList.get(i - 1);
			if (thisTitle.compareTo(lastTitle) < 0) {
				areMoviesInCorrectOrder = false;
				break;
			}
		}

		assertTrue(areMoviesInCorrectOrder);

		// Order movies by rating
		solo.clickOnActionBarItem(R.id.menu_sort_button);
		solo.waitForText("Order by");
		solo.clickOnText("Rating");
		solo.waitForDialogToClose(Constants.WAIT_FOR_DIALOG_TO_CLOSE_TIME);

		// Extract movie's rating from rating bars found in the
		// MovieListFragment's list.
		ArrayList<ProgressBar> ratingBarList = solo.getCurrentProgressBars();
		ArrayList<Float> movieRatingList = new ArrayList<Float>();

		for (int i = 0; i < ratingBarList.size(); i++) {
			float ratingTemp = ((RatingBar) ratingBarList.get(i)).getRating();
			movieRatingList.add(ratingTemp);
		}

		// Check if movies really are sorted by rating
		areMoviesInCorrectOrder = true;
		for (int i = 1; i < movieRatingList.size(); i++) {
			float thisRating = movieRatingList.get(i);
			float lastRating = movieRatingList.get(i - 1);
			if (thisRating > lastRating) {
				areMoviesInCorrectOrder = false;
				break;
			}
		}

		assertTrue(areMoviesInCorrectOrder);
	}

}