package se.chalmers.watchme.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.TagsTable;
import se.chalmers.watchme.database.WatchMeContentProvider;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * 
 * @author mattiashenriksson
 *
 */
//TODO: This class is quite similar to MovieListFragment. Possible to refactor?
public class TagListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	SimpleCursorAdapter adapter;
	private Uri uri = WatchMeContentProvider.CONTENT_URI_MOVIES;
	
	@Override
	public void onActivityCreated(Bundle b) {
		super.onActivityCreated(b);
		Thread.currentThread().setContextClassLoader(getActivity().getClassLoader());
		
		//TODO Exception when uncommenting MoviesTable.COLUMN_NAME:
		// IllegalArgumentException: column 'name' does not exist.
		// why?
		String[] from = new String[] { TagsTable.COLUMN_TAG_ID, /* TagsTable.COLUMN_NAME */ };
		int[] to = new int[] { android.R.id.text1 , /* android.R.id.text1 */};
		
		getActivity().getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.rowlayout , null, from, to, 0);
	    setListAdapter(adapter);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tag_list_fragment_view, container, false);	
	}
	
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { TagsTable.COLUMN_TAG_ID};
	    CursorLoader cursorLoader = new CursorLoader(getActivity(),
	        uri, projection, null, null, null);
	    return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);		
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// data is not available anymore, delete reference
	    adapter.swapCursor(null);
	}

}

