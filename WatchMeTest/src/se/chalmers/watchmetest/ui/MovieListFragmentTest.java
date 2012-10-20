package se.chalmers.watchmetest.ui;

import java.util.ArrayList;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.AddMovieActivity;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchmetest.Constants;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

	// TODO: solo.sleep(n) ugly code? better with .waitFor___() method?
	public void testAddMovie() {
		
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.assertCurrentActivity("AddMovieActivity expected",
				AddMovieActivity.class);
		solo.enterText(Constants.TITLE_FIELD, "Batman");
		solo.setProgressBar(Constants.RATING_BAR, 1);
		solo.clickOnText("Pick");
		solo.setDatePicker(Constants.DATE_PICKER, 2013, 12, 24);
		solo.clickOnText("Done");
		solo.enterText(Constants.TAG_FIELD, "Action");
		solo.enterText(Constants.NOTE_FIELD, "Mum said I'd like this");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		boolean movieFound = solo.searchText("Batman");
		assertTrue(movieFound);
	}

	// TODO: Can you be sure that the application is in the state as when the
	// last test was done? In that case, first rows can be removed.
	public void testRemoveMovie() {
		final int ADD_MOVIE_BUTTON = 1;

		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "1_TEST_MOVIE");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickLongOnText("1_TEST_MOVIE");

		boolean dialogAppeared = solo
				.searchText("Are you sure you want to delete \"1_TEST_MOVIE\"?");
		assertTrue(dialogAppeared);

		solo.clickOnText("Cancel");
		boolean expected = true;
		boolean actual = solo.searchText("1_TEST_MOVIE");
		assertEquals("1_TEST_MOVIE was not found", expected, actual);

		solo.clickLongOnText("1_TEST_MOVIE");
		solo.clickOnText("Yes");

		// Delay. Gives 1_TEST_MOVIE time to disappear 
		solo.sleep(5000);

		expected = false;
		actual = solo.searchText("1_TEST_MOVIE");
		assertEquals("1_TEST_MOVIE was found", expected, actual);
	}

	public void testClickOnMovie() {
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(Constants.TITLE_FIELD, "2_TEST_MOVIE");
		solo.clickOnButton(Constants.ADD_MOVIE_BUTTON);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("2_TEST_MOVIE");
		solo.assertCurrentActivity("MovieDetailsActivity expected",
				MovieDetailsActivity.class);
	}

	// TODO: add date sorting. hard to compare dates..
	public void testSort() {
		final int ADD_MOVIE_BUTTON = 1;

		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");

		// Add three movies with different titles, rating and dates
		char movieFirstCharacter = 'A';
		int year = 2012;
		int rating = 1;
		for (int i = 0; i < 3; i++) {
			solo.clickOnActionBarItem(R.id.menu_add_movie);
			solo.enterText(Constants.TITLE_FIELD, movieFirstCharacter + "_MOVIE");
			solo.setProgressBar(Constants.RATING_BAR, rating);
			solo.clickOnText("Pick");
			solo.setDatePicker(Constants.DATE_PICKER, year, 12, 24);
			solo.clickOnText("Done");
			solo.clickOnButton(ADD_MOVIE_BUTTON);
			solo.sleep(2000);

			movieFirstCharacter++;
			year++;
			rating++;
		}

		solo.clickOnActionBarItem(R.id.menu_sort_button);
		boolean dialogAppeared = solo.searchText("Order by");
		assertTrue(dialogAppeared);

		solo.clickOnText("Title");
		solo.sleep(2000);

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
			}
		}

		assertTrue(areMoviesInCorrectOrder);

		solo.clickOnActionBarItem(R.id.menu_sort_button);
		solo.clickOnText("Rating");
		solo.sleep(2000);

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
			}
		}

		assertTrue(areMoviesInCorrectOrder);

	}

}