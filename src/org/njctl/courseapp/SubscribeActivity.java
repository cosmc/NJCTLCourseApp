package org.njctl.courseapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import org.njctl.courseapp.model.Class;

public class SubscribeActivity extends DrawerActivity
{
	protected ArrayList<Class> classes;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //setContentView(R.layout.activity_init);
		
		Intent intent = getIntent();
		classes = intent.getParcelableArrayListExtra("classes");

		getActionBar().setTitle("Classes");
		
		SubscribeFragment frag = new SubscribeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
        //setListAdapter(new ArrayAdapter<Unit>(getActivity(), android.R.layout.simple_list_item_activated_1,
        		//		theClass.getUnits()));
    	//TODO show list of classes.
        
	}
	
	public List<Class> getClasses()
	{
		return classes;
	}
}
