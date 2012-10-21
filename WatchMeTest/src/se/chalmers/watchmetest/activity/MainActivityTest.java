package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchmetest.Constants;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

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
	@TargetApi(11)
	public void testState() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();

		assertTrue(actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS);
	}

	public void testSwitchTabs() {
		MainActivity mainActivity = this.getActivity();
		ViewPager viewPager = (ViewPager) mainActivity
				.findViewById(R.id.vPager);
		int viewPagerId = viewPager.getId();

		solo.assertCurrentActivity("Current activity is not MainActivity",
				MainActivity.class);

		boolean tagListFragmentViewed = solo
				.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
						+ Constants.TAG_LIST_FRAGMENT_VIEW_PAGER_ID);
		solo.clickOnText("Tags");
		assertTrue(tagListFragmentViewed);

		boolean movieListFragmentViewed = solo
				.waitForFragmentByTag("android:switcher:" + viewPagerId + ":"
						+ Constants.MOVIE_LIST_FRAGMENT_VIEW_PAGER_ID);
		solo.clickOnText("Movies");
		assertTrue(movieListFragmentViewed);
	}

}
