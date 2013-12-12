package org.njctl.courseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Colin on 12/9/13.
 */

public class ChapterListFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		final NJCTLClass theClass = (NJCTLClass) getArguments().getParcelable("class"); // Find out which class we're displaying the chapters for.
		
        ExpandableListView elv = (ExpandableListView) getView().findViewById(R.id.chapter_list_fragment_listview);

        ExpandableChapterAdapter adapter = new ExpandableChapterAdapter(getActivity(), theClass.getContents());
        elv.setAdapter(adapter);
        
        elv.setOnChildClickListener(new OnChildClickListener() {
        	 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.w("Item selected:", groupPosition + " " + childPosition);
                try {
        			((NJCTLNavActivity) getActivity()).showDocList(theClass.getContents().get(groupPosition).getContents().get(childPosition));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
                return false;
            }
        });
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chapter_list_fragment, container, false);   
        return v;
    }
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}


