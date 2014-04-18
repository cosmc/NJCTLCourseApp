package org.njctl.courseapp;

import java.util.ArrayList;

import org.njctl.courseapp.model.NJCTLClass;
import org.njctl.courseapp.model.NJCTLDocList;

public interface NJCTLNavActivity {
	public void showClasses(ArrayList<NJCTLClass> classes);
	public void showChapters(NJCTLClass theClass);
	public void showDocList(NJCTLDocList docList);
}
