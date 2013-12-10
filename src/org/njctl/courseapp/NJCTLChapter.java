package org.njctl.courseapp;

/**
 * Created by ying on 11/3/13.
 */
public class NJCTLChapter {
    private int chapterId;
    private String chapterTitle;
    private String lectureDocumentId;
    private String homeworkDocumentId;

    public NJCTLChapter(String title) {
        chapterTitle = title;
    }
    
    public String getTitle() {
    	return chapterTitle;
    }
    
    public String getLecture() {
    	return "";
    }

    public String getHomework() {
    	return "";
    }

}
