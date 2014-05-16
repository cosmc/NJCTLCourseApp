package org.njctl.courseapp;

import android.content.Intent;
import android.os.Bundle;

import org.njctl.courseapp.model.Class;

public class UnitSelectActivity extends DrawerActivity
{
	protected Class theClass;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		theClass = (Class) intent.getParcelableExtra("class");
		theClass.setLastOpened();
		
		UnitSelectFragment select = new UnitSelectFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, select).commit();
	}
	
	public Class getNJCTLClass()
	{
		return theClass;
	}
}
