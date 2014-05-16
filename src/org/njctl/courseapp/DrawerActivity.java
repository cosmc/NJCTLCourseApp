package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public abstract class DrawerActivity extends Activity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ListView mainContainerList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
    	
		setContentView(R.layout.activity_main);
		
        Intent intent = getIntent();
        ArrayList<Class> mMyClassesList = intent.getParcelableArrayListExtra("subscribedClasses");
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        //DrawerAdapter drawerAdapter = new DrawerAdapter(this, 0, mMyClassesList);
        //mDrawerList.setAdapter(drawerAdapter);
        
        mDrawerList.setAdapter(new ArrayAdapter<Class>(this,
        		android.R.layout.simple_list_item_1, mMyClassesList));

        
        //TODO DONT AUTO OPEN THE DRAWER?
        mDrawerLayout.openDrawer(mDrawerList);
        
        //TODO SET CLICK LISTENER
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener(model, mDrawerLayout, mDrawerList));
	}
}
