package org.njctl.courseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

public class UnitSelectFragment extends ListFragment implements TwoStatesDecider<Unit>
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.unitselect_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		UnitSelectActivity selector = (UnitSelectActivity) getActivity();
		Class theClass = selector.getNJCTLClass();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				Unit unit = (Unit) adapter.getItemAtPosition(position);
				
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
					SlidingDrawer dLButtonDrawer = (SlidingDrawer) getActivity().findViewById(R.id.slidingDrawer);
					dLButtonDrawer.animateOpen();
					
					//TODO: Display download button, and then call unit.download();
					
					Log.v("NJCTLLOG", "Going to display download button for unit " + unit.getTitle());
				}
			}
		});

		//setListAdapter(new ArrayAdapter<Unit>(getActivity(), android.R.layout.simple_list_item_activated_1,
				//theClass.getUnits()));
		TwoStatesAdapter<Unit> listAdapter = new TwoStatesAdapter<Unit>(getActivity(), theClass.getUnits(), this);
        setListAdapter(listAdapter);
	}

	@Override
	public boolean isActive(Unit content) {
		return content.isDownloaded();
	}
}
