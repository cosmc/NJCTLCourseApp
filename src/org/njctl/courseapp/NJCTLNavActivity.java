package org.njctl.courseapp;

import java.util.ArrayList;

public interface NJCTLNavActivity {
	public void showClasses(ArrayList<NJCTLClass> classes);
	public void showChapters(NJCTLClass theClass);
	public String getDocStorageRoot();
}
