package org.njctl.courseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;

/**
 * Created by ying on 11/16/13.
 */
class MyClassesAdapter extends ArrayAdapter {

    private ArrayList<Class> classes;

    class ViewHolder {
        TextView classNameTextView;
        ImageView colorBarImageView;
    }

    public MyClassesAdapter(Context context, int resource, ArrayList<Class> classes) {
        super(context, resource, classes);
        this.classes = classes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.my_classes_row_viewholder, parent, false);
            holder = new ViewHolder();
            holder.classNameTextView = (TextView) convertView.findViewById(R.id.my_classes_row_viewholder_class_name_textview);
            holder.colorBarImageView = (ImageView) convertView.findViewById(R.id.my_classes_row_viewholder_colorbar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Class aClass = classes.get(position);
        holder.classNameTextView.setText(aClass.getTitle());
        holder.colorBarImageView.setImageResource(aClass.getSubject().getColorBarResource()); // TODO Deal with image sources for color bars.
        return convertView;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    
}

