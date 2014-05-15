package org.njctl.courseapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import org.njctl.courseapp.model.Class;

public class SubscribeActivity extends DrawerActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //setContentView(R.layout.activity_init);
		
		Intent intent = getIntent();
        @SuppressWarnings("unchecked")
		ArrayList<Class> classes = (ArrayList<Class>) intent.getSerializableExtra("classes");
    	//TODO show list of classes.
        
	}
}
