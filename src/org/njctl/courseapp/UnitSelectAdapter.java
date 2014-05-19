package org.njctl.courseapp;

import java.util.List;

import org.njctl.courseapp.model.Unit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UnitSelectAdapter extends ArrayAdapter<Unit>
{
	private Context context;
	private List <Unit> units;
	private int textColorDownloaded = Color.BLACK;
	private int textColorNotDownloaded = Color.BLACK;
	private int backgroundColorDownloaded = Color.GREEN;
	private int backgroundColorNotDownloaded = Color.WHITE;
	
	public UnitSelectAdapter(Context theContext, List<Unit> list)
	{
		super(theContext, R.layout.unitselect_list, list);
		
		context = theContext;
		units = list;
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

        Unit unit = units.get(position);
        
        if(unit != null )
        {
        	text.setText(unit.toString());
        	text.setPadding(30,12,20,12);
        	
        	if(unit.isDownloaded())
        	{
        		text.setTextColor(textColorDownloaded);
                text.setBackgroundColor(backgroundColorDownloaded);
        	}
        	else
        	{
        		text.setTextColor(textColorNotDownloaded);
                text.setBackgroundColor(backgroundColorNotDownloaded);
        	}
        }

        return mView;
    }
}
