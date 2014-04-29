package org.njctl.courseapp.model.material;

import java.util.Date;

import org.njctl.courseapp.model.DocumentState;

import android.os.Parcel;
import android.os.Parcelable;

// Homework, Topic, Handout, Lab

public abstract class Document implements Parcelable
{
	protected String id;
	protected String name;
	protected String relativePath; // The path to the document, relative to the
									// app's assets folder.
	protected String url;
	protected String fileName;
	protected String MIMEType;
	protected Date lastOpened;
	protected Date lastUpdated;
	protected Integer numOpened = 0;
	
	private DocumentState state;
	
	public Document()
	{
		
	}

	public Document(String name)
	{
		this.name = name;
	}

	public Document(String name, String relativePath)
	{
		this.name = name;
		this.setPath(relativePath);
	}

	public boolean isDownloaded()
	{
		return this.relativePath != "";
	}

	public void setPath(String relativePath)
	{
		this.relativePath = relativePath;
		String[] segments = relativePath.split("/");
		this.fileName = segments[segments.length - 1];
		// this.name = fileName;
		String[] endstuff = fileName.split("\\.");
		String extension;
		if (endstuff.length > 1) {
			extension = endstuff[endstuff.length - 1];
		} else {
			extension = "";
		}

		// Set the MIME type!
		// TODO: Handle more types.
		if (extension.equals("pdf")) {
			this.MIMEType = "application/pdf";
		} else if (extension.equals("xlsx")) {
			this.MIMEType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if (extension.equals(".docx")) {
			this.MIMEType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String getRelativePath()
	{
		return this.relativePath;
	}

	public String getRelativePathForOpening()
	{
		numOpened++;
		lastOpened = new Date();
		
		return this.relativePath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getMIMEType()
	{
		return MIMEType;
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

	// Methods for Parcelable implementation.

	@Override
	public int describeContents()
	{
		return 0;
	}
	
	public Document(Parcel in)
	{
		this.id = in.readString();
		this.name = in.readString();
		this.relativePath = in.readString();
		this.fileName = in.readString();
		this.MIMEType = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.relativePath);
		dest.writeString(this.fileName);
		dest.writeString(this.MIMEType);
	}
/*
	public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {
		public Document createFromParcel(Parcel in)
		{
			return new Document(in);
		}

		public Document[] newArray(int size)
		{
			return new Document[size];
		}
	};*/
}