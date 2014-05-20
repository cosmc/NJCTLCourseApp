package org.njctl.courseapp;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

public class UnitSelectFragment extends ListFragment implements TwoStatesDecider<Unit>
{
	protected Unit currentSelectedUnit;
	TwoStatesAdapter<Unit> listAdapter;
	protected boolean dLBtnVisible; 
	
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
		
		Button btnTranslate = (Button) getActivity().findViewById(R.id.button);
		btnTranslate.setVisibility(View.INVISIBLE);
		
		getListView().setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				Button btnTranslate = (Button) getActivity().findViewById(R.id.button);
				btnTranslate.setVisibility(View.INVISIBLE);
			}
		});

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
					selector.setDrawerIntent(intent);			
					intent.putParcelableArrayListExtra("subscribedClasses", selector.getSubscribedClasses());
					
			        startActivity(intent);
				}
				else //display Download Button if unit isn't downloaded yet.
				{
					//final Animation animTranslate = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_translate);
					Button btnTranslate = (Button) getActivity().findViewById(R.id.button);
					btnTranslate.setVisibility(View.VISIBLE);
					if (dLBtnVisible){
						TranslateAnimation closeAnim=new TranslateAnimation(0.0f, 0.0f, 0.0f, btnTranslate.getHeight());
						closeAnim.setDuration(100);
						btnTranslate.startAnimation(closeAnim);
						dLBtnVisible = false;
					}
					TranslateAnimation openAnim=new TranslateAnimation(0.0f, 0.0f, btnTranslate.getHeight(),
							0.0f);
					openAnim.setDuration(100);
					btnTranslate.startAnimation(openAnim);
					dLBtnVisible = true;
					
					currentSelectedUnit = unit;
					
					Log.v("NJCTLLOG", "Going to display download button for unit " + unit.getTitle());
				}
			}
		});
		
		btnTranslate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(currentSelectedUnit != null)
				{
					UnitSelectActivity action = (UnitSelectActivity) getActivity();
					currentSelectedUnit.download(action);
				}
			}
		});

		//setListAdapter(new ArrayAdapter<Unit>(getActivity(), android.R.layout.simple_list_item_activated_1,
				//theClass.getUnits()));
		listAdapter = new TwoStatesAdapter<Unit>(getActivity(), theClass.getUnits(), this);
        setListAdapter(listAdapter);
	}

	@Override
	public boolean isActive(Unit content) {
		return content.isDownloaded();
	}

	public void update(Unit content)
	{
		// TODO Auto-generated method stub
		//listAdapter.
	}
}
