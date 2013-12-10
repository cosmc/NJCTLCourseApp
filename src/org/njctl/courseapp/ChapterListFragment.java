package org.njctl.courseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import org.njctl.courseapp.ClassesAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Colin on 12/9/13.
 */

public class ChapterListFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ExpandableListView elv = (ExpandableListView) getView().findViewById(R.id.chapter_list_fragment_listview);
		
        ArrayList<NJCTLChapter> mockChapters = new ArrayList<NJCTLChapter>();
        mockChapters.add(new NJCTLChapter("1. Kinematics"));
        mockChapters.add(new NJCTLChapter("2. Electrons"));
        mockChapters.add(new NJCTLChapter("3. Conformal Field Theories"));

        ExpandableChapterAdapter adapter = new ExpandableChapterAdapter(getActivity(), mockChapters);
        elv.setAdapter(adapter);
        
        elv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Click", "" + position);
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


