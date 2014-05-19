package org.njctl.courseapp;

import java.util.List;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class SubscribeFragment extends ListFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.subscribe_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		SubscribeActivity selector = (SubscribeActivity) getActivity();
		List<Class> classes = selector.getClasses();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				Class theClass = (Class) adapter.getItemAtPosition(position);
				/*
				if(unit.isDownloaded())
				{
					// Open MaterialsActivity.
					Log.v("NJCTLLOG", "Going to open MaterialsActivity for unit " + unit.getTitle());
					UnitSelectActivity selector = (UnitSelectActivity) getActivity();
					
					Intent intent = new Intent(selector, MaterialsActivity.class);
					intent.putExtra("unit", unit);
					intent.putParcelableArrayListExtra("subscribedClasses", selector.getSubscribedClasses());
					
			        startActivity(intent);
				}
				else
				{
					//TODO: Display download button, and then call unit.download();
					unit.download();
					Log.v("NJCTLLOG", "Going to display download button for unit " + unit.getTitle());
				}*/
			}
		});

		//CustomListAdapter<Class> listAdapter = new CustomListAdapter<Class>(this , R.layout.drawer_list , subscribedClasses, Color.WHITE, Color.GRAY);
        //mDrawerList.setAdapter(listAdapter);
		
		setListAdapter(new ArrayAdapter<Class>(getActivity(), android.R.layout.simple_list_item_activated_1,
				classes));
	}
}
