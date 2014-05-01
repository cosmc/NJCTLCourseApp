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
 * Created by Colin on ??.
 */

public class SubjectsFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		ListView myClassesListView = (ListView) getView().findViewById(R.id.my_classes_listview);
        ListView subjectsListView = (ListView) getView().findViewById(R.id.subjects_listview);
		
        Bundle args = getArguments();
        final ArrayList<Class> myClasses = args.getParcelableArrayList("myClasses");
        final ArrayList<Subject> subjects = args.getParcelableArrayList("subjects");
        
        for(int i = 0; i < subjects.size(); i++)
        {
        	Log.v("NJCTLLOG2", subjects.get(i).getTitle());
        }

        MyClassesAdapter myClassesAdapter = new MyClassesAdapter(getActivity(), 0, myClasses);
        myClassesListView.setAdapter(myClassesAdapter);
        SubjectsAdapter subjectAdapter = new SubjectsAdapter(getActivity(), 0, subjects);
        subjectsListView.setAdapter(subjectAdapter);
        
        myClassesListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("My Classes click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).showUnits(myClasses.get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
        
        subjectsListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Subject click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).showClasses(subjects.get(position));
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


