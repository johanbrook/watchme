package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchmetest.Constants;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class that tests functionality implemented in MainActivity class.
 * 
 * @author mattiashenriksson
 * 
 */
public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;

	public MainActivityTest() {
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

	// TODO: Name? testOnCreate()?
	// Testing to basic stuff?
	// API fix
	/**
	 * Test if main activity is in correct state.
	 */
	@TargetApi(11)
	public void testState() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();

		// Check if the correct navigation mode is set
		assertTrue(actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS);
	}

	/**
	 * Test if the tab switching functionality is functioning properly.
	 */
	public void testSwitchTabs() {
		MainActivity mainActivity = this.getActivity();

		// Get the id of the view pager handling the tabs
		ViewPager viewPager = (ViewPager) mainActivity
				.findViewById(R.id.vPager);
		int viewPagerId = viewPager.getId();

		solo.assertCurrentActivity("Current activity is not MainActivity",
				MainActivity.class);
		
		// Click Tags-tab and check if it's viewed
		boolean tagListFragmentViewed = solo
				.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
						+ Constants.TAG_LIST_FRAGMENT_VIEW_PAGER_ID);
		solo.clickOnText("Tags");
		assertTrue(tagListFragmentViewed);

		// Click Movies-tab and check if it's viewed
		boolean movieListFragmentViewed = solo
				.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
						+ Constants.MOVIE_LIST_FRAGMENT_VIEW_PAGER_ID);
		solo.clickOnText("Movies");
		assertTrue(movieListFragmentViewed);
	}

}
