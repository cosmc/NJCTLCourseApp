package org.njctl.courseapp.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.njctl.courseapp.model.useful.Tripel;

import android.os.AsyncTask;
import android.util.Log;

public class FileWriterTask extends AsyncTask<Tripel<String,InputStream,WriteFinishListener>, Void, Void>
{
	protected String absolutePath;
	protected InputStream input;
	protected WriteFinishListener listener;
	
	@Override
	protected Void doInBackground(@SuppressWarnings("unchecked") Tripel<String, InputStream, WriteFinishListener>... request)
	{
		try {
			if(request.length != 1 || !(request[0].x instanceof String) || !(request[0].y instanceof InputStream) || !(request[0].z instanceof WriteFinishListener))
				throw new Exception();
		
			absolutePath = request[0].x;
			input = request[0].y;
			listener = request[0].z;
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("NJCTLLOG", e.toString());
		}
		
		writeContents();
		
		return null;
	}
	
	protected void writeContents()
	{
		try
		{
			BufferedInputStream bis = new BufferedInputStream(input);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(absolutePath)));
			int inByte;
			while((inByte = bis.read()) != -1) bos.write(inByte);
			bis.close();
			bos.close();
			
			listener.onWriteFinish();
		}
		catch (FileNotFoundException e)
		{
			Log.v("NJCTLLOG pdf save filenotfound", Log.getStackTraceString(e));
		}
		catch (IOException e)
		{
			Log.v("NJCTLLOG pdf save ioexception", Log.getStackTraceString(e));
		}
	}

}
