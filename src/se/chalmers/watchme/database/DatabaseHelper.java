package se.chalmers.watchme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manage Database creation and version managements. 
 * 
 * @author lisastenberg
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "watchme.db";
    
	private static final String CREATE_TRIGGER_DETACH = "CREATE TRIGGER detach AFTER " +
			"DELETE ON " + MoviesTable.TABLE_MOVIES +
			" FOR EACH ROW BEGIN " +
			"DELETE FROM " + HasTagTable.TABLE_HAS_TAG + " WHERE " +
			HasTagTable.COLUMN_MOVIE_ID + " = old." + MoviesTable.COLUMN_MOVIE_ID +
			"; END;";
	
	// TODO Delete if we don't get it to work correctly
	private static final String CREATE_TRIGGER_DELETETAG = "CREATE TRIGGER deleteTag AFTER " +
    		"DELETE ON " + HasTagTable.TABLE_HAS_TAG + 
    		" BEGIN " +
    		"SELECT CASE WHEN (1 > (SELECT Count(*) " +
    		"FROM " + HasTagTable.TABLE_HAS_TAG + 
    		" WHERE " + HasTagTable.COLUMN_TAG_ID + " = old." + 
    		HasTagTable.COLUMN_TAG_ID + "))" + 
    		" THEN " +
    		"DELETE FROM " + TagsTable.TABLE_TAGS + " WHERE " + 
    		TagsTable.COLUMN_TAG_ID + " = old." + HasTagTable.COLUMN_TAG_ID +
    		"; END; " +
    		"END;";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		MoviesTable.onCreate(db);
        TagsTable.onCreate(db);
        HasTagTable.onCreate(db);
        
        db.execSQL(CREATE_TRIGGER_DETACH);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MoviesTable.onUpgrade(db, oldVersion, newVersion);
		TagsTable.onUpgrade(db, oldVersion, newVersion);
		HasTagTable.onUpgrade(db, oldVersion, newVersion);
		
		db.execSQL("DROP TRIGGER IF EXISTS detachMovie");
		db.execSQL(CREATE_TRIGGER_DETACH);
	}
}