package org.njctl.courseapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.useful.Tripel;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Context;
import android.util.Log;

public class Model implements AsyncStringResponse
{
	final String NJCTLLOG = "NJCTLLOG";
	private String jsonUrl = "http://sandbox-njctl.org/courses.json?for-app=1";
	protected Context context;
	protected DatabaseHelper dbHelper;
	protected boolean dbFilled = false;
	
	protected SubjectRetriever retriever;
	
	public Model(Context ctx)
	{
		dbHelper = OpenHelperManager.getHelper(ctx, DatabaseHelper.class);
		
		RuntimeExceptionDao<Subject, String> dao = dbHelper.getRuntimeExceptionDao(Subject.class);
		Subject.setDao(dao);
	}
	
	@SuppressWarnings("unchecked")
	public void fetchManifest(SubjectRetriever retrieverObject)
	{
		retriever = retrieverObject;
		
		if(dbFilled)
		{
			buildClassTreeFromDb();
		}
		else
		{
			Tripel<String, String, AsyncStringResponse> request = new Tripel<String, String, AsyncStringResponse>(jsonUrl, "application/json", this);
			new FileRetrieverTask().execute(request);
		}
	}
	
	protected void buildClassTreeFromDb()
	{
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		retriever.useSubjects(subjects);
	}

	public void processString(String jsonString)
	{
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		try {
			JSONObject json = new JSONObject(jsonString);
			JSONArray results = json.getJSONObject("content").getJSONArray("pages");

			Log.v(NJCTLLOG, "Looping through " + Integer.toString(results.length()) + " subjects...");
			
			RuntimeExceptionDao<Subject, String> dao = Subject.getDao();
			
			for(int i = 0; i < results.length(); i++)
			{
				Subject subject = Subject.get(results.getJSONObject(i));
				subjects.add(subject);
			}
			
			//TODO remove subjects that arent in the json anymore.
			
			//TODO store the information that the json has been downloaded and parsed successfully.
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
		
		retriever.useSubjects(subjects);
	}
}
