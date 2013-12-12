package org.njctl.courseapp;

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

//import org.njctl.courseapp.R;

public class MainActivity extends ActionBarActivity implements NJCTLNavActivity {

	/**** Start of NJCTLNavActivity Methods ****/
	
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
        transaction.addToBackStack("Chapter List for " + theClass.getName());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	public String getDocStorageRoot() {
	// Return the path of the root directory for storing the teaching material documents.
		return "";
	}
	
	/**** End of NJCTLNavActivity Methods ****/
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
        	ArrayList<NJCTLChapter> mockChapters = new ArrayList<NJCTLChapter>();
            mockChapters.add(new NJCTLChapter("1. Kinematics"));
            mockChapters.add(new NJCTLChapter("2. Electrons"));
            mockChapters.add(new NJCTLChapter("3. Conformal Field Theories"));
            
            ArrayList<NJCTLClass> mockClasses = new ArrayList<NJCTLClass>();
            mockClasses.add(new NJCTLClass("Math", mockChapters));
            mockClasses.add(new NJCTLClass("Physics", mockChapters));
            mockClasses.add(new NJCTLClass("Chemistry", mockChapters));
            
            showClasses(mockClasses);
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
            case R.id.action_discover:
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
