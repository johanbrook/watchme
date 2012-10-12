package se.chalmers.watchme.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.model.Tag;

/**
 * Adapter to the Content Provider.
 * 
 * To get a correct update in the database it seems like you need to create
 * a new DatabaseAdapter before every new method-call to this class.
 * Probably because it needs a fresh ContentResolver.
 * 
 * @author lisastenberg
 *
 */
public class DatabaseAdapter {
	
	private Uri uri_movies = WatchMeContentProvider.CONTENT_URI_MOVIES;
	private Uri uri_tags = WatchMeContentProvider.CONTENT_URI_TAGS;
	private Uri uri_has_tag = WatchMeContentProvider.CONTENT_URI_HAS_TAG;
	
	private ContentResolver contentResolver;
	
	/**
	 * Creates a new adapter.
	 * 
	 * @param contentResolver the ContentResolver.
	 */
	public DatabaseAdapter(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}
	
	/**
	 * Returns the specified Movie.
	 * 
	 * @param id The id of the Movie.
	 * @return null if there is no Movie with the specified id.
	 */
	public Movie getMovie(long id) {
		String selection = MoviesTable.COLUMN_MOVIE_ID + " = " + id; 
		Cursor cursor = contentResolver.query(uri_movies, null, selection, null, null);
		if(cursor.moveToFirst()) {
			String title = cursor.getString(1);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(cursor.getString(4)));
			int rating = Integer.parseInt(cursor.getString(2));
			String note = cursor.getString(3);
			
			Movie movie = new Movie(title, calendar, rating, note);
			movie.setId(id);
			movie.setApiID(Integer.parseInt(cursor.getString(5)));
			
			return movie;
		}
		return null;
	}
	
	/**
	 * Inserts a Movie to the database and set the id of the Movie.
	 * 
	 * @param movie Movie to be inserted.
	 * @return the id of the added Movie.
	 */
	public long addMovie(Movie movie) {
		ContentValues values = new ContentValues();
	    values.put(MoviesTable.COLUMN_TITLE, movie.getTitle());
	    values.put(MoviesTable.COLUMN_RATING, movie.getRating());
	    values.put(MoviesTable.COLUMN_NOTE, movie.getNote());
	    values.put(MoviesTable.COLUMN_DATE, movie.getDate().getTimeInMillis());
	    values.put(MoviesTable.COLUMN_IMDB_ID, movie.getApiID());
	    values.put(MoviesTable.COLUMN_POSTER_LARGE, movie.getPosterURL(Movie.PosterSize.MID));
	    values.put(MoviesTable.COLUMN_POSTER_SMALL, movie.getPosterURL(Movie.PosterSize.THUMB));
	    
		Uri uri_movie_id = contentResolver.insert(uri_movies, values);
		movie.setId(Long.parseLong(uri_movie_id.getLastPathSegment()));
		return movie.getId();
	}
	
	/**
	 * Delete a Movie from the database.
	 * 
	 * @param movie The movie to be removed.
	 */
	public void removeMovie(Movie movie) {
		String where = MoviesTable.COLUMN_MOVIE_ID + " = " + movie.getId();
		
		contentResolver.delete(uri_movies, where, null);
	}
	
	/**
	 * Return all Movies from the database.
	 * @return all Movies from the database.
	 */
	public List<Movie> getAllMovies() {
		return getAllMovies(null);
	}
	
	/**
	 * Return all Movies from the database in the specified order.
	 * 
	 * @param orderBy The attribute to order by.
	 * @return all Movies from the database in the specified order.
	 */
	public List<Movie> getAllMovies(String orderBy) {
		List<Movie> movies = new ArrayList<Movie>();
		
		Cursor cursor = contentResolver.query(uri_movies, null, null, null, orderBy);
		
		while(cursor.moveToNext()) {
			long id = Long.parseLong(cursor.getString(0));
			String title = cursor.getString(1);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(cursor.getString(4)));
			int rating = Integer.parseInt(cursor.getString(2));
			String note = cursor.getString(3);
			
			Movie movie = new Movie(title, calendar, rating, note);
			movie.setId(id);
			movie.setApiID(Integer.parseInt(cursor.getString(5)));
			
			movies.add(movie);
		}
		return movies;
	}
	
	/**
	 * Returns the specified Tag.
	 * 
	 * @param id The id of the Tag.
	 * @return null if there is no Tag with the specified id.
	 */
	public Tag getTag(long id) {
		String selection = TagsTable.COLUMN_TAG_ID + " = " + id; 
		Cursor cursor = contentResolver.query(uri_tags, null, selection, null, null);
		
		if(cursor.moveToFirst()) {
			String name = cursor.getString(1);
			
			Tag tag = new Tag(name);
			tag.setId(id);
			
			return tag;
		}
		return null;
	}
	
	/**
	 * Inserts a Tag to the database.
	 * 
	 * @param tag The Tag to be inserted.
	 * @return the id of the added Tag.
	 */
	private long addTag(Tag tag) {
		ContentValues values = new ContentValues();
	    values.put(TagsTable.COLUMN_NAME, tag.getName());
	    
	    Uri uri_tag_id = contentResolver.insert(uri_tags, values);
	    tag.setId(Long.parseLong(uri_tag_id.getLastPathSegment()));
		return tag.getId();
	}
	
	/**
	 * Deletes a Tag from the database.
	 * 
	 * @param tag The Tag to be removed.
	 */
	public void removeTag(Tag tag) {
		String where = TagsTable.COLUMN_TAG_ID + " = " + tag.getId();
		
		contentResolver.delete(uri_tags, where, null);
	}
	
	/**
	 * Return all Tags in the database.
	 * 
	 * @return all Tags in the database. 
	 */
	public List<Tag> getAllTags() {
		List<Tag> tags = new ArrayList<Tag>();
		Cursor cursor = contentResolver.query(uri_movies, null, null, null, null);
		
		while(cursor.moveToNext()) {
			long id = Long.parseLong(cursor.getString(0));
			String name = cursor.getString(1);
			
			Tag tag = new Tag(name);
			tag.setId(id);
			
			tags.add(tag);
		}
		return tags;
	}
	
	/**
	 * Attach a Tag to a Movie.
	 * 
	 * @param movie The Movie.
	 * @param tag The Tag to be attached.
	 */
	public void attachTag(Movie movie, Tag tag) {
		ContentValues values = new ContentValues();
		
		values.put(MoviesTable.COLUMN_MOVIE_ID, movie.getId());
		values.put(TagsTable.COLUMN_NAME, tag.getSlug());
		
		contentResolver.insert(uri_has_tag, values);
	}
	
	/**
	 * Attach Tags to a movie.
	 * 
	 * @param movie The Movie.
	 * @param tags A list of Tags with Tags to be attached.
	 */
	public void attachTags(Movie movie, List<Tag> tags) {
		for(Tag tag : tags) {
			attachTag(movie, tag);
		}
	}
	
	/**
	 * Detach a Tag from a Movie.
	 * 
	 * @param movie The Movie.
	 * @param tag The Tag to be detached.
	 */
	public void detachTag(Movie movie, Tag tag) {
		String where = HasTagTable.COLUMN_MOVIE_ID + " = " + movie.getId() +
				" AND " + HasTagTable.COLUMN_TAG_ID + " = " + tag.getId();
		
		contentResolver.delete(uri_has_tag, where, null);
	}
	
	/**
	 * Return a Cursor containing all Tags attached to a Movie. 
	 * Cursor.getString(0) contains the id of the Tag.
	 * Cursor.getString(1) contains the name of the Tag.
	 * 
	 * @param movie The Movie.
	 * @return all Tags attached to the Movie.
	 */
	public Cursor getAttachedTags(Movie movie) {
		
		String selection = MoviesTable.TABLE_MOVIES + "." + 
				MoviesTable.COLUMN_MOVIE_ID + " = " + movie.getId();
		String[] projection = 
			{ TagsTable.TABLE_TAGS + "." + TagsTable.COLUMN_TAG_ID, 
				TagsTable.TABLE_TAGS + "." + TagsTable.COLUMN_NAME };

		return contentResolver.query(uri_has_tag, projection, selection, null, null);
	}
	
	/**
	 * Return a Cursor containing all Movies attached to a Tag.
	 * Cursor.getString(0) contains the id.
	 * Cursor.getString(1) contains the title.
	 * Cursor.getString(2) contains the rating.
	 * Cursor.getString(3) contains the note.
	 * Cursor.getString(4) contains the timeInMillis.
	 * Cursor.getString(5) contains the IMDb-id
	 * Cursor.getString(6) contains the large poster.
	 * Cursor.getString(7) contains the small poster.
	 * 
	 * @param tagId The id of the Tag.
	 * @return all Movies attached to the Tag.
	 */
	public Cursor getAttachedMovies(long tagId) {

		String selection = HasTagTable.TABLE_HAS_TAG + "." + 
				HasTagTable.COLUMN_TAG_ID + " = " + tagId;
		
		Cursor cursor = contentResolver.query(uri_has_tag, null, selection, null, null);
		
		return cursor;
	}
	
	/**
	 * Return a Cursor containing all Movies attached to a Tag.
	 * Cursor.getString(0) contains the id.
	 * Cursor.getString(1) contains the title.
	 * Cursor.getString(2) contains the rating.
	 * Cursor.getString(3) contains the note.
	 * Cursor.getString(4) contains the timeInMillis.
	 * Cursor.getString(5) contains the IMDb-id
	 * Cursor.getString(6) contains the large poster.
	 * Cursor.getString(7) contains the small poster.
	 * 
	 * @param tag The tag.
	 * @return all Movies attached to the Tag.
	 */
	public Cursor getAttachedMovies(Tag tag) {
		return getAttachedMovies(tag.getId());
	}
}
