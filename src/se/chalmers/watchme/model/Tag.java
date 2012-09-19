package se.chalmers.watchme.model;

import java.io.Serializable;

/**
 * 
 * @author mattiashenriksson
 *
 */

public class Tag implements Serializable {

	String name;
	String slug;
	
	public Tag(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSlug() {
		return slug;
	}
	
}
