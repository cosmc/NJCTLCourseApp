package org.njctl.courseapp.model.subscribe;

public interface DownloadFinishListener<T>
{
	void onDownloaded(T content);
}
