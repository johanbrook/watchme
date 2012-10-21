/**
*	MovieAlreadyExistsException.java
*
*	Exception to throw when a movie already exist in the database
*	(in reference to a movie's title).
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.database;

import se.chalmers.watchme.model.Movie;

public class MovieAlreadyExistsException extends Exception {
	
	private Movie movie;

	/**
	 * Create a new movie exception.
	 * 
	 * @param detailMessage The message string
	 * @param movie The incorrect movie
	 */
	public MovieAlreadyExistsException(String detailMessage, Movie movie) {
		super(detailMessage);
		
		this.movie = movie;
	}
	
	/**
	 * Get the incorrect movie object.
	 * 
	 * @return The duplicated movie
	 */
	public Movie getMovie() {
		return this.movie;
	}

}
