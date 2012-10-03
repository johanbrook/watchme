/**
*	AutoCompleteAdapter.java
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook
*	@license MIT
*/

package se.chalmers.watchme.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import se.chalmers.watchme.R;
import se.chalmers.watchme.model.Movie;
import se.chalmers.watchme.utils.DateConverter;
import se.chalmers.watchme.utils.MovieHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<Movie> {

	private LayoutInflater inflater = LayoutInflater.from(getContext());
	
	public AutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	
	private void sortItemsByRating(List<JSONObject> res) {
		Collections.sort(res, Collections.reverseOrder(new Comparator<JSONObject>() {

			public int compare(JSONObject lhs, JSONObject rhs) {
				return Double.compare(lhs.optDouble("rating"), rhs.optDouble("rating"));
			}
			
		}));
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.i("Custom", "GET_VIEW");
		
		ViewHolder holder;
		Movie suggestion = this.getItem(position);
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.auto_complete_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.autocomplete_title);
			holder.year = (TextView) convertView.findViewById(R.id.autocomplete_year);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(suggestion.getTitle());
		holder.year.setText(DateConverter.toSimpleDate(suggestion.getDate()));
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView year;
		int position;
	}

}
