package org.njctl.courseapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.HashMap;

/**
 * 
 * Created by Colin on 12/12/13.
 *
 */

public class NJCTLDocument implements Parcelable {
	private String _id;
	private String _title;
	private String _relativePath; // The path to the document, relative to the app's assets folder.
	private String _fileName;
	private String _MIMEType;
	
	public NJCTLDocument(String relativePath) {
		this._relativePath = relativePath;
		String[] segments = relativePath.split("/");
		this._fileName = segments[segments.length - 1];
		this._title = _fileName;
		String[] endstuff = _fileName.split("\\.");
		String extension;
		if (endstuff.length > 1) {
			extension = endstuff[endstuff.length - 1];
		} else {
			extension = "";
		}
		
		// Set the MIME type!
		// TODO: Handle more types.
		if (extension.equals("pdf")) {
			this._MIMEType = "application/pdf";
		} else if (extension.equals("xlsx")) {
			this._MIMEType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if (extension.equals(".docx")) {
			this._MIMEType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		
	}
	
	public NJCTLDocument(Parcel in) {
		this._id = in.readString();
		this._title = in.readString();
		this._relativePath = in.readString();
		this._fileName = in.readString();
		this._MIMEType = in.readString();
	}

	public String getId() {
		return this._id;
	}
	
	public String getTitle() {
		return this._title;
	}
	
	public String getRelativePath() {
		return this._relativePath;
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public String getMIMEType() {
		return _MIMEType;
	}
	
    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(this._id);
    	dest.writeString(this._title);
    	dest.writeString(this._relativePath);
    	dest.writeString(this._fileName);
    	dest.writeString(this._MIMEType);
    }
    
    public static final Parcelable.Creator<NJCTLDocument> CREATOR = new Parcelable.Creator<NJCTLDocument>() {
    	public NJCTLDocument createFromParcel(Parcel in) {
    		return new NJCTLDocument(in);
    	}
    	public NJCTLDocument[] newArray(int size) {
    		return new NJCTLDocument[size];
    	}
    };
	
}
