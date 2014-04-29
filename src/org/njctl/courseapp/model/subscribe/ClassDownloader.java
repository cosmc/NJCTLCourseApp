package org.njctl.courseapp.model.subscribe;

import org.njctl.courseapp.model.DownloadFinishListener;

public interface ClassDownloader
{
	public void downloadClass(DownloadFinishListener listener);
}
