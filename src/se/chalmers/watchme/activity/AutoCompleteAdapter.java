/**
*	AutoCompleteAdapter.java
*
*	Custom Array adapter for auto complete dropdowns.
*
*	Includes custom view drawing with objects of the type Movie instead
*	of plain Strings.	
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.utils.DateConverter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<Movie> {

	private LayoutInflater inflater;
	
	public AutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		
		this.inflater = LayoutInflater.from(getContext());
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		/**
		 * Performance tweaks in getView:
		 * 
		 * Re-use the "trash view" convertView instead of always inflating
		 * a new view from XML. Only inflate if convertView is null.
		 * 
		 * Also make use of a ViewHolder to cache references to subviews. A 
		 * reference to a ViewHolder is created if a new view is inflated,
		 * otherwise we just use the attached ViewHolder of the old view.
		 */
		
		ViewHolder holder;
		Movie suggestion = this.getItem(position);
		
		if(convertView == null) {
			Log.i("Custom", "INFLATE");
			convertView = this.inflater.inflate(R.layout.auto_complete_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.autocomplete_title);
			holder.year = (TextView) convertView.findViewById(R.id.autocomplete_year);
			convertView.setTag(holder);
		}
		else {
			Log.i("Custom", "GET TAG");
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(suggestion.getTitle());
		holder.year.setText(DateConverter.toSimpleDate(suggestion.getDate()));
		
		return convertView;
	}
	
	/**
	 * A ViewHolder with a title, year and position fields. 
	 * 
	 * @author Johan
	 */
	static class ViewHolder {
		TextView title;
		TextView year;
		int position;
	}

}
