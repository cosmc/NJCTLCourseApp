package org.njctl.courseapp.model.material;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.njctl.courseapp.model.AsyncStringResponse;
import org.njctl.courseapp.model.DocumentState;
import org.njctl.courseapp.model.FileRetrieverTask;
import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.subscribe.DownloadFinishListener;
import org.njctl.courseapp.model.useful.Tripel;

import com.j256.ormlite.field.DatabaseField;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

// Homework, Topic, Handout, Lab

public abstract class Document implements Parcelable, AsyncStringResponse
{
	@DatabaseField
	protected String id;
	
	@DatabaseField
	protected String name;
	
	protected String relativePath; // The path to the document, relative to the
									// app's assets folder.
	
	@DatabaseField
	protected String url;
	
	@DatabaseField
	protected String fileName;
	
	@DatabaseField
	protected Date lastOpened;
	
	@DatabaseField
	protected Date lastUpdated;
	
	protected String MIMEType = "application/pdf";
	
	@DatabaseField
	protected Integer numOpened = 0;
	
	@DatabaseField
	protected String hash = "";
	
	protected DownloadFinishListener<? extends Document> downloadListener;
	
	private DocumentState state = DocumentState.NOTDOWNLOADED;
	
	protected static Context ctx;
	
	public static void setContext(Context context)
	{
		ctx = context;
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
		/*if (extension.equals("pdf")) {
			this.MIMEType = "application/pdf";
		} else if (extension.equals("xlsx")) {
			this.MIMEType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if (extension.equals(".docx")) {
			this.MIMEType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}*/
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
	
	public void download(DownloadFinishListener<? extends Document> listener)
    {
		downloadListener = listener;
		download();
    }
	
	public void download()
	{
		doDownload();
	}
	
	@SuppressWarnings("unchecked")
	protected void doDownload()
	{
		state = DocumentState.DOWNLOADING;
		Tripel<String, String, AsyncStringResponse> request = new Tripel<String, String, AsyncStringResponse>(url, "application/pdf", this);
		new FileRetrieverTask().execute(request);
	}
	
	public void processString(String pdfContent)
	{
		
		//TODO save to file
		
		//TODO check md5 sum.
		String downloadedHash = FileRetrieverTask.getMD5EncryptedString(pdfContent);
		
		if(hash == "" || downloadedHash == hash)
		{
			//Internal storage; http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
			String path = ctx.getFilesDir().getAbsolutePath();
			String fileName = id + "_" + downloadedHash;
			String filePath = path + fileName;
			File file = new File(filePath);
			
			FileOutputStream stream = null;
			
			try {
				stream = new FileOutputStream(file);
			
			    stream.write(pdfContent.getBytes());
			    
			    state = DocumentState.OK;
			    relativePath = fileName;
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			    try {
					if(stream != null)
						stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			state = DocumentState.NOTDOWNLOADED;
			Log.v("NJCTLLOG", "Download hash incorrect.");
		}
		/*
		try {
			FileOutputStream wurst = ctx.openFileOutput("config.txt", Context.MODE_PRIVATE);
			OutputStreamWriter writer = new OutputStreamWriter(null);
		
			writer.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public void deleteFile()
	{
		//TODO delete file.
	}

	// Methods for Parcelable implementation.

	@Override
	public int describeContents()
	{
		return 0;
	}
	/*
	public Document(Parcel in)
	{
		this.id = in.readString();
		this.name = in.readString();
		this.relativePath = in.readString();
		this.fileName = in.readString();
		this.MIMEType = in.readString();
	}*/

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
