package se.chalmers.watchme.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
	
//TODO: stolen from http://developer.android.com/reference/android/support/v4/view/ViewPager.html
//need license or something?
public class TabsAdapter extends FragmentPagerAdapter implements
		ActionBar.TabListener, ViewPager.OnPageChangeListener {

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

	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	public void onPageSelected(int position) {
		this.actionBar.setSelectedNavigationItem(position);
	}

	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		Object tag = tab.getTag();
		for (int i = 0; i < this.tabs.size(); i++) {
			if (this.tabs.get(i) == tag) {
				this.viewPager.setCurrentItem(i);
			}
		}
	}

	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = this.tabs.get(position);
		return Fragment.instantiate(this.context, info.clss.getName(),
				info.args);
	}

	@Override
	public int getCount() {
		return this.tabs.size();
	}

}
