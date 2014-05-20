package org.njctl.courseapp;

import java.util.List;

import org.njctl.courseapp.model.Class;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SubscribeFragment extends ListFragment implements TwoStatesDecider<Class>
{
	protected TwoStatesAdapter<Class> listAdapter;
	
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

		final SubscribeActivity selector = (SubscribeActivity) getActivity();
		List<Class> classes = selector.getClasses();
		
		listAdapter = new TwoStatesAdapter<Class>(getActivity(), classes, this);
		setListAdapter(listAdapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				//TODO Show subscription button, do subscription stuff.
				Class theClass = (Class) adapter.getItemAtPosition(position);
				theClass.subscribe();
				listAdapter.update(theClass);
				selector.updateSubscriptions(theClass);
			}
		});

		//CustomListAdapter<Class> listAdapter = new CustomListAdapter<Class>(this , R.layout.drawer_list , subscribedClasses, Color.WHITE, Color.GRAY);
        //mDrawerList.setAdapter(listAdapter);
		
		
		
		//setListAdapter(new ArrayAdapter<Class>(getActivity(), android.R.layout.simple_list_item_activated_1,
		//		classes));
	}

	@Override
	public boolean isActive(Class content)
	{
		return content.isSubscribed();
	}
}
