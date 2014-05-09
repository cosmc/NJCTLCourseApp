package org.njctl.courseapp.model.subscribe;

import org.njctl.courseapp.model.Unit;

public interface UnitDownloader
{
	public void downloadUnit(Unit unit, DownloadFinishListener<Unit> listener);
}
