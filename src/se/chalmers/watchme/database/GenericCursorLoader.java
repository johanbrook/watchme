package se.chalmers.watchme.database;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
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
 * A CursorLoader that has a ICursorHelper that calculates the cursor.
 * 
 * @author lisastenberg
 */

public class GenericCursorLoader extends CursorLoader {

	private final ForceLoadContentObserver mObserver;
	private Uri mUri;

	// Set default to order by Date
	private String mSortOrder;
	
	private Cursor mCursor;
	private CancellationSignal mCancellationSignal;
	
	private ICursorHelper mCursorHelper;

	/**
	 * Creates an empty unspecified CursorLoader. You must follow this with
	 * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc to
	 * specify the query to perform.
	 */
	public GenericCursorLoader(Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver();
	}
	
	/**
	 * Creates a CursorLoader with a cursorHelper that is used to calculate
	 * the Cursor in loadCursor()
	 * 
	 * @param context The context
	 * @param cursorHelper The CursorHelper
	 */
	public GenericCursorLoader(Context context, ICursorHelper cursorHelper) {
		super(context);
		mObserver = new ForceLoadContentObserver();
		mCursorHelper = cursorHelper;
		
		mUri = mCursorHelper.getUri();
		mSortOrder = mCursorHelper.getSortOrder();
	}
	
	/* Runs on a worker thread */
	@Override
	public Cursor loadInBackground() {
        synchronized (this) {
        	//TODO: Unnecessary?
            mCancellationSignal = new CancellationSignal();
        }
        try {
        	
            Cursor cursor = mCursorHelper.getCursor();
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
