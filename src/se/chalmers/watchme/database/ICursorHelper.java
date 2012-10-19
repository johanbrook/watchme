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
