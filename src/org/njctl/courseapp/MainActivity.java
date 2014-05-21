package org.njctl.courseapp;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Model;
import org.njctl.courseapp.model.ModelRetriever;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//import org.njctl.courseapp.R;

public class MainActivity extends DrawerActivity implements ModelRetriever,  TwoStatesDecider<Class>, DownloadFinishListener<Model> {

	private Model model;
	ProgressDialog progress;
	private TwoStatesAdapter<Class> listAdapter;
	private Button button;
	private boolean dLBtnVisible;
	private Class currentSelectedClass;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	Log.v("NJCTL", "called oncreate");
    	
        setContentView(R.layout.activity_main);
        getActionBar().setTitle("CTL Classes");
        
        button = (Button) findViewById(R.id.button_subscribe);
		button.setVisibility(View.INVISIBLE);
        
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_update:
            	runUpdate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected void runUpdate()
    {
    	if(isNetworkAvailable())
    	{
    		progress = new ProgressDialog(this);
    		progress.setTitle("Loading");
    		progress.setMessage("Wait while fetching data...");
    		progress.show();
    		
        	model.update(this);
    	}
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
		
		showDrawer();
		showSubscribe();
		
		if(subscribedClasses.size() > 0) // Start UnitSelectActivity
		{
			Class theClass = model.getLastOpenedClass();
			
			Intent intent = new Intent(this, UnitSelectActivity.class);
			intent.putParcelableArrayListExtra("subscribedClasses", subscribedClasses);
			intent.putParcelableArrayListExtra("classes", model.getClasses());
			intent.putExtra("class", theClass);
			
	        startActivity(intent);
		}
	}
	
	protected void showSubscribe()
	{
		listAdapter = new TwoStatesAdapter<Class>(this, classes, this);
		ListView list = (ListView) findViewById(R.id.subscribing_classes_list);
		list.setAdapter(listAdapter);
		
		list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				//TODO Show subscription button, do subscription stuff.
				final Class theClass = (Class) adapter.getItemAtPosition(position);
				final String text = theClass.isSubscribed() ? "Unsubscribe " + theClass.getTitle() : "Subscribe " + theClass.getTitle();
				
				if (dLBtnVisible)
				{
					hideButton(new AnimationListener()
					{
						@Override
						public void onAnimationEnd(Animation arg0)
						{
							button.setText(text);
							showButton();
						}
						public void onAnimationRepeat(Animation arg0)
						{
						}
						public void onAnimationStart(Animation arg0)
						{
						}
					});
				}
				else
				{
					button.setText(text);
					showButton();
				}
				currentSelectedClass = theClass;
			}
		});
		
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(currentSelectedClass != null)
				{
					if(currentSelectedClass.isSubscribed())
					{
						currentSelectedClass.unsubscribe();
						listAdapter.update(currentSelectedClass);
						updateSubscriptions(currentSelectedClass);
						hideButton();
					}
					else
					{
						currentSelectedClass.subscribe();
						listAdapter.update(currentSelectedClass);
						updateSubscriptions(currentSelectedClass);
						hideButton();
						openDrawer();
					}
				}
			}
		});
	}
	
	protected void hideButton(AnimationListener listener)
	{
		TranslateAnimation closeAnim=new TranslateAnimation(0.0f, 0.0f, 0.0f, button.getHeight());
		closeAnim.setDuration(100);
		if(listener != null) closeAnim.setAnimationListener(listener);
		button.startAnimation(closeAnim);
		dLBtnVisible = false;
		button.setVisibility(View.INVISIBLE);
	}
	
	protected void hideButton()
	{
		hideButton(null);
	}
	
	protected void showButton()
	{
		button.setVisibility(View.VISIBLE);
		TranslateAnimation openAnim=new TranslateAnimation(0.0f, 0.0f, button.getHeight(),
				0.0f);
		openAnim.setDuration(100);
		button.startAnimation(openAnim);
		dLBtnVisible = true;
	}
	
	public boolean isActive(Class content)
	{
		return content.isSubscribed();
	}

	// When update is done.
	public void onDownloaded(Model content)
	{
		if(progress != null)
			progress.dismiss();
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
