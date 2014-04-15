package org.njctl.courseapp.model;

import java.util.Date;

public class Topic
{
	private Date lastOpened;
	private Date lastUpdated;
	private String url;
	private String fileAddress;
	private Integer numOpened;
	private String hash;
	private NJCTLDocument doc;
	private String name;
	private Integer size;
	
	public String getFileAddressForOpening()
	{
		numOpened++;
		lastOpened = new Date();
		return fileAddress;
	}
	
	public NJCTLDocument getDocument()
	{
		return doc;
	}
	
	public Integer getNumOpened()
	{
		return numOpened;
	}
	
	public Date getLastOpened()
	{
		return lastOpened;
	}
	
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Integer getSize()
	{
		return size;
	}
}