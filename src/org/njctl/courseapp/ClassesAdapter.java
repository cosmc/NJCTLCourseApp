package org.njctl.courseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Subject;

/**
 * Created by ying on 11/16/13.
 */
class ClassesAdapter extends ArrayAdapter {

    private ArrayList<Class> classes;

    class ViewHolder {
        TextView classNameTextView;
        TextView progressTextView;
        TextView unitsTextView;
    }

    public ClassesAdapter(Context context, int resource, ArrayList<Class> classes) {
        super(context, resource, classes);
        this.classes = classes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.class_row_viewholder, parent, false);
            holder = new ViewHolder();
            holder.classNameTextView = (TextView) convertView.findViewById(R.id.class_row_viewholder_class_name_textview);
            holder.progressTextView = (TextView) convertView.findViewById(R.id.class_row_viewholder_progress_label_textview);
            holder.unitsTextView = (TextView) convertView.findViewById(R.id.class_row_viewholder_units_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Class aClass = classes.get(position);
        holder.classNameTextView.setText(aClass.getTitle());
        holder.unitsTextView.setText("");
        holder.progressTextView.setText("In Progress");
        return convertView;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    
}

