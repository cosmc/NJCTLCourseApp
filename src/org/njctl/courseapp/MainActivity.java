package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Model;
import org.njctl.courseapp.model.ModelRetriever;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

//import org.njctl.courseapp.R;

public class MainActivity extends ActionBarActivity implements ModelRetriever {

	private Model model;
	ProgressDialog progress;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	Log.v("NJCTL", "called oncreate");
    	//TODO Put in loading gif.
        setContentView(R.layout.activity_main);
        
        progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("Wait while fetching data...");
		progress.show();
        
    	model = new Model(this);
    	
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
            case R.id.action_home:
            	// Build the class tree!
            	//ArrayList<NJCTLClass> classes = model.getClassTree( getResources().getString(R.string.course_manifest_rel_path), getResources());
                // Display the classes!
            	showSubscribe();
                return true;
            /*case R.id.action_settings:
            	Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            	startActivity(intent);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    //populate the drawer with my classes
    //TODO pull out drawer specific stuff into its own method
	public void onModelReady()
	{
		Log.v("NJCTLModel", "Model Ready.");
		//TODO take out later, fake class subscription for testing.
		//this.subjects.get(0).getContents().get(0).subscribe();
		
		progress.dismiss();
		
		ArrayList<Class> myClasses = model.getClassesSubscribed();
		
		if(myClasses.size() > 0) // Start UnitSelectActivity
		{
			Class theClass = model.getLastOpenedClass();
			
			Intent intent = new Intent(this, UnitSelectActivity.class);
			intent.putParcelableArrayListExtra("subscribedClasses", myClasses);
			intent.putParcelableArrayListExtra("classes", model.getClasses());
			intent.putExtra("class", theClass);
			
	        startActivity(intent);
		}
		else // Start SubscribeActivity
		{
			showSubscribe(myClasses);
		}
	}
	
	protected void showSubscribe(ArrayList<Class> myClasses)
	{
		Intent intent = new Intent(this, SubscribeActivity.class);
		intent.putParcelableArrayListExtra("subscribedClasses", myClasses);
		intent.putParcelableArrayListExtra("classes", model.getClasses());
		
        startActivity(intent);
	}
	
	protected void showSubscribe()
	{
		showSubscribe(model.getClassesSubscribed());
	}
}
