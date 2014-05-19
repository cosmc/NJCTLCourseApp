package org.njctl.courseapp;

public interface TwoStatesDecider<T>
{
	public boolean isActive(T content);
}
