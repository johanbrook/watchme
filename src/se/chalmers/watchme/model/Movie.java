/**
 * A class that represents a Movie.
 * 
 * A Movie contains information about its title, rating, note and tags.
 * The higher rating a movie has, the more you want to see it. 
 */

package se.chalmers.watchme.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Movie implements Serializable {
	private String title, note;
	private int rating;
	private List<String> tags;
	
	/**
	 * Creates a movie with the given title, rating 0 and an empty note.
	 * @param title The title of the Movie.
	 */
	public Movie(String title) {
		this(title, 0, "");
	}
	
	/**
	 * Creates a movie with the given title, rating and note.
	 * @param title The title of the Movie.
	 * @param rating The rating.
	 * @param note The added note.
	 */
	public Movie(String title, int rating, String note) {
		this.title = title;
		this.note = note;
		this.rating = rating;
		tags = new LinkedList<String>();
	}
	
	/**
	 * Adds a tag to the list with tags.
	 * 
	 * @param tag The tag you want to add.
	 */
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	/**
	 * Removes a tag from the list with tags.
	 * 
	 * @param tag The tag you want to remove.
	 * @return true if the removal went through.
	 */
	public boolean removeTag(String tag) {
		return tags.remove(tag);
	}
	
	/**
	 * Change the rating of the Movie.
	 * @param newRating the new rating.
	 */
	public void setRating(int newRating) {
		rating = newRating;
	}
	
	/**
	 * @return The title of the Movie.
	 */
	public String getTitle() {
		return title;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		} else if(this.getClass() != o.getClass()) {
			return false;
		} else {
			Movie tmp = (Movie)o;
			return this.title == tmp.title;
		}
	}
}
