package se.chalmers.watchmetest.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.AddMovieActivity;
import se.chalmers.watchme.activity.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class TagListFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

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
		final int ADD_MOVIE_BUTTON = 1;
		
		solo.clickOnText("Movies");
		solo.clickOnActionBarItem(R.id.menu_add_movie);
		solo.enterText(0, "Batman");
		solo.enterText(1, "Action");
		solo.clickOnButton(ADD_MOVIE_BUTTON);
		solo.clickOnText("Tags");
		boolean wasTagFound = solo.searchText("action");
		assertTrue(wasTagFound);
	}

}
