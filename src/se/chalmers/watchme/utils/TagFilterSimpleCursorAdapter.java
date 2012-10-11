package se.chalmers.watchme.utils;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.watchme.R;
import se.chalmers.watchme.database.WatchMeContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TagFilterSimpleCursorAdapter extends SimpleCursorAdapter {
	
	private int realPos;
	private Context context;
	private Uri uri_has_tags = WatchMeContentProvider.CONTENT_URI_HAS_TAG;
	private String tag;
	
	public TagFilterSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}
	
	public TagFilterSimpleCursorAdapter(Context context, int layout, Cursor c,
	String[] from, int[] to, int flags, String tag) {
		super(context, layout, c, from, to, flags);
		this.tag = tag;
	}

	@Override
	public View getView(int pos, View inView, ViewGroup parent) {
		System.out.println("------------------------------------------------");
		System.out.println("getView() in TagFilterSimpleCursorAdapter called");
		// if this is the first list item, initiate realPos
		if (pos == 0) {
			realPos = 0;
		}
		System.out.println("pos: " + pos);
		System.out.println("realPos: " + realPos);
		
		View v = inView;
		System.out.println("v: " + v);

		// if there is no old view to reuse, create new one
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item_movie, null);
		}
		

		// get the view of the correct element
		this.mCursor.moveToPosition(realPos);
		System.out.println("mCursor: " + mCursor.getString(0));
		
		boolean loop = true;

		do {
			
			// Collect tags for the movie
			Cursor tagCursor = context.getContentResolver().query(uri_has_tags,
					null, "_id = " + mCursor.getString(0), null, null);

			// TODO: Refactor, do-statement?
			List<String> tags = new ArrayList<String>();
			if (tagCursor.moveToFirst()) {
				tags.add(tagCursor.getString(3));
				while (tagCursor.moveToNext()) {
					tags.add(tagCursor.getString(3));
				}
			}
			
			//if a movie don't have the tag sent into the constructor, it should not be displayed
			if (!tags.contains(tag)) {
				++realPos;
				mCursor.moveToNext();
			} else {
				//TODO: Better using break; ?
				loop = false;
			}
		} while (loop);
		++realPos;
		
		//input data in view
		TextView title = (TextView) v.findViewById(R.id.title);
		TextView date = (TextView) v.findViewById(R.id.date);
		TextView raiting = (TextView) v.findViewById(R.id.raiting);
		
		title.setText(mCursor.getString(1));
		date.setText(mCursor.getString(2));
		raiting.setText(mCursor.getString(3));

		return v;

	}
	
}
