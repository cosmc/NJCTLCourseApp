package org.njctl.courseapp;

import org.njctl.courseapp.model.Model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements ListView.OnItemClickListener{
	private Model model;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
	public DrawerItemClickListener(Model appModel, DrawerLayout drawerLayout, ListView drawerList){
		model =  appModel;
		mDrawerLayout = drawerLayout;	
		mDrawerList = drawerList;
	}
	@Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }


/** Swaps fragments in the main content view */
public void selectItem(int position) {
	//If Class is selected, display its units
	if (model.getClassesSubscribed().size() == 0){
		Log.d("drawerListener", "subscribed classes list is empty");
		return;
	}
	Log.d("flagSelectItem", "about to make units frag");
	UnitsFragment unitsFragment = new UnitsFragment();
	

    // Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
                   .replace(R.id.container, unitsFragment)
                   .commit();
    

    // Highlight the selected item, update the title, and close the drawer
    mDrawerLayout.closeDrawer(mDrawerList);
}
private FragmentManager getSupportFragmentManager() {
	// TODO Auto-generated method stub
	return null;
}
}
