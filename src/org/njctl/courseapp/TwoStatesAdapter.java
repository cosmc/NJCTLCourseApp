package org.njctl.courseapp;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwoStatesAdapter<T> extends ArrayAdapter<T>
{
	private Context context;
	private List <T> contents;
	private TwoStatesDecider<T> decisionMaker;
	
	private int textColorActive = Color.BLACK;
	private int textColorInactive = Color.BLACK;
	private int backgroundColorActive = Color.WHITE;
	private int backgroundColorInactive = Color.LTGRAY;
	
	public TwoStatesAdapter(Context theContext, List<T> list, TwoStatesDecider<T> decider)
	{
		super(theContext, R.layout.two_states_list, list);
		
		context = theContext;
		contents = list;
		decisionMaker = decider;
	}
	
	public void update(T element)
	{
		Integer index = contents.indexOf(element);
		
		if(index != -1)
		{
			this.remove(element);
			this.insert(element, index);
		}
	}
	/*
	private void updateView(int index){
	    View v = getChildAt(index - 
	        getFirstVisiblePosition());

	    if(v == null)
	       return;

	    TextView someText = (TextView) v.findViewById(R.id.sometextview);
	    someText.setText("Hi! I updated you manually!");
	}*/
	
	@Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(R.layout.two_states_list, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        T content = contents.get(position);
        
        if(content != null )
        {
        	text.setText(content.toString());
        	//text.setPadding(30,12,20,12);
        	
        	
        	if(decisionMaker.isActive(content))
        	{
        		text.setTextColor(textColorActive);
                text.setBackgroundColor(backgroundColorActive);
        	}
        	else
        	{
        		text.setTextColor(textColorInactive);
                text.setBackgroundColor(backgroundColorInactive);
        	}
        }

        return mView;
    }
}
