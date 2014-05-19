package org.njctl.courseapp.model;

public interface AsyncResponse<T>
{
	void processResult(T output);
}