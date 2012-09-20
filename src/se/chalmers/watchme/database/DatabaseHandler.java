package se.chalmers.watchme.database;

import java.util.LinkedList;
import java.util.List;

import se.chalmers.watchme.model.Movie;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieManager";
    private static final String TABLE_MOVIES = "movies";
 
    // The Columns names in the table Movies
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RATING = "rating";
    private static final String KEY_NOTE = "note";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_RATING + " INTEGER," + KEY_NOTE + " TEXT" +  ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
		onCreate(db);
	}
	
	/**
	 * Adds a Movie to the database.
	 * @param movie The Movie you want to add.
	 */
	public void addMovie(Movie movie) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
		//Is the ID inserted automatic?
	    ContentValues values = new ContentValues();
	    values.put(KEY_TITLE, movie.getTitle()); // Contact Name
	    values.put(KEY_RATING, movie.getRating()); // Contact Phone Number
	    values.put(KEY_NOTE, movie.getNote());
	 
	    // Inserting Row
	    db.insert(TABLE_MOVIES, null, values);
	    db.close(); // Closing database connection
	}
	
	/**
	 * @param id The unique id of the Movie
	 * @return the Movie asked for.
	 */
	public Movie getMovie(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MOVIES, new String[] { KEY_ID,
				KEY_TITLE, KEY_RATING, KEY_NOTE }, KEY_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Movie movie = new Movie(cursor.getString(1), Integer.parseInt(cursor.getString(1)), cursor.getString(3));
		return movie;
	}
	
	/**
	 * @return All Movies stored in the database.
	 */
	public List<Movie> getAllMovies() {
		List<Movie> allMovies = new LinkedList<Movie>();

		String selectQuery = "SELECT * FROM " + TABLE_MOVIES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Movie movie = new Movie(cursor.getString(1),
						Integer.parseInt(cursor.getString(2)),
						cursor.getString(3));
				allMovies.add(movie);
			} while (cursor.moveToNext());
		}
		
		return allMovies;
	}
	
	
	/**
	 * Updates information about a Movie in the database.
	 * @param movie The movie you want to update.
	 * @return Number of updated rows.
	 */
	/*
	 * NOT WORKING YET
	public int updateMovie(Movie movie) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
	    values.put(KEY_TITLE, movie.getTitle()); // Contact Name
	    values.put(KEY_RATING, movie.getRating()); // Contact Phone Number
	    values.put(KEY_NOTE, movie.getNote());
		
	    //TODO: Get movie:id. Where?
	    return db.update(TABLE_MOVIES, values, KEY_ID + " = ?", new String[] {"movieid"});
	}
	*/
	
	/**
	 * Deletes a Movie from the database.
	 * @param movie The movie you want to delete.
	 * @return Number of deleted rows.
	 */
	/*
	 * NOT WORKING YET
	public int deleteMovie(Movie movie) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		return db.delete(TABLE_MOVIES, KEY_ID + " = ?", new String[] {"movieid"});
	}
	*/
}