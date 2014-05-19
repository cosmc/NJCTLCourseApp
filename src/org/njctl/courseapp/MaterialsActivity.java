package org.njctl.courseapp;

import org.njctl.courseapp.model.Unit;

import android.content.Intent;
import android.os.Bundle;

public class MaterialsActivity extends DrawerActivity
{
	protected Unit unit;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		unit = (Unit) intent.getParcelableExtra("unit");
		unit.getCTLClass().setLastOpened();
		
		getActionBar().setTitle(unit.getTitle());
		
		MaterialsFragment select = new MaterialsFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, select).commit();
	}
	
	public Unit getUnit()
	{
		return unit;
	}
}
