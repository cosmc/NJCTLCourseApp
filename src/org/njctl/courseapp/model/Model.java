package org.njctl.courseapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Model implements AsyncJsonResponse
{
	final String NJCTLLOG = "NJCTL";
	protected SubjectRetriever retriever;
	
	public void fetchManifest(SubjectRetriever retrieverObject)
	{
		retriever = retrieverObject;
		
		new RetrieveManifestTask().execute(this);
	}

	@Override
	public void processJson(JSONObject json)
	{
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		try {
			JSONArray results = json.getJSONArray("pages");
			
			for(int i = 0; i < results.length(); i++)
			{
				Subject subject = new Subject(results.getJSONObject(i));
				subjects.add(subject);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
		
		retriever.useSubjects(subjects);
	}
}
