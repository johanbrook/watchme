package se.chalmers.watchme;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        this.addButton = (Button) findViewById(R.id.add_movie_button);
        this.textField = (TextView) findViewById(R.id.movie_name_field);
        
        this.addButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Movie query = new Movie(textField.getText().toString());
				
				Intent home = new Intent(context, MainActivity.class);
				home.putExtra("movie", query);
				setResult(RESULT_OK, home);
				
				finish();
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
