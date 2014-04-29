package org.njctl.courseapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.useful.Tripel;

import android.util.Log;

public class Model implements AsyncStringResponse
{
	final String NJCTLLOG = "NJCTLLOG";
	private String jsonUrl = "http://www.sandbox-njctl.org/courses.json";
	
	protected SubjectRetriever retriever;
	
	@SuppressWarnings("unchecked")
	public void fetchManifest(SubjectRetriever retrieverObject)
	{
		retriever = retrieverObject;
		
		Tripel<String, String, AsyncStringResponse> request = new Tripel<String, String, AsyncStringResponse>(jsonUrl, "application/json", this);
		new FileRetrieverTask().execute(request);
	}

	@Override
	public void processString(String jsonString)
	{
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		try {
			JSONObject json = new JSONObject(jsonString);
			
			
			JSONArray results = json.getJSONArray("pages");

			Log.v(NJCTLLOG, "Looping through " + Integer.toString(results.length()) + " subjects...");
			
			for(int i = 0; i < results.length(); i++)
			{
				Subject subject = Subject.newInstance(results.getJSONObject(i));
				
				if(subject != null)
				{
					subjects.add(subject);
				}
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
