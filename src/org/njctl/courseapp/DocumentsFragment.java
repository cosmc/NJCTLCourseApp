package org.njctl.courseapp;

import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.material.Handout;
import org.njctl.courseapp.model.material.Homework;
import org.njctl.courseapp.model.material.Presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Colin on 28 April 2014.
 */

public class DocumentsFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView presentationsListView = (ListView) getView().findViewById(R.id.presentations_listview);
        ListView handoutsListView = (ListView) getView().findViewById(R.id.handouts_listview);
        ListView homeworkListView = (ListView) getView().findViewById(R.id.homework_listview);
        ListView labsListView = (ListView) getView().findViewById(R.id.labs_listview);
		
        Bundle args = getArguments();
        final Unit unit = args.getParcelable("unit");

        ArrayAdapter<Presentation> presentationsAdapter = new ArrayAdapter<Presentation>(getActivity(), 0, unit.getPresentations());
        presentationsListView.setAdapter(presentationsAdapter);
        ArrayAdapter<Handout> handoutsAdapter = new ArrayAdapter<Handout>(getActivity(), 0, unit.getHandouts());
        handoutsListView.setAdapter(handoutsAdapter);
        ArrayAdapter<Homework> homeworkAdapter = new ArrayAdapter<Homework>(getActivity(), 0, unit.getHomeworks());
        //TODO Colin, this doesnt work yet..
        /*
        topicsListView.setAdapter(homeworkAdapter);
        ArrayAdapter<Lab> labsAdapter = new ArrayAdapter<Lab>(getActivity(), 0, unit.getLabs());
        topicsListView.setAdapter(labsAdapter);
        */
        presentationsListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Presentation click", "" + position);
        		try {
        			//TODO
        			//((NJCTLNavActivity) getActivity()).openDocument(unit.getPresentations().get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        });
        
        handoutsListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Handout click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).openDocument(unit.getHandouts().get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
        
        homeworkListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Homework click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).openDocument(unit.getHomeworks().get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
        
        labsListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Lab click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).openDocument(unit.getLabs().get(position));
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
        
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.documents_fragment, container, false);   
        return v;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	
    	// Set the title text to the app name.
    	Unit unit = (Unit) getArguments().getParcelable("unit");
		getActivity().setTitle(unit.getTitle());
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}
