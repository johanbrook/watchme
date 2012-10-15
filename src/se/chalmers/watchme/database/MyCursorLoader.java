package se.chalmers.watchme.database;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Static library support version of the framework's
 * {@link android.content.CursorLoader}. Used to write apps that run on
 * platforms prior to Android 3.0. When running on Android 3.0 or above, this
 * implementation is still used; it does not try to switch to the framework's
 * implementation. See the framework SDK documentation for a class overview.
 * 
 * A CursorLoader that uses the {@link se.chalmers.watchme DatabaseAdapter}
 * to fetch data from the Database.
 * 
 * @lisastenberg
 */

public class MyCursorLoader extends CursorLoader {

	final ForceLoadContentObserver mObserver;
	private Uri mUri;
	private DatabaseAdapter db;

	// Set default to order by Date
	private String mSortOrder;
	private Long mTagId;
	
	private Cursor mCursor;
	private CancellationSignal mCancellationSignal;

	/* Runs on a worker thread */
	@Override
	public Cursor loadInBackground() {
        synchronized (this) {
        	//TODO: Unnecessary?
            mCancellationSignal = new CancellationSignal();
        }
        try {
        	db = new DatabaseAdapter(getContext().getContentResolver());
            Cursor cursor = getCursor();
            if (cursor != null) {
            	
                // Ensure the cursor window is filled
            	cursor.getCount();
                registerContentObserver(cursor, mObserver);
            }
            return cursor;
        } finally {
            synchronized (this) {
            	//TODO: Unnecessary?
                mCancellationSignal = null;
            }
        }
	}
	
	/**
	 * Asks the DatabaseAdapter for a Cursor depending on the Uri and the Id of 
	 * the Tag.
	 * 
	 * @return a Cursor.
	 */
	private Cursor getCursor() {
		if (mUri == WatchMeContentProvider.CONTENT_URI_MOVIES) {
			if (mTagId == -1) {
				return db.getAllMoviesCursor(mSortOrder);
			}
			return db.getAttachedMovies(mTagId);
		} else if(mUri == WatchMeContentProvider.CONTENT_URI_TAGS) {
			return db.getAllTagsCursor();
		}
		return null;
	}

	/**
	 * Registers an observer to get notifications from the content provider when
	 * the cursor needs to be refreshed.
	 */
	void registerContentObserver(Cursor cursor, ContentObserver observer) {
		cursor.registerContentObserver(mObserver);
	}

	/* Runs on the UI thread */
	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = mCursor;
		mCursor = cursor;
		if (isStarted()) {
			super.deliverResult(cursor);
		}
		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	/**
	 * Creates an empty unspecified CursorLoader. You must follow this with
	 * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc to
	 * specify the query to perform.
	 */
	public MyCursorLoader(Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver();
	}

	/**
	 * Creates a fully-specified CursorLoader. See
	 * {@link ContentResolver#query(Uri, String[], String, String[], String)
	 * ContentResolver.query()} for documentation on the meaning of the
	 * parameters. These will be passed as-is to that call.
	 */
	public MyCursorLoader(Context context, Uri uri, Long tagId, String sortOrder) {
		super(context);
		mObserver = new ForceLoadContentObserver();
		mUri = uri;
		mTagId = tagId;
		
		mSortOrder = sortOrder;
	}

	/**
	 * Starts an asynchronous load of the contacts list data. When the result is
	 * ready the callbacks will be called on the UI thread. If a previous load
	 * has been completed and is still valid the result may be passed to the
	 * callbacks immediately.
	 * 
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	/**
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}

	@Override
	public Uri getUri() {
        return mUri;
    }

	@Override
    public void setUri(Uri uri) {
        this.mUri = uri;
    }

	@Override
	public String getSortOrder() {
		return mSortOrder;
	}

	@Override
	public void setSortOrder(String sortOrder) {
		mSortOrder = sortOrder;
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer,
			String[] args) {
		super.dump(prefix, fd, writer, args);
		writer.print(prefix); writer.print("mUri="); writer.println(mUri);
		writer.print(prefix); writer.print("mSortOrder=");
		writer.println(mSortOrder);
		writer.print(prefix); writer.print("mCursor=");
		writer.println(mCursor);
		writer.print(prefix);
	}

}
