package org.njctl.courseapp.model;

public interface DownloadFinishListener<T>
{
	void onDownloaded(T content);
}
