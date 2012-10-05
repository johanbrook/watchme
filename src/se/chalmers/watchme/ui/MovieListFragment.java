package se.chalmers.watchme.ui;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.WatchMeContentProvider;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MovieListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	SimpleCursorAdapter adapter;
	private Uri uri = WatchMeContentProvider.CONTENT_URI_MOVIES;
	
	@Override
	public void onActivityCreated(Bundle b) {
		super.onActivityCreated(b);
		Thread.currentThread().setContextClassLoader(getActivity().getClassLoader());
		 
		//TODO Add MoviesTable.COLUMN_DATE and android.R.id.date when implemented in database
		//TODO Exception when uncommenting MoviesTable.COLUMN_RATING:
		// IllegalArgumentException: column 'raiting' does not exist.
		// why?
		String[] from = new String[] { MoviesTable.COLUMN_MOVIE_ID, MoviesTable.COLUMN_TITLE, /* MoviesTable.COLUMN_RATING*/ /*, MoviesTable.COLUMN_DATE*/ };
		int[] to = new int[] { 0 , R.id.title /*, R.id.raiting */ /*, R.id.date */  };
		
		getActivity().getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_movie , null, from, to, 0);
	    setListAdapter(adapter);
	    
	    this.getListView().setOnItemLongClickListener(new OnDeleteListener());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.movie_list_fragment_view, container, false);
	}
	
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { MoviesTable.COLUMN_MOVIE_ID, MoviesTable.COLUMN_TITLE };
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
	
	 /**
     * The listener for when the user does a long-tap on an item in the list.
     * 
     * The Movie object in the list is removed if the user confirms that he wants to remove the Movie.
     * 
     * @author Johan
     */
    public class OnDeleteListener implements OnItemLongClickListener {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			final long movieId = id;
			
			String[] projection = { MoviesTable.COLUMN_TITLE };
			Cursor movieCursor = getActivity().getContentResolver().query(uri, projection, "_id = " + movieId, null, null);
			
			if (movieCursor != null) {
		        movieCursor.moveToFirst();
			}
			
			final String movieTitle = movieCursor.getString(0);
			
            AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
            alertbox.setMessage("Are you sure you want to delete the movie \"" + movieTitle + "\"?");           
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                	getActivity().getContentResolver().delete(uri, "_id = " + movieId , null);
                    Toast.makeText(getActivity().getApplicationContext(), "\"" + movieTitle + "\" was deleted" , Toast.LENGTH_SHORT).show();
                }
            });
            alertbox.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    
                }
            });
            
            alertbox.show();
			return true;
		}    	
	}

	
	

}
