package org.njctl.courseapp;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Model;
import org.njctl.courseapp.model.ModelRetriever;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class MainActivity extends DrawerActivity implements ModelRetriever,  TwoStatesDecider<Class>, DownloadFinishListener<Model>
{
	private Model model;
	ProgressDialog progress;
	private TwoStatesAdapter<Class> listAdapter;
	private Button button;
	private boolean dLBtnVisible;
	private Class currentSelectedClass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		Log.v("NJCTL", "called oncreate");
		
		setContentView(R.layout.activity_main);
		getActionBar().setTitle("CTL Classes");
		
		button = (Button) findViewById(R.id.button_subscribe);
		button.setVisibility(View.INVISIBLE);
		
		if (savedInstanceState == null)
		{
			model = new Model(this);
			
			if(!model.fetchManifest(this))
			{
				progress = new ProgressDialog(this);
				progress.setTitle("Loading");
				progress.setMessage("Please wait while fetching courses...");
				progress.show();
			}
		}
		else
		{
			showActivity();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
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
			progress.setMessage("Please wait while fetching update...");
			progress.show();
			
			model.update(this);
		}
		else
		{
			showNoConnectionDialog();
		}
	}
	
	private void showNoConnectionDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
		// set title
		alertDialogBuilder.setTitle("No Connection");
 
			// set dialog message
		alertDialogBuilder
			.setMessage("Could not connect to the internet for updating. Please check your connection.")
			.setCancelable(false)
			.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
			// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
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
		
		showActivity();
	}
	
	protected void showActivity()
	{
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
				final String text = theClass.isSubscribed() ? "Unsubscribe from " + theClass.getTitle() : "Subscribe to " + theClass.getTitle();
				
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
				
				if(theClass.isSubscribed())
				{
					openDrawer();
				}
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
	
	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager 
			  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);
	  
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		
		savedInstanceState.putParcelableArrayList("classes", classes);
		savedInstanceState.putParcelableArrayList("subscribedClasses", subscribedClasses);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		
		classes = savedInstanceState.getParcelableArrayList("classes");
		subscribedClasses = savedInstanceState.getParcelableArrayList("subscribedClasses");
	}
}
