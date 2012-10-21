/**
 *	MovieTable.java
 *
 *  The table in the database that holds data for Movies.
 *
 *  @author lisastenberg
 *	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
 *	@license MIT
 */

package se.chalmers.watchme.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MoviesTable {

	public static final String TABLE_MOVIES = "movies";

	public static final String COLUMN_MOVIE_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_DATE = "releasedate";
	public static final String COLUMN_IMDB_ID = "imdbid";
	public static final String COLUMN_POSTER_LARGE = "poster_url_large";
	public static final String COLUMN_POSTER_SMALL = "poster_url_small";

	private static final String CREATE_MOVIES_TABLE = "CREATE TABLE "
			+ TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_TITLE + " TEXT," + COLUMN_RATING + " INTEGER,"
			+ COLUMN_NOTE + " TEXT," + COLUMN_DATE + " INTEGER,"
			+ COLUMN_IMDB_ID + " TEXT," + COLUMN_POSTER_LARGE + " TEXT,"
			+ COLUMN_POSTER_SMALL + " TEXT)";

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
