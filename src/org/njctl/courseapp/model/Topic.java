package org.njctl.courseapp.model;

import java.util.Date;

import android.os.Parcel;

public class Topic extends Document
{
	private String hash;
	//private Document doc;
	private Integer size;
	
	public Topic(Parcel in)
	{
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public Integer getSize()
	{
		return size;
	}
}