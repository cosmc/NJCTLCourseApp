package org.njctl.courseapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import org.njctl.courseapp.model.Model;
import org.njctl.courseapp.model.NJCTLClass;
import org.njctl.courseapp.model.NJCTLDocList;

//import org.njctl.courseapp.R;

public class MainActivity extends ActionBarActivity implements NJCTLNavActivity {

	private Model model = new Model();
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
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
        	// Build the class tree!
        	ArrayList<NJCTLClass> classes = model.getClassTree( getResources().getString(R.string.course_manifest_rel_path), getResources());
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
            	// Build the class tree!
            	ArrayList<NJCTLClass> classes = model.getClassTree( getResources().getString(R.string.course_manifest_rel_path), getResources());
                // Display the classes!
                showClasses(classes);
                return true;
            case R.id.action_settings:
            	Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            	startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
