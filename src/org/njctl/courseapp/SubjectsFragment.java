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
import android.widget.ArrayAdapter;
import android.util.Log;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Subject;

/**
 * Created by ying on 11/16/13.
 */

public class SubjectsFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView listView = (ListView) getView().findViewById(R.id.subjects_fragment_listview);
		
        Bundle args = getArguments();
        final ArrayList<Subject> subjects = args.getParcelableArrayList("subjects");

        ArrayAdapter<Subject> adapter = new ArrayAdapter<Subject>(getActivity(), 0, subjects);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Subject click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).showClasses(subjects.get(position)); // TODO NJCTLNavActivity and the getClasses method of MainActivity
        																					// will need to be updated in order to add subjects. 
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subjects_fragment, container, false);   
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


