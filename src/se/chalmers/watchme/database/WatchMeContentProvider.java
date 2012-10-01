package se.chalmers.watchme.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * The Content Provider for the WatchMe application.
 * 
 * @author lisastenberg
 *
 */
public class WatchMeContentProvider extends ContentProvider {
	
	private DatabaseHelper db;
	
	public static final String AUTHORITY = "se.chalmers.watchme.database.providers.WatchMeContentProvider";
	
	private static final String BASE_PATH = "wathme";
	
	private static final int MOVIES = 10;
	private static final int MOVIES_ID = 20;
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + BASE_PATH);
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	        + "/watchme";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	        + "/watchme";
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIES_ID);
		  };
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		
		switch (sUriMatcher.match(uri)) {
		case MOVIES:
			//Nothing need to be added to the selection
			break;
		case MOVIES_ID:
			//TODO: Need to check if selection is null?
			selection = selection + MoviesTable.COLUMN_MOVIE_ID + " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		int deletedRows = sqlDB.delete(MoviesTable.TABLE_MOVIES, selection, selectionArgs);
		
		//Called as a courtesy
		getContext().getContentResolver().notifyChange(uri, null);
		return deletedRows;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		
		long id = 0;
		switch(sUriMatcher.match(uri)) {
		case MOVIES:
			id = sqlDB.insert(MoviesTable.TABLE_MOVIES, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		
		//Called as a courtesy
		getContext().getContentResolver().notifyChange(uri, null);
		
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext());
		System.out.println("--- CREATED DB IN CONTENT PROVIDER ---");
		//db.onUpgrade(db.getReadableDatabase(), 0, 0);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase sqlDB = db.getReadableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
	    switch (sUriMatcher.match(uri)) {
	    case MOVIES:
	    	queryBuilder.setTables(MoviesTable.TABLE_MOVIES);
	    	break;
	    case MOVIES_ID:
	    	selection = selection + MoviesTable.COLUMN_MOVIE_ID + " = " + uri.getLastPathSegment();
	    	queryBuilder.setTables(MoviesTable.TABLE_MOVIES);
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	    Cursor cursor = queryBuilder.query(sqlDB,
	            projection, selection, selectionArgs, null, null, sortOrder);
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);
	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		
		switch (sUriMatcher.match(uri)) {
		case MOVIES:
			//Nothing need to be added to selection
			break;
		case MOVIES_ID:
			selection = selection + MoviesTable.COLUMN_MOVIE_ID + " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		int updatedRows = sqlDB.update(MoviesTable.TABLE_MOVIES, values, selection, selectionArgs);
		
		//Called as a courtesy
		getContext().getContentResolver().notifyChange(uri, null);
		return updatedRows;
	}

}
