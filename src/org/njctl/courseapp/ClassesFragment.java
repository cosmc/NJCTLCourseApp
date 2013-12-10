package org.njctl.courseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ying on 11/16/13.
 */

public class ClassesFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView listView = (ListView) getView().findViewById(R.id.classes_fragment_listview);
		
        
        ArrayList<NJCTLChapter> mockChapters = new ArrayList<NJCTLChapter>();
        mockChapters.add(new NJCTLChapter("1. Kinematics"));
        mockChapters.add(new NJCTLChapter("2. Electrons"));
        mockChapters.add(new NJCTLChapter("3. Conformal Field Theories"));
        
        ArrayList<NJCTLClass> mockClasses = new ArrayList<NJCTLClass>();
        mockClasses.add(new NJCTLClass("Math", mockChapters));
        mockClasses.add(new NJCTLClass("Physics", mockChapters));
        mockClasses.add(new NJCTLClass("Chemistry", mockChapters));

        ClassesAdapter adapter = new ClassesAdapter(getActivity(), 0, mockClasses);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Class click", "" + position);
            }
        }); 
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.classes_fragment, container, false);   
        return v;
    }
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}


