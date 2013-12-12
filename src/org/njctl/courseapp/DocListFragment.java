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
import android.net.Uri;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Colin on 12/12/13.
 */

public class DocListFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView listView = (ListView) getView().findViewById(R.id.doc_list_fragment_listview);
		
        Bundle args = getArguments();
        final ArrayList<NJCTLDocument> docs = args.getParcelableArrayList("docs");

        int numDocs = docs.size();
        String[] docTitles = new String[numDocs];
        for (int i = 0; i < numDocs; ++i) {
        	docTitles[i] = docs.get(i).getTitle();
        }
        // TODO: We'll want a custom adapter for displaying DocLists eventually.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, docTitles);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		try {
	        		File docFile = new File( ((NJCTLNavActivity) getActivity()).getDocStorageRoot() + docs.get(position).getRelativePath() );
	        		Intent intent = new Intent(Intent.ACTION_VIEW);
	        		Uri data = Uri.fromFile(docFile);
	        		intent.setDataAndType(data,"application/pdf");
	        		startActivity(intent);
        		} catch (ClassCastException e) {
        			Log.w("ERROR", "Activity does not implement NJCTLNavActivity.");
        		}
            }
        }); 
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doc_list_fragment, container, false);   
        return v;
    }
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}