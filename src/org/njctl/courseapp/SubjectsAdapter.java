package org.njctl.courseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

import org.njctl.courseapp.model.Subject;

/**
 * Created by ying on 11/16/13.
 */
class SubjectsAdapter extends ArrayAdapter {

    private ArrayList<Subject> contents;

    class ViewHolder {
        TextView nameTextView;
        ImageView colorBarImageView;
    }

    public SubjectsAdapter(Context context, int resource, ArrayList<Subject> contents) {
        super(context, resource, contents);
        this.contents = contents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.subject_row_viewholder, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.subject_row_viewholder_subject_name_textview);
            holder.colorBarImageView = (ImageView) convertView.findViewById(R.id.subject_row_viewholder_colorbar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Subject subject = contents.get(position);
        holder.nameTextView.setText(subject.getTitle());
        holder.colorBarImageView.setImageResource(subject.getBottomColorBarResource()); // TODO Deal with image sources for color bars.
        return convertView;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    
}

