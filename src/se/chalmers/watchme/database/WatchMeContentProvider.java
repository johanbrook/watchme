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
	
	private static final String BASE_PATH_MOVIES = "movies";
	private static final String BASE_PATH_TAGS = "tags";
	private static final String BASE_PATH_HAS_TAG = "hastag";
	
	private static final int MOVIES = 10;
	private static final int MOVIES_ID = 20;
	private static final int TAGS = 30;
	private static final int TAGS_ID = 40;
	private static final int HAS_TAG = 50;
	
	public static final Uri CONTENT_URI_MOVIES = Uri.parse("content://" + AUTHORITY
	        + "/" + BASE_PATH_MOVIES);
	public static final Uri CONTENT_URI_TAGS = Uri.parse("content://" + AUTHORITY
	        + "/" + BASE_PATH_TAGS);
	public static final Uri CONTENT_URI_HAS_TAG = Uri.parse("content://" + AUTHORITY
	        + "/" + BASE_PATH_HAS_TAG);
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	        + "/watchme";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	        + "/watchme";
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIES, MOVIES);
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH_MOVIES + "/#", MOVIES_ID);
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH_TAGS, TAGS);
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH_TAGS, TAGS_ID);
		    sUriMatcher.addURI(AUTHORITY, BASE_PATH_HAS_TAG, HAS_TAG);
		  };
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		
		int deletedRows;
		switch (sUriMatcher.match(uri)) {
		case MOVIES:
			//Nothing need to be added to the selection
			deletedRows = sqlDB.delete(MoviesTable.TABLE_MOVIES, selection, selectionArgs);
			break;
		case MOVIES_ID:
			//TODO: Need to check if selection is null?
			selection = selection + MoviesTable.COLUMN_MOVIE_ID + " = " + uri.getLastPathSegment();
			deletedRows = sqlDB.delete(MoviesTable.TABLE_MOVIES, selection, selectionArgs);
			break;
		case TAGS:
			deletedRows = sqlDB.delete(TagsTable.TABLE_TAGS, selection, selectionArgs);
			break;
		case TAGS_ID:
			//TODO: Need to check if selection is null?
			selection = selection + TagsTable.COLUMN_TAG_ID + " = " + uri.getLastPathSegment();
			deletedRows = sqlDB.delete(TagsTable.TABLE_TAGS, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
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
		case HAS_TAG:
			// TODO: Check if the Tag exists. If it doesn't exist. insert into database 
			id = sqlDB.insert(HasTagTable.TABLE_HAS_TAG, null, values);
			break;
		case TAGS:
			id = sqlDB.insert(TagsTable.TABLE_TAGS, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		
		//Called as a courtesy
		getContext().getContentResolver().notifyChange(uri, null);
		
	    return Uri.parse(BASE_PATH_MOVIES + "/" + id);
	}

	@Override
	public boolean onCreate() {
		db = new DatabaseHelper(getContext());
		System.out.println("--- CREATED DB IN CONTENT PROVIDER ---");
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
	    case TAGS:
	    	queryBuilder.setTables(TagsTable.TABLE_TAGS);
	    	break;
	    case TAGS_ID:
	    	selection = selection + TagsTable.COLUMN_TAG_ID + " = " + uri.getLastPathSegment();
	    	queryBuilder.setTables(TagsTable.TABLE_TAGS);
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
		
		int updatedRows;
		switch (sUriMatcher.match(uri)) {
		case MOVIES:
			// Nothing need to be added to selection
			updatedRows = sqlDB.update(MoviesTable.TABLE_MOVIES, values, selection, selectionArgs);
			break;
		case MOVIES_ID:
			selection = selection + MoviesTable.COLUMN_MOVIE_ID + " = " + uri.getLastPathSegment();
			updatedRows = sqlDB.update(MoviesTable.TABLE_MOVIES, values, selection, selectionArgs);
			break;
		case TAGS:
			// TODO: Unnecessary case?
			// Nothing need to be added to selection
			updatedRows = sqlDB.update(TagsTable.TABLE_TAGS, values, selection, selectionArgs);
			break;
		case TAGS_ID:
			selection = selection + TagsTable.COLUMN_TAG_ID + " = " + uri.getLastPathSegment();
			updatedRows = sqlDB.update(TagsTable.TABLE_TAGS, values, selection, selectionArgs);
			break;	
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		//Called as a courtesy
		getContext().getContentResolver().notifyChange(uri, null);
		return updatedRows;
	}

}
