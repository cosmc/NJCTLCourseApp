package org.njctl.courseapp.model.subscribe;

import org.njctl.courseapp.model.DownloadFinishListener;

public class Downloader implements ClassDownloader
{
	protected DownloadFinishListener listener;
	
	public void downloadClass(DownloadFinishListener theListener)
	{
		listener = theListener;
		
		
		
		// TODO do stuff.
	}

}
