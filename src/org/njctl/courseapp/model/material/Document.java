package org.njctl.courseapp.model.material;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.njctl.courseapp.model.AsyncStringResponse;
import org.njctl.courseapp.model.DocumentState;
import org.njctl.courseapp.model.FileRetrieverTask;
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
	@DatabaseField(id = true)
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
	
	@DatabaseField
	protected Date lastUpdatedNew;
	
	protected String MIMEType = "application/pdf";
	
	@DatabaseField
	protected Integer numOpened = 0;
	
	/*@DatabaseField
	protected String hash = "";*/
	
	protected DownloadFinishListener<? super Document> downloadListener;
	
	protected DocumentState state = DocumentState.NOTDOWNLOADED;
	
	protected boolean created = false;
	
	protected static Context ctx;
	
	public static void setContext(Context context)
	{
		ctx = context;
	}
	
	public boolean isDownloaded()
	{
		Log.v("NJCTLLOG", "Document state: " + state);
		return state == DocumentState.OK;
	}
	
	public boolean isDownloading()
	{
		return state == DocumentState.DOWNLOADING;
	}

	public void setPath(String relativePath)
	{
		this.relativePath = relativePath;
		String[] segments = relativePath.split("/");
		this.fileName = segments[segments.length - 1];
		// this.name = fileName;
		/*String[] endstuff = fileName.split("\\.");
		String extension;
		if (endstuff.length > 1) {
			extension = endstuff[endstuff.length - 1];
		} else {
			extension = "";
		}*/

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
	
	public void download(DownloadFinishListener<? super Document> listener)
    {
		downloadListener = listener;
		download();
    }
	
	public void download()
	{
		
		doDownload();
	}
	
	protected abstract void onDownloadFinish();
	
	@SuppressWarnings("unchecked")
	protected void doDownload()
	{
		state = DocumentState.DOWNLOADING;
		Tripel<String, String, AsyncStringResponse> request = new Tripel<String, String, AsyncStringResponse>(url, "application/pdf", this);
		new FileRetrieverTask().execute(request);
	}
	
	protected abstract void notifyDownloadListener();
	
	public void processString(String pdfContent)
	{
		//check md5 sum in a future release.
		//String downloadedHash = FileRetrieverTask.getMD5EncryptedString(pdfContent);
		
		//Internal storage; http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
		String path = ctx.getFilesDir().getAbsolutePath();
		fileName = id + ".pdf";
		relativePath = path + fileName;
		File file = new File(relativePath);
		
		FileOutputStream stream = null;
		
		try
		{
			stream = new FileOutputStream(file);
		
		    stream.write(pdfContent.getBytes());
		    state = DocumentState.OK;
		    stream.close();
		    
		    onDownloadFinish();
		    notifyDownloadListener();
		    
		}
		catch (FileNotFoundException e)
		{
			Log.v("NJCTLLOG pdf save filenotfound", Log.getStackTraceString(e));
		}
		catch (IOException e)
		{
			Log.v("NJCTLLOG pdf save ioexception", Log.getStackTraceString(e));
		}
		finally
		{
		    try
		    {
				if(stream != null)
					stream.close();
			}
		    catch (IOException e)
		    {
				// TODO Auto-generated catch block
				Log.v("NJCTLLOG pdf save", Log.getStackTraceString(e));
				e.printStackTrace();
			}
		}
	}
	
	public void deleteFile()
	{
		//TODO delete file.
	}
	
	public boolean wasCreated()
	{
		return created;
	}
	
    // Methods for Parcelable implementation.
    public int describeContents() {
    	return 0;
    }
    
    protected void setByDocument(Document in)
    {
		name = in.name;
		id = in.id;
		lastOpened = in.lastOpened;
		lastUpdated = in.lastUpdated;
		//hash = in.hash;
		MIMEType = in.MIMEType;
		numOpened = in.numOpened;
		fileName = in.fileName;
		relativePath = in.relativePath;
		url = in.url;
    }
    
    public void writeToParcel(Parcel dest, int flags)
    {
    	dest.writeString(id);
    }
}
