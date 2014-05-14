package org.njctl.courseapp.model.material;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
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
	
	/**
	 * Start downloading the PDF and save the DownloadFinishListener so it can be notified when the task is complete.
	 * @param listener The DownloadFinishListener waiting to be notified upon finished download.
	 */
	public void download(DownloadFinishListener<? super Document> listener)
    {
		downloadListener = listener;
		download();
    }
	
	/**
	 * The method that starts the download. Can be overwritten for eg hash comparing in the Topics.
	 */
	public void download()
	{
		doDownload();
	}
	
	/**
	 * The method that is called in the Documents after the download is finished so lastUpdated times can be updated etc.
	 */
	protected abstract void onDownloadFinish();
	
	/**
	 * The method responsible for telling the FileRetrieverTask what to do and where to download the file from.
	 */
	@SuppressWarnings("unchecked")
	protected void doDownload()
	{
		state = DocumentState.DOWNLOADING;
		Tripel<String, String, AsyncStringResponse> request = new Tripel<String, String, AsyncStringResponse>(url, "application/pdf", this);
		new FileRetrieverTask().execute(request);
	}
	
	/**
	 * The method that calls downloadListener.onDownloaded() with the Document of which the PDF has been downloaded.
	 */
	protected abstract void notifyDownloadListener();
	
	/**
	 * The method dealing with the http response of the download request and saves it to the PDF file.
	 */
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
	
	/**
	 * Tells whether the Document was just created or if it has already existed.
	 * @return True for new Documents, false for already created ones.
	 */
	public boolean wasCreated()
	{
		return created;
	}
	
	/**
	 * Parses a date in String format to a Date.
	 * @param Modified String in format yyyy-MM-dd HH:mm:ss.
	 * @return Parsed Date object.
	 */
	protected Date convertDate(String modified)
	{
		try
		{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			return df.parse(modified);
		}
		catch (ParseException e)
		{
			Log.v("NJCTLLOG DOC DATE", Log.getStackTraceString(e));
			return null;
		}
	}
	
	/**
	 * Sets basic properties based on the JSON such as name, id, lastUpdatedNew, uri.
	 * @param json The JSON Object of the Document, in the NJCTL format.
	 */
	protected void setProperties(JSONObject json)
	{
		try{
			Date newLastUpdated = convertDate(json.getString("post_modified"));
			
			if(lastUpdatedNew == null || newLastUpdated.after(lastUpdatedNew))
			{
				name = json.getString("post_title");
				id = json.getString("ID");
				lastUpdatedNew = newLastUpdated;
				
				if(json.has("pdf_uri"))
				{
					url = json.getString("pdf_uri");
				}
				else
				{
					Log.w("NJCTLLOG", "                pdf_uri not found for doc " + name);
				}
			}
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", e.toString());
		}
	}
	
    // Methods for Parcelable implementation.
    public int describeContents() {
    	return 0;
    }
    
    /**
     * Copies the basic information from a Document over to the current instance.
     * @param in
     */
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
