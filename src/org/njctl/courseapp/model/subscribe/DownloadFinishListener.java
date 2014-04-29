package org.njctl.courseapp.model.subscribe;

public interface DownloadFinishListener<T>
{
	void onClassDownloaded(T content);
}
