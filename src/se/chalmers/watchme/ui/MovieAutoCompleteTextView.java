/**
*	MovieAutoCompleteTextView.java
*
*	Subclass AutoCompleteTextView in order to override the 
*	convertSelectionToString to customize the output.
*
*	This is because the list items in the autocomplete dropdown
*	is objects of the type JSONObject. JSONObject's toString
*	implementation is not overridable by us.
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.ui;

import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class MovieAutoCompleteTextView extends AutoCompleteTextView {

	public MovieAutoCompleteTextView(Context context) {
		super(context);
	}
	
	public MovieAutoCompleteTextView(Context context, AttributeSet atts) {
		super(context, atts);
	}
	
	public MovieAutoCompleteTextView(Context context, AttributeSet atts, int defStyle) {
		super(context, atts, defStyle);
	}

	/*
	 * If we're dealing with a JSON object, instead of letting the
	 * AutoCompleteView use the selected object's toString implementation,
	 * override this method and return the JSON object's key 'original_name'.
	 */
	
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		
		if(selectedItem instanceof JSONObject) {
			return ((JSONObject) selectedItem).optString("original_name");
		}
		else {
			return super.convertSelectionToString(selectedItem);
		}
	}
}
