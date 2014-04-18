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

import org.njctl.courseapp.model.NJCTLClass;

/**
 * Created by ying on 11/16/13.
 */

public class ClassesFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView listView = (ListView) getView().findViewById(R.id.classes_fragment_listview);
		
        Bundle args = getArguments();
        final ArrayList<NJCTLClass> classes = args.getParcelableArrayList("classes");

        ClassesAdapter adapter = new ClassesAdapter(getActivity(), 0, classes);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Class click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).showChapters(classes.get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.classes_fragment, container, false);   
        return v;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	
    	// Set the title text to the app name.
		getActivity().setTitle(R.string.app_name);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}


