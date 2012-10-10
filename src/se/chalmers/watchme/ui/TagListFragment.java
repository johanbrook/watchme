package se.chalmers.watchme.ui;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.watchme.R;
import se.chalmers.watchme.activity.MainActivity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TagListFragment extends ListFragment {
	
	ArrayAdapter<String> adapter;
	
	@Override
	public void onActivityCreated(Bundle b) {
		super.onActivityCreated(b);
		
		List<String> tags = new ArrayList<String>();
		tags.add("Action");
		tags.add("Drama");
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tags);
		setListAdapter(adapter);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tag_list_fragment_view, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((MainActivity) getActivity()).getViewPager().setCurrentItem(0);
	}
}
