package org.njctl.courseapp.model;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.njctl.courseapp.model.useful.Tripel;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * Tripel containing URL, request content type, and listener.
 */
public abstract class FileRetrieverTask<T> extends AsyncTask<Tripel<String,String,AsyncResponse<T>>, Void, T>
{
	final String NJCTLLOG = "NJCTL";
	protected AsyncResponse<T> delegate = null;
	protected String url, contentType = "text/plain";

	@SuppressWarnings("unchecked")
	protected T doInBackground(Tripel<String,String,AsyncResponse<T>>... request)
	{
		try {
			if(request.length != 1 || !(request[0].x instanceof String) || !(request[0].y instanceof String) || !(request[0].z instanceof AsyncResponse))
				throw new Exception();
		
			url = request[0].x;
			contentType = request[0].y;
			delegate = request[0].z;
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.w(NJCTLLOG, e.toString());
		}
		
		return getContents();
	}

	protected void onPostExecute(T result)
	{
		delegate.processResult(result);
	}
	
	abstract T convertContents(InputStream inputStream);

	protected T getContents()
	{
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new BasicHttpParams());

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Content-type", contentType);
		//httpget.setHeader("Content-Encoding", "gzip, deflate");
		//httpget.setHeader("Content-type", "application/json");

		InputStream inputStream = null;

		try {
			Log.v(NJCTLLOG, "executing http query to " + url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			
			return convertContents(inputStream);
			// json is UTF-8 by default
			/*
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String result = sb.toString();

			Log.v(NJCTLLOG, "length:" + result.length());
			return result;*/

		} catch (Exception e) {
			// Oops
			Log.w(NJCTLLOG, e.toString());
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
			}
		}
		return convertContents(null);
	}
	
	public static String getMD5EncryptedString(String encTarget)
	{
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }
}