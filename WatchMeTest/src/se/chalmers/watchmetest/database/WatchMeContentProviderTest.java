package se.chalmers.watchmetest.database;

import junit.framework.Assert;
import se.chalmers.watchme.database.HasTagTable;
import se.chalmers.watchme.database.MoviesTable;
import se.chalmers.watchme.database.TagsTable;
import se.chalmers.watchme.database.WatchMeContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

/**
 * Test all public methods in WatchMeContenProvider
 * 
 * From http://developer.android.com/tools/testing/contentprovider_testing.html
 * "Test with resolver methods: Even though you can instantiate a provider 
 * object in ProviderTestCase2, you should always test with a resolver object 
 * using the appropriate URI. This ensures that you are testing the provider 
 * using the same interaction that a regular application would use. "
 * 
 * @author lisastenberg
 * 
 */
public class WatchMeContentProviderTest extends ProviderTestCase2<WatchMeContentProvider>{
	
	Uri uri_movies = WatchMeContentProvider.CONTENT_URI_MOVIES;
	Uri uri_tags = WatchMeContentProvider.CONTENT_URI_TAGS;
	Uri uri_hastag = WatchMeContentProvider.CONTENT_URI_HAS_TAG;
	
	Uri[] validUris = new Uri[] { uri_movies, uri_tags, uri_hastag };
	
	private ContentResolver contentResolver;
	
	public WatchMeContentProviderTest() {
	    super(WatchMeContentProvider.class, WatchMeContentProvider.AUTHORITY);
	}
	/*
	public WatchMeContentProviderTest(Class<WatchMeContentProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }
    */
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        contentResolver = getMockContentResolver();
    }

	@Override
	protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	/**
	 * Test valid URIs and unvalid URIs
	 * 
	 * From http://developer.android.com/tools/testing/contentprovider_testing.html
	 * "Test invalid URIs: Your unit tests should deliberately call the provider
	 *  with an invalid URI, and look for errors. Good provider design is to 
	 *  throw an IllegalArgumentException for invalid URIs. "
	 */
	public void testURI() {
		Uri uri_invalid = Uri.parse("invalid");
		
		try {
			contentResolver.insert(uri_invalid, new ContentValues());
			Assert.fail("insert: Should have thrown IllegalArgumentException");
		}
		catch(IllegalArgumentException e) {}
		
		try {
			contentResolver.query(uri_invalid, null, null, null, null);
			Assert.fail("query: Should have thrown IllegalArgumentException");
		}
		catch(IllegalArgumentException e) {}
		
		try {
			contentResolver.update(uri_invalid, new ContentValues(), null, null);
			Assert.fail("update: Should have thrown IllegalArgumentException");
		}
		catch(IllegalArgumentException e) {}
		
		try {
			contentResolver.delete(uri_invalid, null, null);
			Assert.fail("delete: Should have thrown IllegalArgumentException");
		}
		catch(IllegalArgumentException e) {}
	}

	public void testQuery() {
        for (Uri uri : validUris) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            assertNotNull(cursor);
        }    
    }
	
	public void testInsert() {
		ContentValues values = new ContentValues();
		
		/*
		 * Test insert into uri_movie
		 */
		values.put(MoviesTable.COLUMN_TITLE, "batman");
		values.put(MoviesTable.COLUMN_RATING, 1);
	    values.put(MoviesTable.COLUMN_NOTE, "");
	    values.put(MoviesTable.COLUMN_DATE, 0);
	    values.put(MoviesTable.COLUMN_IMDB_ID, 0);
	    values.put(MoviesTable.COLUMN_POSTER_LARGE, "");
	    values.put(MoviesTable.COLUMN_POSTER_SMALL, "");
		
		Uri tmpUri = contentResolver.insert(uri_movies, values);
		long movieId = Long.parseLong(tmpUri.getLastPathSegment());
		// Check that the id of the movie was returned.
		assertTrue(movieId != 0);
		
		Cursor cursor = contentResolver.query(uri_movies, null, 
				MoviesTable.COLUMN_MOVIE_ID + " = " + movieId, null, null);
		assertEquals(cursor.getCount(), 1);
		
		cursor.moveToFirst();
		assertTrue(cursor.getString(1).equals("batman"));
		
		/*
		 * Test insert the movie with the same title into uri_movie
		 */
		values = new ContentValues();
		
		values.put(MoviesTable.COLUMN_TITLE, "batman");
		values.put(MoviesTable.COLUMN_RATING, 1);
	    values.put(MoviesTable.COLUMN_NOTE, "");
	    values.put(MoviesTable.COLUMN_DATE, 0);
	    values.put(MoviesTable.COLUMN_IMDB_ID, 0);
	    values.put(MoviesTable.COLUMN_POSTER_LARGE, "");
	    values.put(MoviesTable.COLUMN_POSTER_SMALL, "");
	    
	    tmpUri = contentResolver.insert(uri_movies, values);
	    long zeroId = Long.parseLong(tmpUri.getLastPathSegment());
		
	    // If the movie already existed insert should return 0.
	 	assertEquals(zeroId, 0);
	 	
	 	// Confirms that there exist one and only one movie with the title 'batman'
	 	cursor = contentResolver.query(uri_movies, null, 
				MoviesTable.COLUMN_TITLE + " = 'batman'", null, null);
		assertEquals(cursor.getCount(), 1);
	    
		/*
		 * Test insert into uri_tag
		 */
		values = new ContentValues();
		
		values.put(TagsTable.COLUMN_NAME, "tag");
		try {
			tmpUri = contentResolver.insert(uri_tags, values);
			Assert.fail("Should throw UnsupportedOperationException");
		} catch(UnsupportedOperationException e) {}
		
		/*
		 * Test insert into uri_hastag
		 */
		values = new ContentValues();
		
		values.put(MoviesTable.COLUMN_MOVIE_ID, 1);
		values.put(TagsTable.COLUMN_NAME, "tag");
		tmpUri = contentResolver.insert(uri_hastag, values);
		long tagId = Long.parseLong(tmpUri.getLastPathSegment());
		
		cursor = contentResolver.query(uri_hastag, null, 
				HasTagTable.COLUMN_MOVIE_ID + " = " + movieId + " AND " +
						HasTagTable.COLUMN_TAG_ID + " = " + tagId, null, null);
		assertEquals(cursor.getCount(), 1);
		
		/*
		 * It should not be possible to attach a tag to a movie it is already
		 * attached to.
		 */
		tmpUri = contentResolver.insert(uri_hastag, values);
		zeroId = Long.parseLong(tmpUri.getLastPathSegment());
		
		// If the attachment already existed insert should return 0.
		assertEquals(zeroId, 0);
		
		cursor = contentResolver
				.query(uri_hastag, null, HasTagTable.COLUMN_MOVIE_ID + " = "
						+ movieId + " AND " + HasTagTable.COLUMN_TAG_ID + " = "
						+ tagId, null, null);
		/* Confirms that there exist one and only one attachment between the 
		 * movie 'batman' and the tag 'tag'
		 */
		assertEquals(cursor.getCount(), 1);
	}
	
	/*
	 * Precondition: testInsertMovie has passed
	 */
	public void testUpdate() {
		
		ContentValues values = new ContentValues();
		
		// Test movie-uri
		values.put(MoviesTable.COLUMN_TITLE, "batman");
		values.put(MoviesTable.COLUMN_RATING, 1);
	    values.put(MoviesTable.COLUMN_NOTE, "");
	    values.put(MoviesTable.COLUMN_DATE, 0);
	    values.put(MoviesTable.COLUMN_IMDB_ID, 0);
	    values.put(MoviesTable.COLUMN_POSTER_LARGE, "");
	    values.put(MoviesTable.COLUMN_POSTER_SMALL, "");
		
		Uri tmpUri = contentResolver.insert(uri_movies, values);
		long movieId = Long.parseLong(tmpUri.getLastPathSegment());
		
		values = new ContentValues();
		values.put(MoviesTable.COLUMN_NOTE, "updated");
		int updatedRows = contentResolver.update(uri_movies, values, "_id = " + movieId, null);
		assertEquals(updatedRows, 1);
	}
	
	/*
	 * Precondition: testInsertMovie has passed
	 */
	public void testDeleteMovie() {
		
		ContentValues values = new ContentValues();
		
		// Test movie-uri
		values.put(MoviesTable.COLUMN_TITLE, "batman");
		values.put(MoviesTable.COLUMN_RATING, 1);
	    values.put(MoviesTable.COLUMN_NOTE, "");
	    values.put(MoviesTable.COLUMN_DATE, 0);
	    values.put(MoviesTable.COLUMN_IMDB_ID, 0);
	    values.put(MoviesTable.COLUMN_POSTER_LARGE, "");
	    values.put(MoviesTable.COLUMN_POSTER_SMALL, "");
		
		Uri tmpUri = contentResolver.insert(uri_movies, values);
		long movieId = Long.parseLong(tmpUri.getLastPathSegment());
		
		int deletedRows = contentResolver.delete(uri_movies, "_id = " + movieId, null);
		assertEquals(deletedRows, 1);
	}
	
}