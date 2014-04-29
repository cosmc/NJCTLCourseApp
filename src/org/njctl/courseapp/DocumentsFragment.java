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

import org.njctl.courseapp.model.Document;
import org.njctl.courseapp.model.Unit;

/**
 * Created by Colin on 28 April 2014.
 */

public class DocumentsFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView topicsListView = (ListView) getView().findViewById(R.id.topics_listview);
        ListView handoutsListView = (ListView) getView().findViewById(R.id.handouts_listview);
        ListView homeworkListView = (ListView) getView().findViewById(R.id.homework_listview);
        ListView labsListView = (ListView) getView().findViewById(R.id.labs_listview);
		
        Bundle args = getArguments();
        final Unit unit = args.getParcelable("unit");

        ArrayAdapter<Document> topicsAdapter = new ArrayAdapter<Document>(getActivity(), 0, unit.getTopics());
        topicsListView.setAdapter(topicsAdapter);
        ArrayAdapter<Document> handoutsAdapter = new ArrayAdapter<Document>(getActivity(), 0, unit.getHandouts());
        handoutsListView.setAdapter(handoutsAdapter);
        ArrayAdapter<Document> homeworkAdapter = new ArrayAdapter<Document>(getActivity(), 0, unit.getHomework());
        topicsListView.setAdapter(homeworkAdapter);
        ArrayAdapter<Document> labsAdapter = new ArrayAdapter<Document>(getActivity(), 0, unit.getLabs());
        topicsListView.setAdapter(labsAdapter);
        
        topicsListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Log.w("Topic click", "" + position);
        		try {
        			((NJCTLNavActivity) getActivity()).openDocument(unit.getTopics().get(position));
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
        			((NJCTLNavActivity) getActivity()).openDocument(unit.getHomework().get(position));
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
