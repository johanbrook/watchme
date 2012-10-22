/**
*	NoEntityException.java
*
*	Custom class reporting failures with HTTP entities.
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
*	@license MIT
*/

package se.chalmers.watchme.net;

public class NoEntityException extends Exception {
	
	public NoEntityException() {
		super();
	}
	
	public NoEntityException(String msg) {
		super(msg);
	}
}
