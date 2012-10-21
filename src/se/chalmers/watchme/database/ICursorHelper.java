/**
 *	ICursorHelper.java
 *
 *  A helper interface that is needed in GenericCursorLoader
 * 
 *  Contain information about Uri, Cursor and sort order.
 *
 *	@author lisastenberg
 *	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
 *	@license MIT
 */

package se.chalmers.watchme.database;

import android.database.Cursor;
import android.net.Uri;

public interface ICursorHelper {
	
	/**
	 * Return the Uri
	 * @return the Uri
	 */
	public Uri getUri();
	
	/**
	 * Return the sortOrder
	 * @return the Cursor
	 */
	public String getSortOrder();
	
	/**
	 * Return the cursor
	 * @return the Cursor
	 */
	public Cursor getCursor();
}
