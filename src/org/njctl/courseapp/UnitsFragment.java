package org.njctl.courseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

/**
 * Created by Colin on 28 April 2014.
 */

public class UnitsFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView listView = (ListView) getView().findViewById(R.id.units_fragment_listview);
        
        Bundle args = getArguments();
        final Class theClass = args.getParcelable("class");

        ArrayAdapter<Unit> adapter = new ArrayAdapter<Unit>(getActivity(), 0, theClass.getContents());
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Unit click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).showDocuments(theClass.getContents().get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        });
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.units_fragment, container, false);
        return v;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	
    	// Set the title text to the app name.
    	Class theClass = (Class) getArguments().getParcelable("class");
		getActivity().setTitle(theClass.getTitle());
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}


