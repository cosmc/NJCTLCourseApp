package org.njctl.courseapp;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter<T> extends ArrayAdapter
{
	private Context mContext;
    private int id;
    private List <T>items ;
    private int textColor, backgroundColor;

    public CustomListAdapter(Context context, int textViewResourceId , List<T> list, int textColor, int backgroundColor ) 
    {
        super(context, textViewResourceId, list);           
        mContext = context;
        id = textViewResourceId;
        items = list ;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        if(items.get(position) != null )
        {
            text.setTextColor(textColor);
            text.setText(items.get(position).toString());
            text.setBackgroundColor(backgroundColor); 
            text.setPadding(30,12,20,12);
        }

        return mView;
    }
}