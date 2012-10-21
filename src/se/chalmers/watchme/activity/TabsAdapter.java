/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * This is a helper class that implements the management of tabs and all details
 * of connecting a ViewPager with associated TabHost. It relies on a trick.
 * Normally a tab host has a simple API for supplying a View or Intent that each
 * tab will show. This is not sufficient for switching between pages. So instead
 * we make the content part of the tab host 0dp high (it is not shown) and the
 * TabsAdapter supplies its own dummy view to show as the tab content. It
 * listens to changes in tabs, and takes care of switch to the correct paged in
 * the ViewPager whenever the selected tab changes.
 * 
 * Class is available as example at
 * http://developer.android.com/reference/android/support/v4/view/ViewPager.html
 */
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
