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
	
    private static final String TRIGGER = "CREATE TRIGGER deleteTag AFTER " +
    		"DELETE ON " + HasTagTable.TABLE_HAS_TAG + 
    		" DECLARE" +
    		" nbrOfAttachment INT " +
    		"BEGIN " +
    		"SELECT Count(*) INTO nbrOfAttachment " +
    		"FROM " + HasTagTable.TABLE_HAS_TAG + 
    		" WHERE old." + TagsTable.COLUMN_TAG_ID + " = " + 
    		HasTagTable.COLUMN_TAG_ID + "; " + 
    		"IF nbrOfAttachment = 0 THEN " +
    		"DELETE FROM " + TagsTable.TABLE_TAGS + " WHERE " + 
    		TagsTable.COLUMN_TAG_ID + " = old." + TagsTable.COLUMN_TAG_ID +
    		" END IF; " +
    		"END;";
    
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TAGS_TABLE);
		//db.execSQL(TRIGGER);
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
