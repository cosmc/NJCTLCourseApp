package org.njctl.courseapp;

import android.content.Intent;
import android.os.Bundle;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

public class UnitSelectActivity extends DrawerActivity implements DownloadFinishListener<Unit>
{
	protected Class theClass;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		theClass = (Class) intent.getParcelableExtra("class");
		theClass.setLastOpened();
		
		getActionBar().setTitle(theClass.getTitle());
		
		UnitSelectFragment select = new UnitSelectFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, select).commit();
	}
	
	public Class getNJCTLClass()
	{
		return theClass;
	}

	public void onDownloaded(Unit content)
	{
		// TODO Auto-generated method stub
		//getFragmentManager().beginTransaction().replace(R.layout.unitselect_fragment, new UnitSelectFragment());
		
		//UnitSelectFragment select = new UnitSelectFragment();
		//getSupportFragmentManager().beginTransaction().replace(R.id.container, select).commit();
	}
}
