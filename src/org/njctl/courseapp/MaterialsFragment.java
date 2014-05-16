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
		
		ListView docView = (ListView) getView().findViewById(id.materials_fragment_listview_documents);
		
		ArrayList<Document> data = new ArrayList<Document>();
		data.addAll(unit.getHomeworks());
		data.addAll(unit.getLabs());
		
		for(Presentation pres : unit.getPresentations())
		{
			if(pres.hasTopics())
			{
				for(Topic topic : pres.getTopics())
				{
					data.add(topic);
				}
			}
			else
			{
				data.add(pres);
			}
		}
		data.addAll(unit.getHandouts());
		
		docView.setAdapter(new ArrayAdapter<Document>(getActivity(), android.R.layout.simple_list_item_activated_1, data));
		
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
