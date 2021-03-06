package org.njctl.courseapp.model.material;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.AsyncResponse;
import org.njctl.courseapp.model.FileWriterTask;
import org.njctl.courseapp.model.InputStreamRetrieverTask;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.WriteFinishListener;
import org.njctl.courseapp.model.useful.Tripel;

import com.j256.ormlite.field.DatabaseField;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public abstract class Document implements Parcelable, AsyncResponse<InputStream>, WriteFinishListener
{
	/**
	 * The Document's ID. Is not auto-generated, but fetched from
	 * the NJCTL Post's ID so it can be traced back and compared easily.
	 */
	@DatabaseField(id = true)
	protected String id;
	
	/**
	 * The Document's displayable text.
	 */
	@DatabaseField
	protected String title;
	
	/**
	 * The path to the PDF.
	 */
	@DatabaseField
	protected String absolutePath;
	
	/**
	 * The HTTP URI pointing to the location of the PDF on the NJCTL Server.
	 */
	@DatabaseField
	protected String url;
	
	/**
	 * The file name of the PDF.
	 */
	@DatabaseField
	protected String fileName;
	
	/**
	 * The last time the PDF has been opened, or getRelativePathForOpening has been called.
	 */
	@DatabaseField
	protected Date lastOpened;
	
	/**
	 * The downloaded PDF's last modified date if exists, otherwise the last time the PDF has been downloaded.
	 */
	@DatabaseField
	protected Date lastUpdated;
	
	/**
	 * The last modified date of the server's Document that the app knows of.
	 * Will be the new lastUpdated date if the user chooses to download. 
	 */
	@DatabaseField
	protected Date lastUpdatedNew;
	
	/**
	 * The MIME type of the Document, usually application/pdf.
	 */
	protected String MIMEType = "application/pdf";
	
	/**
	 * Number of times the PDF has been opened, or getAbsolutePathForOpening has been called.
	 */
	@DatabaseField
	protected Integer numOpened = 0;
	
	@DatabaseField
    protected Integer order;
	
	/*@DatabaseField
	protected String hash = "";*/
	
	protected DownloadFinishListener<? super Document> downloadListener;
	
	/**
	 * The state of the PDF. Can be NOTDOWNLOADED, DOWNLOADING, OK or OUTDATED.
	 */
	@DatabaseField
	protected DocumentState state = DocumentState.NOTDOWNLOADED;
	
	/**
	 * Stores whether the Document Object has just been created.
	 */
	protected boolean created = false;
	
	/**
	 * The context of the app. Required for using the app's path for storing the PDFs.
	 */
	protected static Context ctx;
	
	/**
	 * Sets the context of the Document Class so the path for storing and reading the PDFs can be accessed.
	 * @param context
	 */
	public static void setContext(Context context)
	{
		ctx = context;
	}
	
	/**
	 * Check to see if a Document's PDF has been downloaded.
	 * @return True if the file has been downloaded (may be outdated), false if not.
	 */
	public boolean isDownloaded()
	{
		return state == DocumentState.OK || state == DocumentState.OUTDATED;
	}
	
	/**
	 * Check to see if a PDF is being downloaded.
	 * @return True if the file is being downloaded, false if not.
	 */
	public boolean isDownloading()
	{
		return state == DocumentState.DOWNLOADING;
	}

	/**
	 * Returns the ID of the document, matching the ID on the NJCTL server.
	 * @return The NJCTL Document ID as String.
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * Get a displayable name of the Document as String.
	 * @return The UTF-8 encoded name of the Document.
	 */
	public String getName()
	{
		return this.title;
	}
/*
	public String getRelativePath()
	{
		return this.relativePath;
	}
*/
	/**
	 * Returns the path to the PDF and stores the information that the PDF has been accessed.
	 * @return The path to the PDF.
	 */
	public String getAbsolutePathForOpening()
	{
		numOpened++;
		lastOpened = new Date();
		
		return this.absolutePath;
	}

	/**
	 * Returns the PDF's name including the file's extension.
	 * @return
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Get the Document's MIME type, usually application/pdf.
	 * @return The MIME type as String.
	 */
	public String getMIMEType()
	{
		return MIMEType;
	}
	
	/**
	 * Number of times the Document's PDF has been opened, or getAbsolutePathForOpening has been called.
	 * @return Number of openings as Integer.
	 */
	public Integer getNumOpened()
	{
		return numOpened;
	}
	
	/**
	 * The last time the Document's PDF has been opened, or getAbsolutePathForOpening has been called.
	 * @return Date the last opening happened, in Date format.
	 */
	public Date getLastOpened()
	{
		return lastOpened;
	}
	
	/**
	 * The downloaded PDF's last modified date if exists, otherwise the last time the PDF has been downloaded.
	 * @return The last updated time in Date format.
	 */
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
	 * The method that starts the download. Can be overwritten for e.g. hash comparing in the Topics.
	 */
	public void download()
	{
		if(state != DocumentState.OK)
		{
			doDownload();
		}
		else
			notifyDownloadListener();
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
		if(state != DocumentState.OK)
		{
			state = DocumentState.DOWNLOADING;
			Tripel<String, String, AsyncResponse<InputStream>> request = new Tripel<String, String, AsyncResponse<InputStream>>(url, "application/pdf", this);
			Log.v("NJCTLDOWNLOAD", "Starting pdf download for " + title);
			new InputStreamRetrieverTask().execute(request);
		}
		else
		{
			Log.w("NJCTLDOWNLOAD", "pdf download for " + title + " skipped as Document is already downloaded and up to date.");
		}
	}
	
	/**
	 * The method that calls downloadListener.onDownloaded() with the Document of which the PDF has been downloaded.
	 */
	protected abstract void notifyDownloadListener();
	
	/**
	 * The method dealing with the http response of the download request and saves it to the PDF file.
	 */
	@SuppressWarnings("unchecked")
	public void processResult(InputStream pdfStream)
	{
		//check md5 sum in a future release.
		//String downloadedHash = FileRetrieverTask.getMD5EncryptedString(pdfContent);

		String path = ctx.getFilesDir().getAbsolutePath();
		fileName = id + ".pdf";
		absolutePath = path + fileName;
		
		Tripel<String, InputStream, WriteFinishListener> request = new Tripel<String, InputStream, WriteFinishListener>(absolutePath, pdfStream, this);
		new FileWriterTask().execute(request);
	}
	
	public void onWriteFinish()
	{
		state = DocumentState.OK;
		onDownloadFinish();
	    notifyDownloadListener();
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
				title = json.getString("post_title");
				id = json.getString("ID");
				lastUpdatedNew = newLastUpdated;
				checkOutdated();
				
				if(json.has("pdf_uri"))
				{
					url = json.getString("pdf_uri");
				}
				else
				{
					Log.w("NJCTLLOG", "                pdf_uri not found for doc " + title);
				}
			}
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", e.toString());
		}
	}
	
	/**
	 * Checks if the downloaded PDF (if applicable) is up to date.
	 */
	protected void checkOutdated()
    {
    	if(state == DocumentState.OK && lastUpdated.before(lastUpdatedNew))
    	{
    		state = DocumentState.OUTDATED;
    		download();
    	}
    }
	
	public String toString()
	{
		return getName();
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
		title = in.title;
		id = in.id;
		lastOpened = in.lastOpened;
		lastUpdated = in.lastUpdated;
		//hash = in.hash;
		MIMEType = in.MIMEType;
		numOpened = in.numOpened;
		fileName = in.fileName;
		absolutePath = in.absolutePath;
		url = in.url;
    }
    
    /**
     * Saves the Document's ID in the Parcel.
     */
    public void writeToParcel(Parcel dest, int flags)
    {
    	dest.writeString(id);
    }
}
