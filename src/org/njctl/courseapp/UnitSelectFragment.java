package org.njctl.courseapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

public class UnitSelectFragment extends ListFragment implements TwoStatesDecider<Unit>
{
	protected Unit currentSelectedUnit;
	TwoStatesAdapter<Unit> listAdapter;
	protected boolean dLBtnVisible;
	protected Button button;
	ProgressDialog progress;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.unitselect_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		UnitSelectActivity selector = (UnitSelectActivity) getActivity();
		Class theClass = selector.getNJCTLClass();
		
		button = (Button) getActivity().findViewById(R.id.button_unit_download);
		button.setVisibility(View.INVISIBLE);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				final Unit unit = (Unit) adapter.getItemAtPosition(position);
				
				if(unit.isDownloaded())
				{
					openUnit(unit);
				}
				else if(!unit.isDownloading())//display Download Button if unit isn't downloaded yet.
				{
					if (dLBtnVisible)
					{
						hideButton(new AnimationListener()
						{
							@Override
							public void onAnimationEnd(Animation arg0)
							{
								button.setText("Download " + unit.getTitle());
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
						button.setText("Download " + unit.getTitle());
						showButton();
					}
					currentSelectedUnit = unit;
					
					Log.v("NJCTLLOG", "Going to display download button for unit " + unit.getTitle());
				}
			}
		});
		
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(currentSelectedUnit != null)
				{
					UnitSelectActivity action = (UnitSelectActivity) getActivity();
					
					if(isNetworkAvailable())
					{
						currentSelectedUnit.download(action);
						
						progress = new ProgressDialog(action);
						progress.setTitle("Downloading");
						progress.setMessage("Please wait while downloading " + currentSelectedUnit.getTitle());
						progress.show();
						hideButton();
					}
					else
					{
						showNoConnectionDialog();
					}
				}
			}
		});

		listAdapter = new TwoStatesAdapter<Unit>(getActivity(), theClass.getUnits(), this);
        setListAdapter(listAdapter);
	}
	
	private void showNoConnectionDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
 
			// set title
			alertDialogBuilder.setTitle("No Connection");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Could not connect to the internet for downloading the materials. Please check your connection.")
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
	
	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager 
			  = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

	@Override
	public boolean isActive(Unit content) {
		return content.isDownloaded();
	}
	
	protected void openUnit(Unit unit)
	{
		Log.v("NJCTLLOG", "Going to open MaterialsActivity for unit " + unit.getTitle());
		UnitSelectActivity selector = (UnitSelectActivity) getActivity();
		
		Intent intent = new Intent(selector, MaterialsActivity.class);
		intent.putExtra("unit", unit);
		selector.setDrawerIntent(intent);			
		intent.putParcelableArrayListExtra("subscribedClasses", selector.getSubscribedClasses());
		
        startActivity(intent);
	}
	
	public void onUnitDownloaded(Unit unit)
	{
		listAdapter.update(unit);
		progress.dismiss();
		openUnit(unit);
	}
}
