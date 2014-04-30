package org.njctl.courseapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import org.njctl.courseapp.model.Model;
import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.NJCTLDocList;
import org.njctl.courseapp.model.Subject;
import org.njctl.courseapp.model.SubjectRetriever;
import org.njctl.courseapp.model.Unit;

//import org.njctl.courseapp.R;

public class MainActivity extends ActionBarActivity implements NJCTLNavActivity, SubjectRetriever {

	private Model model = new Model();
	private ArrayList<Subject> subjects;
	
	/**** Start of NJCTLNavActivity Methods ****/
	
	// TODO: Condense the NJCTLNavActivity interface's "show" methods into a single method to avoid repetitiveness.
	
	public void showSubjects(ArrayList<Subject> subjects) {
	// Populate a SubjectsFragment with the given list of classes and display it in the container element.
		SubjectsFragment frag = new SubjectsFragment();
		
		// Build the list of already downloaded classes.
		ArrayList<Class> myClasses = new ArrayList<Class>();
		for (int i=0; i < subjects.size(); ++i) {
			myClasses.addAll(subjects.get(i).getClassesDownloaded());
		}
		
		Bundle args = new Bundle();
		args.putParcelableArrayList("myClasses", myClasses);
		args.putParcelableArrayList("subjects", subjects);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack("Subject List");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	public void showClasses(Subject subject) {
	// Populate a ClassesFragment with the given list of classes and display it in the container element.
		ClassesFragment frag = new ClassesFragment();
		Bundle args = new Bundle();
		args.putParcelable("subject", subject);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack("Class List for " + subject.getTitle());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	public void showChapters(Class theClass) {
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
	
	public void showUnits(Class theClass) {
	// Populate a UnitListFragment with the units of the given class and display it in the container element.
		UnitsFragment frag = new UnitsFragment();
		Bundle args = new Bundle();
		args.putParcelable("class", theClass);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack("Unit List for " + theClass.getTitle());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
	}
	
	public void showDocuments(Unit theUnit) {
	// Populate a TopicListFragment with the topics of the given unit and display it in the container element.
		DocumentsFragment frag = new DocumentsFragment();
		Bundle args = new Bundle();
		args.putParcelable("unit", theUnit);
		frag.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    transaction.replace(R.id.container, frag);
	    transaction.addToBackStack("Document List for " + theUnit.getTitle());
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
        	
        	//useClasses(model.getClassTree( getResources().getString(R.string.course_manifest_rel_path), getResources()));
            
            model.fetchManifest(this);
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
            	//ArrayList<NJCTLClass> classes = model.getClassTree( getResources().getString(R.string.course_manifest_rel_path), getResources());
                // Display the classes!
                showSubjects(subjects);
                return true;
            case R.id.action_settings:
            	Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            	startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void useSubjects(ArrayList<Subject> subjects)
	{
		this.subjects = subjects;
		showSubjects(subjects);
	}

	@Override
	public void openDocument(org.njctl.courseapp.model.material.Document doc)
	{
		try {
			
			// Get an input stream for the locally stored file.
    		BufferedInputStream docStream = new BufferedInputStream( getResources().getAssets().open( doc.getRelativePathForOpening() ) );
    		//create a buffer that has the same size as the InputStream  
            byte[] buffer = new byte[docStream.available()];
            //read the text file as a stream, into the buffer
            docStream.read(buffer);
            //create a output stream to write the buffer into
            // TODO: Context.MODE_WORLD_READABLE is deprecated. Look into replacing this with a ContentProvider.
            BufferedOutputStream outStream = new BufferedOutputStream( openFileOutput(doc.getFileName(), Context.MODE_WORLD_READABLE) );  
            //write this buffer to the output stream  
            outStream.write(buffer);  
            //Close the Input and Output streams  
            outStream.close();  
            docStream.close();
    		
            // Try to launch the PDF in a PDF viewer.
    		Intent intent = new Intent(Intent.ACTION_VIEW);
    		File docFile = new File(getFilesDir(), doc.getFileName());
    		docFile.setReadable(true, false);
    		docFile.setWritable(true, false); // So that people can take notes in the PDF reader.
    		Uri docUri = Uri.fromFile(docFile);
    		intent.setDataAndType(docUri,doc.getMIMEType());
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(intent);
    		
		} catch (ActivityNotFoundException e) {
			// Show an error message if the user does not have an appropriate application for opening the document.
			Log.w("ERROR", e.toString());
			Toast.makeText(this, "Error: No activity found for viewing MIME type " + doc.getMIMEType() + ".", Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			// Show an error message if the file isn't there.
			Log.w("ERROR", e.toString());
			Toast.makeText(this, "Error: Could not open file / file not found.", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Log.w("ERROR", e.toString());
		}
	}

}
