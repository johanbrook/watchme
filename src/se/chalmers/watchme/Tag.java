package se.chalmers.watchme;

import java.io.Serializable;

/**
 * 
 * @author mattiashenriksson
 *
 */

public class Tag implements Serializable {

	private String name;
	private String slug;
	
	public Tag(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}
	
	/*
	 * TODO: 	Add util function which takes a string and "slugifies" it
	 * 			i.e. removes unwanted characters: "Komedi film" => "komedi-film".
	 * 			(if we need the 'slug' attribute at all.
	*/ 
	
	public String getName() {
		return this.name;
	}
	
	public String getSlug() {
		return this.slug;
	}
	
	public String toString() {
		return this.name;
	}
	
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		Tag t = (Tag) obj;
		
		return this.name.equalsIgnoreCase(t.name);
	}
	
	
	
}
