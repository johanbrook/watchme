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
 */

public class TestCursorLoader extends CursorLoader /*AsyncTaskLoader<Cursor>*/ {

	final ForceLoadContentObserver mObserver;
	private Uri mUri;
	DatabaseAdapter db;

	String[] mProjection;
	String mSelection;
	String[] mSelectionArgs;
	String mSortOrder;
	Long mTagId;
	
	Cursor mCursor;
	CancellationSignal mCancellationSignal;

	/* Runs on a worker thread */
	@Override
	public Cursor loadInBackground() {
        synchronized (this) {
        	/*
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            */
            mCancellationSignal = new CancellationSignal();
        }
        try {
        	db = new DatabaseAdapter(getContext().getContentResolver());
            Cursor cursor = getCursor(mTagId);
            if (cursor != null) {
                // Ensure the cursor window is filled
                cursor.getCount();
                System.out.println("LOADINBACKGROUND: " + cursor.getCount());
                registerContentObserver(cursor, mObserver);
            }
            return cursor;
        } finally {
            synchronized (this) {
                mCancellationSignal = null;
            }
        }
	}
	
	private Cursor getCursor(Long tagId) {
		if(tagId == -1) {
			return db.getAllMoviesCursor();
		}
		return db.getAttachedMovies(tagId);
	}

	/**
	 * Registers an observer to get notifications from the content provider when
	 * the cursor needs to be refreshed.
	 */
	void registerContentObserver(Cursor cursor, ContentObserver observer) {
		System.out.println("-- registerContentObserver --");
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
//		cursorr.unregisterContentObserver(mObserver);
		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	/**
	 * Creates an empty unspecified CursorLoader. You must follow this with
	 * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc to
	 * specify the query to perform.
	 */
	public TestCursorLoader(Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver();
	}

	/**
	 * Creates a fully-specified CursorLoader. See
	 * {@link ContentResolver#query(Uri, String[], String, String[], String)
	 * ContentResolver.query()} for documentation on the meaning of the
	 * parameters. These will be passed as-is to that call.
	 */
	public TestCursorLoader(Context context, Uri uri, Long tagId, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		super(context);
		mObserver = new ForceLoadContentObserver();
		mUri = uri;
		mTagId = tagId;
		
		mProjection = projection;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
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

	public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }
	
	public String[] getProjection() {
		return mProjection;
	}

	public void setProjection(String[] projection) {
		mProjection = projection;
	}

	public String getSelection() {
		return mSelection;
	}

	public void setSelection(String selection) {
		mSelection = selection;
	}

	public String[] getSelectionArgs() {
		return mSelectionArgs;
	}

	public void setSelectionArgs(String[] selectionArgs) {
		mSelectionArgs = selectionArgs;
	}

	public String getSortOrder() {
		return mSortOrder;
	}

	public void setSortOrder(String sortOrder) {
		mSortOrder = sortOrder;
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer,
			String[] args) {
		super.dump(prefix, fd, writer, args);
		// writer.print(prefix); writer.print("mUri="); writer.println(mUri);
		// writer.print(prefix); writer.print("mProjection=");
		// writer.println(Arrays.toString(mProjection));
		// writer.print(prefix); writer.print("mSelection=");
		// writer.println(mSelection);
		// writer.print(prefix); writer.print("mSelectionArgs=");
		// writer.println(Arrays.toString(mSelectionArgs));
		// writer.print(prefix); writer.print("mSortOrder=");
		// writer.println(mSortOrder);
		// writer.print(prefix); writer.print("mCursor=");
		// writer.println(cursor);
		// writer.print(prefix);
		// writer.print("mContentChanged=");
		// // writer.println(mContentChanged);
	}

}
