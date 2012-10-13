package se.chalmers.watchme.ui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import event.Event;
import event.EventBus;
import event.EventHandler;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchme.database.DatabaseAdapter;
import se.chalmers.watchme.database.HasTagTable;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.TestCursorLoader;
import se.chalmers.watchme.database.WatchMeContentProvider;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.net.ImageDownloadTask;
import se.chalmers.watchme.notifications.NotificationClient;
import se.chalmers.watchme.utils.DateTimeUtils;
import se.chalmers.watchme.utils.ImageCache;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


// TODO Important! API level required does not match with what is used
@TargetApi(11)
public class MovieListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, EventHandler {
	
	private SimpleCursorAdapter adapter;
	private DatabaseAdapter db;
	
	private AsyncTask<String, Void, Bitmap> imageTask;
	private Cursor cursor;
	
	public MovieListFragment() {
		super();
		EventBus.register(this);
	}

	public MovieListFragment(Cursor cursor) {
		super();
		this.cursor = cursor;
		EventBus.register(this);
		System.out.println("----MovieListFragment(Cursor)----");
		
	}
	
	@Override
	public void onActivityCreated(Bundle b) {
		super.onActivityCreated(b);
		Thread.currentThread().setContextClassLoader(getActivity().getClassLoader());
		if(cursor == null) {
			db = new DatabaseAdapter(getActivity().getContentResolver());
			this.cursor = db.getAllMoviesCursor();
		}
		// Set up cache
		
		final File cacheDir = getActivity().getBaseContext().getCacheDir();
		ResponseCache.setDefault(new ImageCache(cacheDir));

		String[] from = new String[] { 
				MoviesTable.COLUMN_MOVIE_ID, 
				MoviesTable.COLUMN_TITLE,  
				MoviesTable.COLUMN_RATING ,
				MoviesTable.COLUMN_DATE,
				MoviesTable.COLUMN_POSTER_SMALL
				};
		
		int[] to = new int[] { 0 , 
				R.id.title, 
				R.id.raiting, 
				R.id.date,
				R.id.poster};
		
		getActivity().getSupportLoaderManager().initLoader(2, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_movie , cursor, from, to, 0);
		
		/**
		 * Convert date text from millis to dd-mm-yyyy format
		 */
		//TODO: Refactor?
		adapter.setViewBinder(new ViewBinder() {
			
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				
				if (columnIndex == cursor.getColumnIndexOrThrow(MoviesTable.COLUMN_DATE)) {
					String date = cursor.getString(columnIndex);
					TextView textView = (TextView) view;
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.parseLong(date));
					String formattedDate = DateTimeUtils.toSimpleDate(cal);
					
					textView.setText(formattedDate);
					return true;
				}
				
				/*
				 * Handle poster images
				 */
				
				else if(columnIndex == cursor.getColumnIndexOrThrow(MoviesTable.COLUMN_POSTER_SMALL)) {
					String smallImageUrl = cursor.getString(columnIndex);
					final ImageView imageView = (ImageView) view;
					
					if(smallImageUrl != null && !smallImageUrl.isEmpty()) {
						
						// Fetch the image in an async task
						imageTask = new ImageDownloadTask(new ImageDownloadTask.TaskActions() {
							
							public void onFinished(Bitmap image) {
								if(image != null) {
									((ImageView) imageView).setImageBitmap(image);
								}
							}
						});
						
						imageTask.execute(new String[]{smallImageUrl});
					}
					
					return true;
				}
				
				return false;
			}
		});
		
		setListAdapter(adapter);
	    
		// Set up listeners to delete and view a movie
        this.getListView().setOnItemClickListener(new OnDetailsListener());
	    this.getListView().setOnItemLongClickListener(new OnDeleteListener());
	    
	    System.out.println("----onActivityCreated()----");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.movie_list_fragment_view, container, false);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		System.out.println("--- onCreateLoader ---");
		String[] projection = { 
				MoviesTable.COLUMN_MOVIE_ID,
				MoviesTable.COLUMN_TITLE, 
				MoviesTable.COLUMN_RATING, 
				MoviesTable.COLUMN_DATE,
				MoviesTable.COLUMN_POSTER_SMALL};
		
		TestCursorLoader t = new TestCursorLoader(getActivity(), 
				WatchMeContentProvider.CONTENT_URI_MOVIES,
				cursor,projection,null,null,null);
		return t;
//	    return new CursorLoader(getActivity(),
//	    		WatchMeContentProvider.CONTENT_URI_MOVIES, projection, 
//	    		null, null, null);
	    
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		System.out.println("--- onLoadFinished ---");
		System.out.println("Cursor == null: " + cursor == null);
		System.out.println("COUNT:" + c.getCount());
		adapter.swapCursor(c);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// data is not available anymore, delete reference
	    adapter.swapCursor(null);
	    adapter.notifyDataSetChanged();
		
	}
	
	/**
     * Listener for when the user clicks an item in the list
     * 
     * The movie object in the list is used to fill a new activity with data
     * 
     * @author Robin
     */
    private class OnDetailsListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			db = new DatabaseAdapter(getActivity().getContentResolver());
			
			if(imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING) {
				imageTask.cancel(true);
			}
			
			Cursor selectedMovie = (Cursor) getListView().getItemAtPosition(position);
			Movie movie = db.getMovie(Long.parseLong(selectedMovie.getString(0)));
			
			Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
			
			// TODO Fetch all data from database in DetailsActivity instead?
			intent.putExtra(MovieDetailsActivity.MOVIE_EXTRA, movie);
			
			startActivity(intent);
			
		}
    }
	
	 /**
     * The listener for when the user does a long-tap on an item in the list.
     * 
     * The Movie object in the list is removed if the user confirms that he wants to remove the Movie.
     * 
     * @author Johan
     * @author lisastenberg
     */
    private class OnDeleteListener implements OnItemLongClickListener {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			db = new DatabaseAdapter(getActivity().getContentResolver());
			
			Cursor selectedMovie = (Cursor) getListView().getItemAtPosition(position);
    		final Movie movie = db.getMovie(Long.parseLong(selectedMovie.getString(0)));
    		
            AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
            alertbox.setMessage("Are you sure you want to delete the movie \"" + movie.getTitle() + "\"?");           
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                	
                	db = new DatabaseAdapter(getActivity().getContentResolver());
                	db.removeMovie(movie);
                	
                	EventBus.publish(new Event(Event.Tag.MOVIE_TABLE_CHANGED, ""));
                	EventBus.publish(new Event(Event.Tag.TAG_TABLE_CHANGED, ""));
                	
                	NotificationClient.cancelNotification(getActivity(), movie);
                    Toast.makeText(getActivity().getApplicationContext(), "\"" + movie.getTitle() + "\" was deleted" , Toast.LENGTH_SHORT).show();
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

	@Override
	public void onEvent(Event evt) {
		System.out.println("--- EVENT RECIEVED ---");
		if(evt.getTag() == Event.Tag.MOVIE_TABLE_CHANGED) {
			System.out.println("--- MOVIE_TABLE_CHANGED ---");
			adapter.notifyDataSetChanged();
			System.out.println("Before invalidate");
			this.getView().postInvalidate();
			//getActivity().getContentResolver().registerContentObserver(WatchMeContentProvider.CONTENT_URI_MOVIES, true, new MyContentObserver(handler) );
		}
	}

	private class MyContentObserver extends ContentObserver {

		public MyContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
	}

}
