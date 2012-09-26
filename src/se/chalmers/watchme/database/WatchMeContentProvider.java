package se.chalmers.watchme.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class WatchMeContentProvider extends ContentProvider {

	
	private DatabaseHelper db;
	
	public static final String AUTHORITY = "se.chalmers.watchme.database.providers.WatchMeContentProvider";
	
	private static final String BASE_PATH = "wathme";
	
	private static final int MOVIES = 10;
	private static final int MOVIES_ID = 20;
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + BASE_PATH);
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIES_ID);
		  };
	
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		int deletedRows = 0;
		switch (sUriMatcher.match(uri)) {
		case MOVIES:
			//Should we have this row or not?
			deletedRows = sqlDB.delete(MoviesTable.TABLE_MOVIES, selection,
			          selectionArgs);
			break;
		case MOVIES_ID:
			selection = selection + "_id = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		int count = sqlDB.delete(BASE_PATH, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
