package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.NJCTLClass;
import org.njctl.courseapp.model.NJCTLDocList;
import org.njctl.courseapp.model.Subject;

public interface NJCTLNavActivity {
	public void showSubjects(ArrayList<Subject> subjects);
	public void showClasses(ArrayList<NJCTLClass> classes);
	public void showChapters(NJCTLClass theClass);
	public void showDocList(NJCTLDocList docList);
}
