package org.njctl.courseapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.njctl.courseapp.ClassesAdapter;

import java.util.ArrayList;

/**
 * Created by ying on 11/16/13.
 */

public class ClassesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.classes_fragment, container, false);
        ListView listView = (ListView) v.findViewById(R.id.classes_fragment_listview);

        ArrayList<NJCTLClass> mockClasses = new ArrayList<NJCTLClass>();
        mockClasses.add(new NJCTLClass("Math"));
        mockClasses.add(new NJCTLClass("Physics"));
        mockClasses.add(new NJCTLClass("Chemistry"));

        ClassesAdapter adapter = new ClassesAdapter(getActivity(), 0, mockClasses);
        listView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


}


