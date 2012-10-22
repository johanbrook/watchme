/**
*	MenuUtils.java
*
*	@author Robin Andersson
*	@copyright (c) 2012 Robin Andersson
*	@license MIT
*/

package se.chalmers.watchme.utils;

import android.view.MenuItem;

public class MenuUtils {

	/**
	 * Changes the menu item's icon to reflect enabled or disabled state.
	 * 
	 * @param menuItem The menu item which icon is to be set
	 * @param isEnabled True if the item is enabled
	 */
	public static void setMenuIconState(MenuItem menuItem, boolean isEnabled) {
		menuItem.getIcon().setAlpha(menuItem.isEnabled() ? 255 : 64);
	}

}