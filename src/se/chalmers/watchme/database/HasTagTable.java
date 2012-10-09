package se.chalmers.watchme.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The table that holds data for Movies and Tags.
 * 
 * COLUMN_MOVIE_ID is a foreign key to COLUMN_MOVIE_ID in MoviesTable
 * COLUMN_TAG_ID is a foreign key to COLUMN_TAG_ID in TagsTable
 * 
 * @author lisastenberg
 */
public class HasTagTable {
	public static final String TABLE_HAS_TAG = "hastag";

	public static final String COLUMN_MOVIE_ID = "movieid";
	public static final String COLUMN_TAG_ID = "tagid";

	private static final String CREATE_HAS_TAG_TABLE = "CREATE TABLE "
			+ TABLE_HAS_TAG + "(" + 
			COLUMN_MOVIE_ID + " INTEGER," + 
			COLUMN_TAG_ID + " INTEGER," + 
			"PRIMARY KEY(" + COLUMN_MOVIE_ID + ", " + COLUMN_TAG_ID + "), " + 
			"FOREIGN KEY(" + COLUMN_MOVIE_ID + ") REFERENCES " + 
			MoviesTable.TABLE_MOVIES + "(" + MoviesTable.COLUMN_MOVIE_ID +
			") ON DELETE CASCADE," +
			"FOREIGN KEY(" + COLUMN_TAG_ID + ") REFERENCES " + 
			TagsTable.TABLE_TAGS + "(" + TagsTable.COLUMN_TAG_ID + 
			") ON DELETE CASCADE" +
			")";

	private static final String TRIGGER = "CREATE TRIGGER deleteMovie AFTER " +
			"DELETE ON " + MoviesTable.TABLE_MOVIES +
			" FOR EACH ROW BEGIN " +
			"DELETE FROM " + HasTagTable.TABLE_HAS_TAG + " WHERE " +
			HasTagTable.COLUMN_MOVIE_ID + " = " + "old." + MoviesTable.COLUMN_MOVIE_ID +
			"; END;";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_HAS_TAG_TABLE);
		db.execSQL(TRIGGER);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(HasTagTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAS_TAG);
		onCreate(db);
	}
}
