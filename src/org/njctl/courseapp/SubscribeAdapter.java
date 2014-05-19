package org.njctl.courseapp;

import java.util.List;

import org.njctl.courseapp.model.Class;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SubscribeAdapter extends ArrayAdapter<Class>
{
	private Context context;
	private List <Class> classes;
	private int textColorSubscribed = Color.BLACK;
	private int textColorNotSubscribed = Color.BLACK;
	private int backgroundColorSubscribed = Color.GREEN;
	private int backgroundColorNotSubscribed = Color.WHITE;
	
	public SubscribeAdapter(Context theContext, List<Class> list)
	{
		super(theContext, R.layout.subscribe_list, list);
		
		context = theContext;
		classes = list;
	}
	
	@Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(R.layout.unitselect_list, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        Class theClass = classes.get(position);
        
        if(theClass != null )
        {
        	text.setText(theClass.toString());
        	text.setPadding(30,12,20,12);
        	
        	if(theClass.isSubscribed())
        	{
        		text.setTextColor(textColorSubscribed);
                text.setBackgroundColor(backgroundColorSubscribed);
        	}
        	else
        	{
        		text.setTextColor(textColorNotSubscribed);
                text.setBackgroundColor(backgroundColorNotSubscribed);
        	}
        }

        return mView;
    }
}
