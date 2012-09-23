package se.chalmers.watchme.model;

import java.io.Serializable;

/**
 * 
 * @author mattiashenriksson
 *
 */

public class Tag implements Serializable {

	private String name;
	private String slug;
	private long id;
	
	//TODO: The construcor should just look like this: Tag(String name)
	//Slug should be auto-generated
	public Tag(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}
	
	/*
	 * TODO: 	Add util function which takes a string and "slugifies" it
	 * 			i.e. removes unwanted characters: "Komedi film" => "komedi-film".
	 * 			(if we need the 'slug' attribute at all.
	*/ 
	
	/**
	 * @return the id of the Tag.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id the id you want to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSlug() {
		return this.slug;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
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
