package se.chalmers.watchme.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * The table in the database that holds data for Movies.
 * 
 * @author lisastenberg
 */
public class MoviesTable {

	public static final String TABLE_MOVIES = "movies";

	public static final String COLUMN_MOVIE_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_NOTE = "note";

	private static final String CREATE_MOVIES_TABLE = "CREATE TABLE "
			+ TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_TITLE + " TEXT," + COLUMN_RATING + " INTEGER," + COLUMN_NOTE
			+ " TEXT" + ")";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MOVIES_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(MoviesTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
		onCreate(db);
	}
}
