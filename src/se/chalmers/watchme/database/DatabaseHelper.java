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
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		MoviesTable.onCreate(db);
        TagsTable.onCreate(db);
        HasTagTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MoviesTable.onUpgrade(db, oldVersion, newVersion);
		TagsTable.onUpgrade(db, oldVersion, newVersion);
		HasTagTable.onUpgrade(db, oldVersion, newVersion);
		System.out.println("--- UPDATED DATABASE ---");
	}
}