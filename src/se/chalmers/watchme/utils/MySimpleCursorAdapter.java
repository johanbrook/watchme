package se.chalmers.watchme.utils;

import android.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {
	
	int mRealPos;
	
	public MySimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public View getView(int pos, View inView, ViewGroup parent) {
		if(pos == 0){
		    mRealPos = 0;
		  }
		  View v = inView;
		  if (v == null) {
		    LayoutInflater inflater = (LayoutInflater) mContext
		      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    v = inflater.inflate(R.layout.simple_list_item_1, null);
		  }
		  this.mCursor.moveToPosition(mRealPos);
		  boolean loop = true;
		  String bookmark;
		  Integer id;
		  do{
		    this.mCursor.g
			  id = Integer.parseInt(this.mCursor.getString(this.mCursor.getColumnIndex(BaseColumns._ID)));
		    if(mShowSelected && !mSelectedItems.contains( id )){
		      ++mRealPos;
		      mCursor.moveToNext();
		    }else{
		      loop = false;
		    }
		}while(loop);
		++mRealPos;
		
		return parent;
		
	}
	
}
