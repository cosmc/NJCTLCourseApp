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
import android.widget.Toast;
import android.util.Log;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.content.ActivityNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Colin on 12/12/13.
 */

public class DocListFragment extends Fragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
        ListView listView = (ListView) getView().findViewById(R.id.doc_list_fragment_listview);
		
        Bundle args = getArguments();
        NJCTLDocList docList = args.getParcelable("docList");
        final ArrayList<NJCTLDocument> docs = docList.getContents();

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
        			
        			// Get an input stream for the locally stored file.
	        		BufferedInputStream docStream = new BufferedInputStream( getActivity().getResources().getAssets().open( docs.get(position).getRelativePath() ) );
	        		//create a buffer that has the same size as the InputStream  
	                byte[] buffer = new byte[docStream.available()];
	                //read the text file as a stream, into the buffer  
	                docStream.read(buffer);
	                //create a output stream to write the buffer into
	                // TODO: Context.MODE_WORLD_READABLE is deprecated. Look into replacing this with a ContentProvider.
	                BufferedOutputStream outStream = new BufferedOutputStream( getActivity().openFileOutput(docs.get(position).getFileName(), Context.MODE_WORLD_READABLE) );  
	                //write this buffer to the output stream  
	                outStream.write(buffer);  
	                //Close the Input and Output streams  
	                outStream.close();  
	                docStream.close();
	        		
	                // Try to launch the PDF in a PDF viewer.
	        		Intent intent = new Intent(Intent.ACTION_VIEW);
	        		File docFile = new File(getActivity().getFilesDir(), docs.get(position).getFileName());
	        		docFile.setReadable(true, false);
	        		Uri docUri = Uri.fromFile(docFile);
	        		intent.setDataAndType(docUri,docs.get(position).getMIMEType());
	        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivity(intent);
	        		
        		} catch (ActivityNotFoundException e) {
        			// Show an error message if the user does not have an appropriate application for opening the document.
        			Log.w("ERROR", e.toString());
        			Toast.makeText(getActivity(), "Error: No activity found for viewing MIME type " + docs.get(position).getMIMEType() + ".", Toast.LENGTH_SHORT).show();
        		} catch (FileNotFoundException e) {
        			// Show an error message if the file isn't there.
        			Log.w("ERROR", e.toString());
        			Toast.makeText(getActivity(), "Error: Could not open file / file not found.", Toast.LENGTH_SHORT).show();
        		} catch (IOException e) {
        			Log.w("ERROR", e.toString());
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
    public void onStart() {
    	super.onStart();
    	
    	// Set the title text to the title of the doc list whose contents are being shown.
		NJCTLDocList docList = (NJCTLDocList) getArguments().getParcelable("docList");
		getActivity().setTitle(docList.getTitle());
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
}