package se.chalmers.watchmetest.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.AddMovieActivity;
import se.chalmers.watchme.activity.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

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
		//finish all activities that have been opened during test execution.
		solo.finishOpenedActivities();
	}
	
	public void testAddMovie() {
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.assertCurrentActivity("AddMovieActivity expected", AddMovieActivity.class);
		solo.enterText(0, "Batman");
		solo.clickOnText("Pick");
		solo.setDatePicker(0, 2013, 12, 24);
		solo.clickOnText("Done");
		solo.enterText(1, "Action");
		solo.enterText(2, "Mum said I'd like this");
		solo.clickOnButton(1);
		solo.assertCurrentActivity("MainActivity expected", MainActivity.class);
		assertTrue(solo.searchText("Batman"));	
	}

}