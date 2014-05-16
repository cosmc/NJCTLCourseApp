package org.njctl.courseapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.njctl.courseapp.R.id;
import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.material.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleExpandableListAdapter;

public class MaterialsFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.materials_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		MaterialsActivity selector = (MaterialsActivity) getActivity();
		Unit unit = selector.getUnit();
		
		ListView homeworkView = (ListView) getView().findViewById(id.materials_fragment_listview_homeworks);
		
		homeworkView.setAdapter(new ArrayAdapter<Homework>(getActivity(), android.R.layout.simple_list_item_activated_1, unit.getHomeworks()));
		

		ListView labView = (ListView) getView().findViewById(id.materials_fragment_listview_labs);
		
		labView.setAdapter(new ArrayAdapter<Lab>(getActivity(), android.R.layout.simple_list_item_activated_1, unit.getLabs()));
		

		ListView presentationView = (ListView) getView().findViewById(id.materials_fragment_listview_presentations);
		
		ArrayList<Document> presentationData = new ArrayList<Document>();
		for(Presentation pres : unit.getPresentations())
		{
			if(pres.hasTopics())
			{
				for(Topic topic : pres.getTopics())
				{
					presentationData.add(topic);
				}
			}
			else
			{
				presentationData.add(pres);
			}
		}
		
		presentationView.setAdapter(new ArrayAdapter<Document>(getActivity(), android.R.layout.simple_list_item_activated_1, presentationData));
		

		ListView handoutView = (ListView) getView().findViewById(id.materials_fragment_listview_handouts);
		
		handoutView.setAdapter(new ArrayAdapter<Handout>(getActivity(), android.R.layout.simple_list_item_activated_1, unit.getHandouts()));
		


		/*ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				Unit unit = (Unit) adapter.getItemAtPosition(position);
				Log.v("NJCTLUnitSelect", "Downloading unit " + unit.getTitle());

				unit.download();
				// assuming string and if you want to get the value on click of
				// list item
				// do what you intend to do on click of listview row
			}
		});*/

		//setAdapter(new ArrayAdapter<Lab>(getActivity(), android.R.layout.simple_list_item_activated_1, id.materials_fragment_listview_labs,
		//		unit.getLabs()));
	}
}
