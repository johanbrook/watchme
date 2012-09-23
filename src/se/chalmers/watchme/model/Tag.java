package se.chalmers.watchme.model;

import java.io.Serializable;

/**
 * A class that represents a Tag.
 * 
 * @author mattiashenriksson
 * @author lisastenberg
 */

public class Tag implements Serializable {

	private String name;
	private String slug;
	private long id;
	
	/**
	 * Creates a Tag and generates a slug after the given name.
	 * @param name The name of the Tag.
	 */
	public Tag(String name) {
		this.name = name;
		this.slug = generateSlug(name);
	}
	
	/**
	 * @return the id of the Tag.
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Sets the id.
	 * @param id the id you want to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return The name of the Tag.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The slug.
	 */
	public String getSlug() {
		return this.slug;
	}
	
	/**
	 * Generates a slug of a given string.
	 * @param s The string.
	 * @return The slug.
	 */
	public static String generateSlug(String s) {
		return s.toLowerCase();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if(o == null || this.getClass() != o.getClass()) {
			return false;
		} else {
			Tag tmp = (Tag)o;
			return this.getSlug().equals(tmp.getSlug());
		}
	}
}
