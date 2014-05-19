package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public abstract class DrawerActivity extends FragmentActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ListView mainContainerList;
	protected ArrayList<Class> subscribedClasses;
	protected ArrayList<Class> classes;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
    	
		setContentView(R.layout.activity_main);
		
        Intent intent = getIntent();
        subscribedClasses = intent.getParcelableArrayListExtra("subscribedClasses");
        classes = intent.getParcelableArrayListExtra("classes");
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);
        
        View drawer = findViewById(R.id.left_drawer);
        
        //DrawerAdapter drawerAdapter = new DrawerAdapter(this, 0, mMyClassesList);
        //mDrawerList.setAdapter(drawerAdapter);
        
        CustomListAdapter<Class> listAdapter = new CustomListAdapter<Class>(this , R.layout.drawer_list , subscribedClasses, Color.WHITE, Color.GRAY);
        mDrawerList.setAdapter(listAdapter);
        /*
        mDrawerList.setAdapter(new ArrayAdapter<Class>(this,
        		android.R.layout.simple_list_item_1, subscribedClasses));
		*/
        
        //TODO DONT AUTO OPEN THE DRAWER?
        //mDrawerLayout.openDrawer(mDrawerList);
        mDrawerLayout.openDrawer(drawer);
        
        Button button = (Button) findViewById(R.id.button_manage_subscriptions);
        button.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(v.getContext(), SubscribeActivity.class);
				setDrawerIntent(intent);
		        startActivity(intent);
			}
		});
        
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				Class theClass = (Class) adapter.getItemAtPosition(position);
				
				Intent intent = new Intent(v.getContext(), UnitSelectActivity.class);
				setDrawerIntent(intent);
				intent.putExtra("class", theClass);
				startActivity(intent);
			}
		});
        //TODO SET CLICK LISTENER
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener(model, mDrawerLayout, mDrawerList));
	}
	
	protected void setDrawerIntent(Intent intent)
	{
		intent.putParcelableArrayListExtra("subscribedClasses", subscribedClasses);
		intent.putParcelableArrayListExtra("classes", classes);
	}
	
	public ArrayList<Class> getSubscribedClasses()
	{
		return subscribedClasses;
	}
}
