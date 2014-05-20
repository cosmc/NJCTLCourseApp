package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class DrawerActivity extends FragmentActivity
{
	private ListView mDrawerList;
	CustomListAdapter<Class> listAdapter;
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
		
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);
        
        listAdapter = new CustomListAdapter<Class>(this , R.layout.drawer_list , subscribedClasses, Color.WHITE, Color.GRAY);
        mDrawerList.setAdapter(listAdapter);
        
        // Uncomment this to auto open the drawer.
        //View drawer = findViewById(R.id.left_drawer);
        //mDrawerLayout.openDrawer(drawer);
        
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
	}
	
	public void setDrawerIntent(Intent intent)
	{
		intent.putParcelableArrayListExtra("subscribedClasses", subscribedClasses);
		intent.putParcelableArrayListExtra("classes", classes);
	}
	
	public ArrayList<Class> getSubscribedClasses()
	{
		return subscribedClasses;
	}
	
	protected void updateSubscriptions(Class theClass)
	{
		if(theClass.isSubscribed())
			listAdapter.update(theClass);
	}
}
