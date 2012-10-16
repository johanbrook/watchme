package se.chalmers.watchme.activity;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.DatabaseAdapter;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.notifications.NotificationClient;
import se.chalmers.watchme.ui.MovieListFragment;
import se.chalmers.watchme.ui.MyListFragment;
import se.chalmers.watchme.ui.TagListFragment;
import se.chalmers.watchme.utils.DateTimeUtils;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends FragmentActivity {
	
	public static final String MOVIE_DETAILS_ID = "se.chalmers.watchme.DETAILS_ID";
	public static final String MOVIE_DETAILS_TITLE = "se.chalmers.watchme.DETAILS_TITLE";
	public static final String MOVIE_DETAILS_RATING = "se.chalmers.watchme.DETAILS_RATING";
	public static final String MOVIE_DETAILS_NOTE = "se.chalmers.watchme.DETAILS_NOTE";
	
	//TODO: Correct to put key values for Intent.putExtra() here? 
	public static final String TAG_ID = "se.chalmers.watchme.TAG_ID";
	
	private ViewPager viewPager;
	private TabsAdapter tabsAdapter;
	private ActionBar actionBar;

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
     * When the user clicks on a button in the Action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
    	switch(item.getItemId()) {
    	case R.id.menu_add_movie:
    		Intent intent = new Intent(this, AddMovieActivity.class);
            startActivity(intent);
            return true;
    		
    	case R.id.menu_send_email_button:
    		sendEmail();
    		return true;

    	case R.id.menu_search_button:
    		search();
    		
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    	
    }
    
    @Override
    public boolean onSearchRequested() {
    	
    	search();
    	
    	return false;
    }
    
    private void search() {    	
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	alertbox.setTitle(getString(R.string.search));
    	final EditText ed = new EditText(this);
    	ed.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable e) {
				MyListFragment f = (MyListFragment) getSupportFragmentManager().findFragmentById(R.id.vPager);
				int i = viewPager.getCurrentItem();
				System.out.println("FRAGMENT: " + f.getClass() + " " + i);
				f.filter(e.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
    		
    	});

    	alertbox.setView(ed);
		alertbox.setPositiveButton("Search!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
					}
				});
		alertbox.show();
    }
    
    private void sendEmail() {
    	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	emailIntent.setType("text/plain");
    	
    	List<Movie> movies = new DatabaseAdapter(getContentResolver()).getAllMovies();
    	
    	// Parse all movies with their dates:
    	String movieString = "";
    	for(Movie m : movies) {
    		movieString += m.getTitle() + " ("+DateTimeUtils.toSimpleDate(m.getDate()) + ")\n";
    	}
    	
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.email_subject);
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, movieString);  
    	
    	// Let the user choose email app to mail from
    	startActivity(Intent.createChooser(emailIntent, getString(R.string.choose_message_app)));
    }
        
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
	
    //TODO: stolen from http://developer.android.com/reference/android/support/v4/view/ViewPager.html
    //need license or something?
	public static class TabsAdapter extends FragmentPagerAdapter 
		implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
		
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
