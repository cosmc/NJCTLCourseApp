package org.njctl.courseapp;

import java.util.ArrayList;

/**
 * Created by ying on 11/3/13.
 */
public class NJCTLClass {
    private int classId;
    private String className;
    private ArrayList<NJCTLChapter> contents;

    public NJCTLClass(String name, ArrayList<NJCTLChapter> chapters) {
        this.className = name;
        this.contents = chapters;
    }
    
    public String getName() {
    	return className;
    }
    
    public int getId() {
    	return classId;
    }
    
    public ArrayList<NJCTLChapter> getContents() {
    	return contents;
    }
}
