package org.njctl.courseapp.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class RetrieveManifestTask extends AsyncTask<AsyncJsonResponse, Void, JSONObject>
{
	final String NJCTLLOG = "NJCTL";
	private String jsonUrl = "http://www.sandbox-njctl.org/courses.json";
	protected AsyncJsonResponse delegate = null;

	protected JSONObject doInBackground(AsyncJsonResponse... dele)
	{
		try {
			if(dele.length != 1)
				throw new Exception();
		
		delegate = dele[0];
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.w(NJCTLLOG, e.toString());
		}
		
		try {
			JSONObject json = new JSONObject(getHTML());

			return json;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(NJCTLLOG, e.toString());
		}

		return null;
	}

	protected void onPostExecute(JSONObject result)
	{
		delegate.processJson(result);
	}

	protected String getHTML()
	{
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new BasicHttpParams());

		HttpPost httppost = new HttpPost(jsonUrl);
		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;

		try {
			Log.v(NJCTLLOG, "executing http query..");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String result = sb.toString();

			Log.v(NJCTLLOG, "length:" + result.length());
			return result;

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
		return "";
	}
}