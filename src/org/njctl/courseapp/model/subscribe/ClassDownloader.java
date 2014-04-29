package org.njctl.courseapp.model.subscribe;

import org.njctl.courseapp.model.Class;

public interface ClassDownloader
{
	public void downloadClass(Class theClass, DownloadFinishListener<Class> listener);
}
