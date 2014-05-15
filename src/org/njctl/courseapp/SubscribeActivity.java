package org.njctl.courseapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ListView;
import org.njctl.courseapp.model.Class;

public class SubscribeActivity extends Activity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
    	
    	//TODO set this to logo + spinner
        setContentView(R.layout.activity_init);
        
        Intent intent = getIntent();
        @SuppressWarnings("unchecked")
		ArrayList<Class> mMyClassesList = (ArrayList<Class>) intent.getSerializableExtra("subscribedClasses");
        
        Log.v("MYCLASSESLIST ARRAY LIST IS IS IS SI SIS IS", mMyClassesList.toString());
        
        String[] mMyClasses = new String[mMyClassesList.size()];
		for (int i=0; i < mMyClassesList.size(); ++i) {
			mMyClasses[i] = mMyClassesList.get(i).toString();
		}
		
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.v("mDrawerLayout", mDrawerLayout.toString());
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
  
      //TODO set adapter
        MyClassesAdapter myClassesAdapter = new MyClassesAdapter(this, 0, mMyClassesList);
        mDrawerList.setAdapter(myClassesAdapter);
        
        //TODO DONT AUTO OPEN THE DRAWER?
        mDrawerLayout.openDrawer(mDrawerList);
        
        //TODO SET CLICK LISTENER
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener(model, mDrawerLayout, mDrawerList));
	}
}
