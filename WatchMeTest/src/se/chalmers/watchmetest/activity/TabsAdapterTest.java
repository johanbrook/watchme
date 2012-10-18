package se.chalmers.watchmetest.activity;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TabsAdapter;
import se.chalmers.watchme.ui.MovieListFragment;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;

public class TabsAdapterTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public TabsAdapterTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// TODO: Add mainActivity, actionBar, etc., as instance variables?
		// Lesser repeating of code but bad reusing same stuff for different
		// tests? (think Joacim von Hacht said that)...
	}

	// TODO fix api level
	@TargetApi(11)
	@UiThreadTest
	public void testAddTab() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();
		TabsAdapter tabsAdapter = new TabsAdapter(mainActivity, new ViewPager(
				mainActivity));

		// TODO: Smelly test?
		// better getting empty action bar or clearing action bar from
		// mainactivity?
		// actionBar.removeAllTabs() generated exception
		// Some tabs might have been added already, for example in
		// mainActivity.onCreate()
		int initialNrOfTabs = actionBar.getTabCount();

		int randomNr = (int) (Math.random() * 10 + 1);

		for (int i = 1; i <= randomNr; i++) {
			Tab tab = actionBar.newTab().setText("tab" + i);
			tabsAdapter.addTab(tab, MovieListFragment.class, null);
		}
		// TODO: test tab.getTag()...
		// Returns inner class, how to do?

		// Test if tab was added in the TabsAdapter's internal list
		assertTrue(tabsAdapter.getCount() == randomNr);

		// Test if tab was added in the action bar
		assertTrue(actionBar.getTabCount() == randomNr + initialNrOfTabs);

	}

	// TODO: fix api level
	@TargetApi(11)
	@UiThreadTest
	public void testOnTabSelected() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();
		ViewPager viewPager = new ViewPager(mainActivity);
		TabsAdapter tabsAdapter = new TabsAdapter(mainActivity, viewPager);

		Tab tab1 = actionBar.newTab().setText("tab1");
		tabsAdapter.addTab(tab1, MovieListFragment.class, null);

		Tab tab2 = actionBar.newTab().setText("tab2");
		tabsAdapter.addTab(tab2, MovieListFragment.class, null);

		// TODO: possible change if less smelly: add getTabs() in TabsAdapter
		// and
		// use assertTrue(viewPager.getCurrentItem() ==
		// tabsAdapter.getTabs().indexOf(tab2))
		tabsAdapter.onTabSelected(tab2, null);
		assertTrue(viewPager.getCurrentItem() == 1);

		tabsAdapter.onTabSelected(tab1, null);
		assertTrue(viewPager.getCurrentItem() == 0);

	}

	// TODO: fix api level
	@TargetApi(11)
	@UiThreadTest
	public void testGetItem() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();
		TabsAdapter tabsAdapter = new TabsAdapter(mainActivity, new ViewPager(
				mainActivity));
		Tab tab = actionBar.newTab().setText("tab");
		tabsAdapter.addTab(tab, MovieListFragment.class, null);

		assertTrue(tabsAdapter.getItem(0).getClass() == MovieListFragment.class);
	}

	// TODO: fix api level
	@TargetApi(11)
	@UiThreadTest
	public void testGetCount() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();
		TabsAdapter tabsAdapter = new TabsAdapter(mainActivity, new ViewPager(
				mainActivity));

		int randomNr = (int) (Math.random() * 10 + 1);

		for (int i = 0; i < randomNr; i++) {
			Tab tab = actionBar.newTab().setText("tab");
			tabsAdapter.addTab(tab, MovieListFragment.class, null);
		}

		assertTrue(tabsAdapter.getCount() == randomNr);

	}

}
