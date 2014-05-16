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

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

public class UnitSelectFragment extends ListFragment
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

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				Unit unit = (Unit) adapter.getItemAtPosition(position);
				Log.v("NJCTLUnitSelect", "Downloading unit " + unit.getTitle());


				

				UnitSelectActivity selector = (UnitSelectActivity) getActivity();
				
				Intent intent = new Intent(selector, MaterialsActivity.class);
				//intent.putParcelableArrayListExtra("subscribedClasses", myClasses);
				intent.putExtra("unit", unit);
				intent.putParcelableArrayListExtra("subscribedClasses", selector.getSubscribedClasses());
				
		        startActivity(intent);
				//unit.download();
				// assuming string and if you want to get the value on click of
				// list item
				// do what you intend to do on click of listview row
			}
		});

		setListAdapter(new ArrayAdapter<Unit>(getActivity(), android.R.layout.simple_list_item_activated_1,
				theClass.getUnits()));
	}
}
