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
import se.chalmers.watchme.utils.MovieHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<JSONObject> {

	private List<JSONObject> items;
	private Context ctx;
	
	public AutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.ctx = context;
	}
	
	public AutoCompleteAdapter(Context context, int textViewResourceId, List<JSONObject> data) {
		this(context, textViewResourceId);
		this.items = data;
		
		if(this.items != null) {
			//this.sortItemsByRating(this.items);
		}
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
		
		View view = convertView;
		if(view == null) {
			LayoutInflater f = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = f.inflate(R.layout.auto_complete_item, null);
		}
		
		JSONObject o = this.items.get(position);
		if(o != null) {
			TextView title = (TextView) view.findViewById(R.id.autocomplete_title);
			TextView year = (TextView) view.findViewById(R.id.autocomplete_year);
			
			title.setText(o.optString("original_name"));
			year.setText(MovieHelper.parseYearFromDate(o.optString("released")));
		}
		
		return view;
	}

}
