package se.chalmers.watchmetest.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TabsAdapter;
import se.chalmers.watchme.ui.MovieListFragment;
import se.chalmers.watchme.ui.TagListFragment;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	//TODO: API fix
	//similarity to test in TabsAdapter
	//refactor?
	@TargetApi(11)
	@UiThreadTest
	public void testSwitchTabs() {
		MainActivity mainActivity = this.getActivity();
		Tab tab1 = mainActivity.getActionBar().getTabAt(0);
		Tab tab2 = mainActivity.getActionBar().getTabAt(1);
		
		tab2.select();
		
		ViewPager viewPager = (ViewPager) mainActivity.findViewById(R.id.vPager);
		int itemViewedIndex = viewPager.getCurrentItem();
		TabsAdapter tabsAdapter = (TabsAdapter) viewPager.getAdapter();
		Fragment currentlyViewedFragment = (Fragment) tabsAdapter.instantiateItem(viewPager, itemViewedIndex);
		
		assertTrue(currentlyViewedFragment.getClass() == TagListFragment.class);
		
		tab1.select();
		
		itemViewedIndex = viewPager.getCurrentItem();
		currentlyViewedFragment = (Fragment) tabsAdapter.instantiateItem(viewPager, itemViewedIndex);
		
		assertTrue(currentlyViewedFragment.getClass() == MovieListFragment.class);
	}
	
	//TODO: Name? testOnCreate()?
	// Testing to basic stuff?
	//API fix
	@TargetApi(11)
	public void testState() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();
		
		assertTrue(actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS);
	}

}
