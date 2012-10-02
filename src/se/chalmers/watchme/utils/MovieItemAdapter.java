package se.chalmers.watchme.utils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import se.chalmers.watchme.R;
import se.chalmers.watchme.model.Movie;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class takes a list of movies, extracts info from them, and puts the info
 * in a list view of movies. 
 * @author mattiashenriksson
 *
 */


public class MovieItemAdapter extends ArrayAdapter<Movie> {
    private List<Movie> movies;
    private Context context;

    public MovieItemAdapter(Context context, int textViewResourceId, List<Movie> movies) {
        super(context, textViewResourceId, movies);
        this.movies = movies;
        this.context = context;
    }
    
    /**
     * When a list view is displayed this method is called once for every list
     * item.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_item_movie, null);
        }
        
        /**
         * Extract info from the movie object and distribute it to the right
         * place of the list item.
         */
        Movie movie = movies.get(position);
        if (movie != null) {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView raiting = (TextView) v.findViewById(R.id.raiting);
        

            if (title != null) {
                title.setText(movie.getTitle());
            }

            //TODO: Refactor ugly code
            //TODO: Change to API level 9 for use of 
            //movie.getDate().getDisplayName(...) (not possilble now).
            //Nicer code and no deprecated methods will be used.
			if (date != null) {
				StringBuilder res = new StringBuilder();
				
				int dateNr = movie.getDate().getTime().getDate();
				if (dateNr < 10) {
					res.append("0" + dateNr  + " / ");
				} else {
					res.append("" + dateNr  + " / ");
				}
				
				int monthNr = movie.getDate().getTime().getMonth() + 1;
				if (monthNr < 10) {
					res.append("0" + monthNr  + " / ");
				} else {
					res.append("" + monthNr  + " / ");
				}
				
				res.append(movie.getDate().getTime().getYear() + 1900);
				date.setText(res);		
			}
            
            if (raiting != null) {
            	raiting.setText("" + movie.getRating());
            }
        }
  
        return v;
    }
} 


