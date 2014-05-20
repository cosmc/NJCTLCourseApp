package org.njctl.courseapp;

import android.content.Intent;
import android.os.Bundle;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

public class UnitSelectActivity extends DrawerActivity implements DownloadFinishListener<Unit>
{
	protected Class theClass;
	protected UnitSelectFragment fragment;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		theClass = (Class) intent.getParcelableExtra("class");
		theClass.setLastOpened();
		
		getActionBar().setTitle(theClass.getTitle());
		
		fragment = new UnitSelectFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
	}
	
	public Class getNJCTLClass()
	{
		return theClass;
	}

	public void onDownloaded(final Unit content)
	{
		runOnUiThread(new Runnable() {
		     @Override
		     public void run()
		     {
		    	 fragment.onUnitDownloaded(content);
		    }
		});
		
		//UnitSelectFragment select = new UnitSelectFragment();
		//getSupportFragmentManager().beginTransaction().replace(R.id.container, select).commit();
	}
}
