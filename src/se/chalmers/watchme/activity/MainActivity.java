package se.chalmers.watchme.activity;

import java.util.ArrayList;

import se.chalmers.watchme.R;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.ui.MovieListFragment;
import se.chalmers.watchme.ui.TagListFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends FragmentActivity {
	
	public static final String MOVIE_DETAILS_ID = "se.chalmers.watchme.DETAILS_ID";
	public static final String MOVIE_DETAILS_TITLE = "se.chalmers.watchme.DETAILS_TITLE";
	public static final String MOVIE_DETAILS_RATING = "se.chalmers.watchme.DETAILS_RATING";
	public static final String MOVIE_DETAILS_NOTE = "se.chalmers.watchme.DETAILS_NOTE";
	
	//TODO: Correct to put key values for Intent.putExtra() here? 
	public static final String EXTRA_CURSOR = "se.chalmers.watchme.CURSOR";
	
	private ViewPager viewPager;
	private TabsAdapter tabsAdapter;
	ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setup view pager
        this.viewPager = new ViewPager(this);
        this.viewPager.setId(R.id.vPager);
        setContentView(viewPager);
        
        //setup actionbar
        actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		//setup tabs
		tabsAdapter = new TabsAdapter(this, viewPager);
		tabsAdapter.addTab(actionBar.newTab().setText(R.string.tab_movies), MovieListFragment.class, null);
		tabsAdapter.addTab(actionBar.newTab().setText(R.string.tab_tags), TagListFragment.class, null);
		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}	

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * When the user clicks the 'Add Movie' button in the Action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        Intent intent = new Intent(this, AddMovieActivity.class);
        startActivity(intent);
        
        return true;
    }
        
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
	
    //TODO: stolen from http://developer.android.com/reference/android/support/v4/view/ViewPager.html
    //need license or something?
	public static class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context context;
		private final ActionBar actionBar;
		private final ViewPager viewPager;
		private final ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> clss, Bundle args) {
				this.clss = clss;
				this.args = args;
			}
		}

		public TabsAdapter(FragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			this.context = activity;
			this.actionBar = activity.getActionBar();
			this.viewPager = pager;
			this.viewPager.setAdapter(this);
			this.viewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			this.tabs.add(info);
			this.actionBar.addTab(tab);
			notifyDataSetChanged();
		}

		public void onPageScrollStateChanged(int state) { 
		}

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

		public void onPageSelected(int position) {
			this.actionBar.setSelectedNavigationItem(position);
		}

		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) { }

		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0 ; i < this.tabs.size(); i++) {
				if (this.tabs.get(i) == tag) {
					this.viewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) { }

		@Override
		public Fragment getItem(int position) {
			TabInfo info = this.tabs.get(position);
			return Fragment.instantiate(this.context, info.clss.getName(), info.args);
		}

		@Override
		public int getCount() {
			return this.tabs.size();
		}

	}
}
