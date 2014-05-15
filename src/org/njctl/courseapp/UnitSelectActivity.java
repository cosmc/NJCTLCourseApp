package org.njctl.courseapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

public class UnitSelectActivity extends DrawerActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Class theClass = (Class) intent.getParcelableExtra("class");
		theClass.setLastOpened();
		
		//setContentView(R.layout.activity_init);
		
		//TODO show theClass info and theClasses units.
		String title = theClass.getTitle();
		ArrayList<Unit> units = theClass.getUnits();
	}
}
