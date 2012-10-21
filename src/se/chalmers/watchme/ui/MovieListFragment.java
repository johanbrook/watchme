/**
 *	MovieListFragment.java
 *
 *  A fragment that present data about Movies.
 *
 *	@author lisastenberg, Johan Brook
 *	@copyright (c) 2012 Robin Andersson, Johan Brook, Mattias Henriksson, Lisa Stenberg
 *	@license MIT
 */

package se.chalmers.watchme.ui;

import java.io.File;
import java.net.ResponseCache;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MovieDetailsActivity;
import se.chalmers.watchme.database.DatabaseAdapter;
import se.chalmers.watchme.database.GenericCursorLoader;
import se.chalmers.watchme.database.ICursorHelper;
import se.chalmers.watchme.database.MoviesTable;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

// TODO Important! API level required does not match with what is used
@TargetApi(11)
public class MovieListFragment extends ContentListFragment {

	/**
	 * Enum that represents a sort order for Movies
	 * 
	 * @author lisastenberg
	 */
	public enum SortOrder {

		ORDER_BY_DATE(MoviesTable.COLUMN_DATE), 
		ORDER_BY_RATING(MoviesTable.COLUMN_RATING + " DESC"),
		ORDER_BY_TITLE(MoviesTable.COLUMN_TITLE);

		private String orderBy;

		/**
		 * Creates a new enum that has a string to order by
		 * 
		 * @param orderBy
		 */
		SortOrder(String orderBy) {
			this.orderBy = orderBy;
		}

		/**
		 * Return the string to order by
		 * 
		 * @return the string to order by
		 */
		public String getOrderBy() {
			return orderBy;
		}
	}

	private static final int LOADER_ID = 0;

	private AsyncTask<String, Void, Bitmap> imageTask;

	private static final long MISSING_TAGID = -1;
	private Long tagId;

	/**
	 * Has the current sort order
	 */
	private static int sortOrder;
	private String query;

	/**
	 * Creates a new MovieListFragment with the Uri
	 * WatchMeContentProvider.CONTENT_URI_MOVIES
	 */
	public MovieListFragment() {
		super(WatchMeContentProvider.CONTENT_URI_MOVIES);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		db = new DatabaseAdapter(getActivity().getContentResolver());

		setHasOptionsMenu(true);
		setRetainInstance(true);

		final File cacheDir = getActivity().getBaseContext().getCacheDir();
		ResponseCache.setDefault(new ImageCache(cacheDir));

		/*
		 * If any arguments where set fetch the values
		 */
		Bundle arguments = getArguments();
		if (arguments != null) {
			query = arguments.getString(getString(R.string.search), null);
			tagId = arguments.getLong(TagListFragment.TAG_ID, MISSING_TAGID);
		} else if (tagId == null) {
			tagId = MISSING_TAGID;
		}

		setUpAdapter();

		// Set up listeners to delete and view a movie
		this.getListView().setOnItemClickListener(new OnDetailsListener());
		this.getListView().setOnItemLongClickListener(new OnDeleteListener());
	}

	/*
	 * Show the Share and Sort buttons while movie list view
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem sortItem = menu.findItem(R.id.menu_sort_button);
		MenuItem shareItem = menu.findItem(R.id.menu_send_email_button);

		sortItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// If there aren't any movies in the list, disable the "Share list"
		// button
		int count = new DatabaseAdapter(getActivity().getContentResolver())
				.getMovieCount();

		if (count == 0) {
			shareItem.setEnabled(false);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.movie_list_fragment_view, container,
				false);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		return new GenericCursorLoader(getActivity(), new ICursorHelper() {

			@Override
			public Uri getUri() {
				return MovieListFragment.this.getUri();
			}

			@Override
			public String getSortOrder() {
				return SortOrder.values()[sortOrder].getOrderBy();
			}

			@Override
			public Cursor getCursor() {
				if (tagId == MISSING_TAGID) {
					if (query == null || query.equals("")) {
						return db.getAllMoviesCursor(getSortOrder());
					}
					// If this is presenting a search.
					return db.searchForMovies(query);
				}
				return db.getAttachedMovies(tagId);
			}

		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_sort_button:
			sortList();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Show a Dialog Box with choices of attributes to order the Movies by.
	 */
	private void sortList() {
		final String[] alternatives = { "Date", "Rating", "Title" };

		AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
		alertbox.setTitle(getString(R.string.order_dialog_title));
		alertbox.setSingleChoiceItems(alternatives, sortOrder,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						sortOrder = item;
						// Fetch the order by-string from SortOrder
						String orderBy = SortOrder.values()[sortOrder]
								.getOrderBy();

						Cursor cursor = db.getAllMoviesCursor(orderBy);

						// Change the cursor
						onLoadFinished(null, cursor);

						dialog.dismiss();
					}
				});

		alertbox.show();
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

			/*
			 * Cancel any tasks that fetches poster images if a movie is
			 * selected
			 */
			if (imageTask != null
					&& imageTask.getStatus() == AsyncTask.Status.RUNNING) {
				imageTask.cancel(true);
			}

			// Fetch selected movie from database
			Cursor selectedMovie = (Cursor) getListView().getItemAtPosition(
					position);
			Movie movie = db
					.getMovie(Long.parseLong(selectedMovie.getString(0)));

			Intent intent = new Intent(getActivity(),
					MovieDetailsActivity.class);
			intent.putExtra(MovieDetailsActivity.MOVIE_EXTRA, movie);

			// .. and jump to the details view
			startActivity(intent);

		}
	}

	/**
	 * The listener for when the user does a long-tap on an item in the list.
	 * 
	 * The Movie object in the list is removed if the user confirms that he
	 * wants to remove the Movie.
	 * 
	 * @author Johan
	 * @author lisastenberg
	 */
	private class OnDeleteListener implements OnItemLongClickListener {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {

			Cursor selectedMovie = (Cursor) getListView().getItemAtPosition(
					position);
			final Movie movie = db.getMovie(Long.parseLong(selectedMovie
					.getString(0)));

			AlertDialog.Builder alertbox = new AlertDialog.Builder(
					getActivity());
			alertbox.setMessage(getString(R.string.delete_dialog_text) + " \""
					+ movie.getTitle() + "\"?");
			alertbox.setPositiveButton(
					getString(R.string.delete_button_positive),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {

							db.removeMovie(movie);

							NotificationClient.cancelNotification(
									getActivity(), movie);
							Toast.makeText(
									getActivity().getApplicationContext(),
									"\"" + movie.getTitle() + "\" was deleted",
									Toast.LENGTH_SHORT).show();

							/*
							 * If this MovieListFragment contains all Movies for
							 * a specified tag and the tag has no other attached
							 * movies, the user is brought back to the main
							 * activity.
							 */
							if (tagId != MISSING_TAGID
									&& db.getAttachedMovies(tagId).getCount() == 0) {
								NavUtils.navigateUpFromSameTask(getActivity());
							}
						}
					});
			alertbox.setNeutralButton(
					getString(R.string.delete_button_negative),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							// Nothing should happen if the user press cancel.
						}
					});

			alertbox.show();
			return true;
		}
	}

	/**
	 * Set up adapter and set adapter.
	 */
	private void setUpAdapter() {

		// Bind columns from the table Movies to items in the rows.
		String[] from = new String[] { MoviesTable.COLUMN_TITLE,
				MoviesTable.COLUMN_RATING, MoviesTable.COLUMN_DATE,
				MoviesTable.COLUMN_POSTER_SMALL };

		int[] to = new int[] { R.id.title, R.id.raiting, R.id.date, R.id.poster };

		getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null,
				this);
		setAdapter(new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_movie, null, from, to, 0));

		/**
		 * Manipulate the shown date in list
		 */
		getAdapter().setViewBinder(new ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {

				if (columnIndex == cursor
						.getColumnIndexOrThrow(MoviesTable.COLUMN_DATE)) {
					
					String dateString = cursor.getString(columnIndex);
					TextView textView = (TextView) view;
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(Long.parseLong(dateString));
					
					/*
					 * If the movie's release date is within a given threshold (fetched 
					 * from resource file), change the text color of the field. 
					 */
					int threshold = Integer.parseInt(getString(R.string.days_threshold));
					
					if(DateTimeUtils.isDateInInterval(date, threshold, TimeUnit.DAYS)) {
						String color = getString(R.string.color_threshold);
						textView.setTextColor(Color.parseColor(color));
					}
					/*
					 * Set to original color if not in threshold
					 */
					else {
						textView.setTextColor(R.string.list_date_color);
					}
					
					
					// Format the date to relative form ("two days left")
					String formattedDate = DateTimeUtils.toHumanDate(date);
					textView.setText(formattedDate);

					return true;
				}

				/*
				 * Handle rating bar conversion
				 */
				else if (columnIndex == cursor
						.getColumnIndexOrThrow(MoviesTable.COLUMN_RATING)) {
					int rating = cursor.getInt(columnIndex);
					RatingBar bar = (RatingBar) view;
					bar.setRating(rating);

					return true;
				}

				/*
				 * Handle poster images
				 */

				else if (columnIndex == cursor
						.getColumnIndexOrThrow(MoviesTable.COLUMN_POSTER_SMALL)) {
					String smallImageUrl = cursor.getString(columnIndex);
					final ImageView imageView = (ImageView) view;

					if (smallImageUrl != null && !smallImageUrl.isEmpty()) {

						// Fetch the image in an async task
						imageTask = new ImageDownloadTask(
								new ImageDownloadTask.TaskActions() {

									// When task is finished, set the resulting
									// image on the poster view
									public void onFinished(Bitmap image) {
										if (image != null) {
											((ImageView) imageView)
													.setImageBitmap(image);
										}
									}
								});

						imageTask.execute(new String[] { smallImageUrl });
					}

					return true;
				}

				return false;
			}
		});
	}

	public long getTagId() {
		return tagId;
	}
}
