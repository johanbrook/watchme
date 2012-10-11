package se.chalmers.watchme.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchme.activity.TagMovieListActivity;
import se.chalmers.watchme.database.TagsTable;
import se.chalmers.watchme.database.WatchMeContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TagListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	SimpleCursorAdapter adapter;
	private Uri uri_tags = WatchMeContentProvider.CONTENT_URI_TAGS;
	
	@Override
	public void onActivityCreated(Bundle b) {
		super.onActivityCreated(b);
		Thread.currentThread().setContextClassLoader(getActivity().getClassLoader());
		
		String[] from = new String[] { TagsTable.COLUMN_TAG_ID, TagsTable.COLUMN_NAME };
		int[] to = new int[] { android.R.id.text1 , android.R.id.text1 };
		
		getActivity().getSupportLoaderManager().initLoader(1, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1 , null, from, to, 0);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tag_list_fragment_view, container, false);
	}
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { TagsTable.COLUMN_TAG_ID, TagsTable.COLUMN_NAME };
	    CursorLoader cursorLoader = new CursorLoader(getActivity(),
	        uri_tags, projection, null, null, null);
	    return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);		
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// data is not available anymore, delete reference
	    adapter.swapCursor(null);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		/**
		 * When a tag is clicked, create a cursor pointing at movies containing
		 * that tag. Then send it to TagMovieListActivity using intent.putExtra()
		 */
		String imaginaryCursor = "cursor";
		Intent intent = new Intent(getActivity(), TagMovieListActivity.class);
		intent.putExtra(MainActivity.EXTRA_CURSOR, imaginaryCursor);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
			
	}
}
