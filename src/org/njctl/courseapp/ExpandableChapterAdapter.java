package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.NJCTLDocList;
 
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.util.Log;

/**
 * 
 * Created by Colin on 12/9/13.
 *
 */

public class ExpandableChapterAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private ArrayList<Unit> units;
    
 
    public ExpandableChapterAdapter(Context context, ArrayList<Unit> chapters) {
        this._context = context;
        this.units = chapters;
    }
 
    @Override
    public NJCTLDocList getChild(int groupPosition, int childPosition) {
    	return units.get(groupPosition).getContents().get(childPosition);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition).getTitle();
 
        if (convertView == null) {
            LayoutInflater infl = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infl.inflate(R.layout.chapter_list_child, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.chapter_list_child_item);
 
        txtListChild.setText("  "+childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return units.get(groupPosition).getContents().size();
    }
 
    @Override
    public Unit getGroup(int groupPosition) {
        return this.units.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.units.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition).getTitle();
        if (convertView == null) {
            LayoutInflater infl = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infl.inflate(R.layout.chapter_list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.chapter_list_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}