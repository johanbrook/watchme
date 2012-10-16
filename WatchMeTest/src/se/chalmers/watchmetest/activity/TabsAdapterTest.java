package se.chalmers.watchmetest.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.TabsAdapter;
import se.chalmers.watchme.ui.MovieListFragment;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.test.AndroidTestCase;


public class TabsAdapterTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	public TabsAdapterTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		super.setUp();
	}
	
	//TODO fix api level
	@TargetApi(11)
	@UiThreadTest
	public void testAddTab() {
		MainActivity mainActivity = this.getActivity();
		ActionBar actionBar = mainActivity.getActionBar();
		TabsAdapter tabsAdapter = mainActivity.getTabsAdapter();
		
		//Some tabs might have been added already, for example in mainActivity.onCreate()
		int initialNrOfTabs = actionBar.getTabCount();
		
		for (int i = 1; i <= 3 ; i++) {
			Tab tab = actionBar.newTab().setText("tab" + i);
			tabsAdapter.addTab(tab, MovieListFragment.class, null);	
		}
		System.out.println("------------------- " + tabsAdapter.getCount() + " --------------------------");
		//TODO: test tab.getTag()... 
		//Returns inner class, how to do?
		
		//Test if tab was added in the TabsAdapter's internal list
		assertTrue(tabsAdapter.getCount() == 3 + initialNrOfTabs);
		
		//Test if tab was added in the action bar
		assertTrue(actionBar.getTabCount() == 3 + initialNrOfTabs);		
		
	}

}
