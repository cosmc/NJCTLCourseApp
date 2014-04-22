package org.njctl.courseapp.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Document implements Parcelable
{
	private String id;
	private String name;
	private String relativePath; // The path to the document, relative to the
									// app's assets folder.
	private String url;
	private String fileName;
	private String MIMEType;
	private Date lastOpened;
	private Date lastUpdated;
	private Integer numOpened = 0;
	
	private DocumentState state;

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

	public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {
		public Document createFromParcel(Parcel in)
		{
			return new Document(in);
		}

		public Document[] newArray(int size)
		{
			return new Document[size];
		}
	};
}
