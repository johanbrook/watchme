package se.chalmers.watchme.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The table in the database that holds data for Tags.
 * 
 * @author lisastenberg
 */
public class TagsTable {

	public static final String TABLE_TAGS = "tags";
	
    public static final String COLUMN_TAG_ID = "_id";
    public static final String COLUMN_NAME = "name";
    
    private static final String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_TAGS + "("
    		+ COLUMN_TAG_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT)";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TAGS_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(TagsTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
		onCreate(db);
	}
}
