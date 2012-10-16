package se.chalmers.watchme.ui;

import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;

public abstract class MyListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	/**
	 * Filter the list from the given string.
	 * 
	 * @param search The string to be filtered on.
	 */
	public abstract void filter(String search);
	
}
