package org.njctl.courseapp;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//import org.njctl.courseapp.R;

public class MainActivity extends DrawerActivity implements ModelRetriever,  TwoStatesDecider<Class> {

	private Model model;
	ProgressDialog progress;
	private TwoStatesAdapter<Class> listAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	Log.v("NJCTL", "called oncreate");
    	
        setContentView(R.layout.activity_main);
        
    	model = new Model(this);
    	
    	if (savedInstanceState == null) {
        	
            if(!model.fetchManifest(this))
            {
            	progress = new ProgressDialog(this);
        		progress.setTitle("Loading");
        		progress.setMessage("Wait while fetching data...");
        		progress.show();
            }
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
	public void onModelReady()
	{
		Log.v("NJCTLModel", "Model Ready.");
		
		classes = model.getClasses();
		subscribedClasses = model.getClassesSubscribed();
		
		if(progress != null)
		{
			progress.dismiss();
		}
		
		ArrayList<Class> myClasses = model.getClassesSubscribed();
		
		showDrawer();
		showSubscribe();
		
		if(myClasses.size() > 0) // Start UnitSelectActivity
		{
			Class theClass = model.getLastOpenedClass();
			
			Intent intent = new Intent(this, UnitSelectActivity.class);
			intent.putParcelableArrayListExtra("subscribedClasses", myClasses);
			intent.putParcelableArrayListExtra("classes", model.getClasses());
			intent.putExtra("class", theClass);
			
	        startActivity(intent);
		}
	}
	
	protected void showSubscribe()
	{
		/*Intent intent = new Intent(this, SubscribeActivity.class);
		intent.putParcelableArrayListExtra("subscribedClasses", myClasses);
		intent.putParcelableArrayListExtra("classes", model.getClasses());
		
        startActivity(intent);*/
		/*
		getActionBar().setTitle("Classes");
		
		SubscribeFragment frag = new SubscribeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).commit();*/
		
		listAdapter = new TwoStatesAdapter<Class>(this, classes, this);
		ListView list = (ListView) findViewById(R.id.subscribing_classes_list);
		list.setAdapter(listAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				//TODO Show subscription button, do subscription stuff.
				Class theClass = (Class) adapter.getItemAtPosition(position);
				theClass.subscribe();
				listAdapter.update(theClass);
				updateSubscriptions(theClass);
			}
		});
	}
	
	public boolean isActive(Class content)
	{
		return content.isSubscribed();
	}
	
	public List<Class> getClasses()
	{
		return classes;
	}
}
