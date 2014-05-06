package org.njctl.courseapp.model.subscribe;

import java.util.ArrayList;

import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Presentation;
import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.material.Handout;
import org.njctl.courseapp.model.material.Homework;
import org.njctl.courseapp.model.material.Lab;

public class Downloader implements ClassDownloader, UnitDownloader, DownloadFinishListener<Unit>
{
	protected DownloadFinishListener<Class> listener;
	protected Integer downloadingUnits = 0;
	
	public void downloadClass(Class theClass, DownloadFinishListener<Class> theListener)
	{
		listener = theListener;
		
		ArrayList<Unit> units = theClass.getUnits();
		
		for(int i = 0; i < units.size(); i++)
		{
			Unit unit = units.get(i);
			
			downloadUnit(unit, this);
		}
		
		// TODO do stuff.
	}
	
	public void downloadUnit(Unit unit, DownloadFinishListener<Unit> listener)
	{
		downloadingUnits++;
		
		ArrayList<Handout> handouts = unit.getHandouts();
		ArrayList<Lab> labs = unit.getLabs();
		ArrayList<Homework> homeworks = unit.getHomeworks();
		ArrayList<Presentation> presentations = unit.getPresentations();
		
		//for(int i = 0; i < )
	}

	@Override
	public void onDownloaded(Unit content)
	{
		// TODO Auto-generated method stub
		
	}
}