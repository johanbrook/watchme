package se.chalmers.watchme.activity;

import se.chalmers.watchme.R;
import se.chalmers.watchme.R.id;
import se.chalmers.watchme.R.layout;
import se.chalmers.watchme.R.menu;
import se.chalmers.watchme.database.DatabaseHandler;
import se.chalmers.watchme.model.Movie;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;

public class AddMovieActivity extends Activity {
	
	private TextView textField;
	private Button addButton;
	private final Context context = this;
	private DatabaseHandler db;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.addButton = (Button) findViewById(R.id.add_movie_button);
        this.textField = (TextView) findViewById(R.id.movie_name_field);
        
        db = new DatabaseHandler(this);
        
        this.addButton.setEnabled(false);
        
        /**
         * Click callback. Create a new Movie object and set it on
         * the Intent, and then finish this Activity.
         */
        this.addButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Movie movie = new Movie(textField.getText().toString());
				db.addMovie(movie);
				
				Intent home = new Intent(context, MainActivity.class);
				setResult(RESULT_OK, home);
				home.putExtra("movie", movie);
				
				finish();
			}
		});
        
        /**
         * Disable "add button" if no Title on Movie has been set.
         */
        this.textField.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(s.toString().equals("")) {
            		addButton.setEnabled(false);
            	} else {
            		addButton.setEnabled(true);
            	}
            }

			public void afterTextChanged(Editable arg0) {
				//TODO Added throw statement since methods never used. Is this
				// the right way to do it? / Mattias
				throw new UnsupportedOperationException();		
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				throw new UnsupportedOperationException();
			}

        });
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_movie, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    


}
