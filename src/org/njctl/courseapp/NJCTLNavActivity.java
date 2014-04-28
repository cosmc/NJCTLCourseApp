package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.NJCTLDocList;
import org.njctl.courseapp.model.Subject;

public interface NJCTLNavActivity {
	public void showSubjects(ArrayList<Subject> subjects);
	public void showClasses(Subject subject);
	public void showChapters(Class theClass);
	public void showUnits(Class theClass);
	public void showTopics(Unit theUnit);
	public void showDocList(NJCTLDocList docList);
}
