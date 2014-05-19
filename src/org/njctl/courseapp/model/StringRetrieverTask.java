package org.njctl.courseapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.util.Log;

/**
 * 
 * Tripel containing URL, request content type, and listener.
 */
public class StringRetrieverTask extends FileRetrieverTask<String>
{

	@Override
	String convertContents(InputStream inputStream)
	{
		try {
			BufferedReader reader;

			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String result = sb.toString();

			Log.v(NJCTLLOG, "length:" + result.length());
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}