package org.njctl.courseapp;

import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

//import org.njctl.courseapp.R;

public class MainActivity extends ActionBarActivity implements NJCTLNavActivity {

	/**** Start of NJCTLNavActivity Methods ****/
	
	// TODO: Condense the NJCTLNavActivity interface's "show" methods into a single method to avoid repetitiveness.
	
	public void showClasses(ArrayList<NJCTLClass> classes) {
	// Populate a ClassFragment with the given list of classes and display it in the container element.
		ClassesFragment frag = new ClassesFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("classes", classes);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack("Class List");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	public void showChapters(NJCTLClass theClass) {
	// Populate a ChapterListFragment with the chapters of the given class and display it in the container element.
		ChapterListFragment frag = new ChapterListFragment();
		Bundle args = new Bundle();
		args.putParcelable("class", theClass);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack("Chapter List for " + theClass.getTitle());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	public void showDocList(NJCTLDocList docList) {
	// Populate a DocListFragment with the documents of the given document list and display it in the container element.
		DocListFragment frag = new DocListFragment();
		Bundle args = new Bundle();
		args.putParcelable("docList", docList);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack("Document List for " + docList.getTitle());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	/**** End of NJCTLNavActivity Methods ****/
	
	private ArrayList<NJCTLClass> getClassTree(String relativePath) {
	// Build all of the necessary NJCTLClass objects from the JSON course manifest file.
		Resources resources = getResources();
		StringBuilder text = new StringBuilder();
		BufferedReader br = null;
		String line;
		
		// Build a StringBuilder from the manifest file.
		try {
		    br = new BufferedReader(new InputStreamReader(resources.getAssets().open( relativePath )));
		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		} catch (IOException e) {
		    // do exception handling
			Log.w("ERR", e.toString());
		} finally {
		    try { br.close(); } catch (Exception e) { Log.w("ERR", e.toString()); }
		}
		
		// Now build the actual class tree!
		// TODO: Okay this is questionable. We probably should have some kind of recursive datatype like "NJCTLContainer" or something.
		// For now, though, the rules:
		// - The top level JSON object has a JSON array called "classes".
		// - Each element of "classes" has a string "title" and a JSON array "chapters".
		// - Each element of each "chapters" has a string "title" and a JSON array "doclists".
		// - Each element of each "doclists" has a string "title" and a JSON array "documents".
		// - Each element of each "documents" is a string indicating the filename.
		// - The relative path from the assets directory to each document is courses/[classtitle]/[chaptertitle]/[doclisttitle]/[filename]
		
		ArrayList<NJCTLClass> njctlClasses = new ArrayList<NJCTLClass>(); // We'll spend the rest of this method filling this dude, and then return him.
		
		try {
			
			JSONObject courseManifest = new JSONObject(text.toString()); // Build the course manifest as a JSON object from the StringBuilder.
			JSONArray classes = courseManifest.getJSONArray("classes");  // Get the list of class objects.
			
			for (int i = 0; i < classes.length(); ++i) {
				JSONObject currentClass = classes.getJSONObject(i);
				JSONArray chapters = currentClass.getJSONArray("chapters");
				ArrayList<NJCTLChapter> njctlChapters = new ArrayList<NJCTLChapter>();
				
				for (int j = 0; j < chapters.length(); ++j) {
					JSONObject currentChapter = chapters.getJSONObject(j);
					JSONArray docLists = currentChapter.getJSONArray("doclists");
					ArrayList<NJCTLDocList> njctlDocLists = new ArrayList<NJCTLDocList>();
					
					for (int k = 0; k < docLists.length(); ++k) {
						JSONObject currentDocList = docLists.getJSONObject(k);
						JSONArray docs = currentDocList.getJSONArray("documents");
						ArrayList<NJCTLDocument> njctlDocs = new ArrayList<NJCTLDocument>();
						
						for (int l = 0; l < docs.length(); ++l) {
							// Construct the path to the document from the assets folder.
							String pathToDoc = "courses/" + currentClass.getString("title") + "/" + currentChapter.getString("title") + "/" + currentDocList.getString("title") + "/" + docs.getString(l);
							njctlDocs.add(new NJCTLDocument(pathToDoc)); // Add the document!
						}
						njctlDocLists.add(new NJCTLDocList(currentDocList.getString("title"), njctlDocs));
					}
					njctlChapters.add(new NJCTLChapter(currentChapter.getString("title"), njctlDocLists));
				}
				njctlClasses.add(new NJCTLClass(currentClass.getString("title"), njctlChapters));
			}
		} catch (JSONException e) { Log.w("JSON ERR", e.toString()); }
		
		return njctlClasses; // Now I can't look up from my screen without seeing curly braces everywhere.
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
        	// Build the class tree!
        	ArrayList<NJCTLClass> classes = getClassTree( getResources().getString(R.string.course_manifest_rel_path) );
            // Display the classes!
            showClasses(classes);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_my_classes:
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
