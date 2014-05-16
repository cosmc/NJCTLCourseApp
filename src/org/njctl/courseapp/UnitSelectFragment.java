package org.njctl.courseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Unit;

public class UnitSelectFragment extends ListFragment
{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.unitselect_fragment, container, false);
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UnitSelectActivity selector = (UnitSelectActivity) getActivity();
        Class theClass = selector.getNJCTLClass();

        
        setListAdapter(new ArrayAdapter<Unit>(getActivity(),
                android.R.layout.simple_list_item_activated_1, theClass.getUnits()));
    }
}
