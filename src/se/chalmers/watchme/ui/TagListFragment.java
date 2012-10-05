package se.chalmers.watchme.ui;

import se.chalmers.watchme.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TagListFragment extends ListFragment {
	
	@Override
	public void onActivityCreated(Bundle b) {
		super.onActivityCreated(b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tag_list_fragment_view, container, false);
	}
}
