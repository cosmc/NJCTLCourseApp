package org.njctl.courseapp.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.useful.Tripel;
import org.njctl.courseapp.model.material.*;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Context;
import android.util.Log;

public class Model implements AsyncStringResponse
{
	final String NJCTLLOG = "NJCTLLOG";
	private String jsonUrl = "http://content.sandbox-njctl.org/courses.json";
	protected Context context;
	protected DatabaseHelper dbHelper;
	protected boolean dbFilled = false;
	protected ArrayList<Subject> subjects;
	
	protected ModelRetriever retriever;
	
	public Model(Context ctx)
	{
		dbHelper = OpenHelperManager.getHelper(ctx, DatabaseHelper.class);
		
		RuntimeExceptionDao<Subject, Integer> dao1 = dbHelper.getRuntimeExceptionDao(Subject.class);
		Subject.setDao(dao1);
		RuntimeExceptionDao<Class, Integer> dao2 = dbHelper.getRuntimeExceptionDao(Class.class);
		Class.setDao(dao2);
		RuntimeExceptionDao<Unit, Integer> dao3 = dbHelper.getRuntimeExceptionDao(Unit.class);
		Unit.setDao(dao3);
		RuntimeExceptionDao<Handout, String> dao4 = dbHelper.getRuntimeExceptionDao(Handout.class);
		Handout.setDao(dao4);
		RuntimeExceptionDao<Homework, String> dao5 = dbHelper.getRuntimeExceptionDao(Homework.class);
		Homework.setDao(dao5);
		RuntimeExceptionDao<Lab, String> dao6 = dbHelper.getRuntimeExceptionDao(Lab.class);
		Lab.setDao(dao6);
		RuntimeExceptionDao<Presentation, String> dao7 = dbHelper.getRuntimeExceptionDao(Presentation.class);
		Presentation.setDao(dao7);
		RuntimeExceptionDao<Topic, String> dao8 = dbHelper.getRuntimeExceptionDao(Topic.class);
		Topic.setDao(dao8);
		
		Document.setContext(ctx);
	}
	
	public void fetchManifest(ModelRetriever retrieverObject)
	{
		retriever = retrieverObject;
		
		//if(true)
		if(!buildClassTreeFromDb())
		{
			update();
		}
	}
	
	public ArrayList<Subject> getSubjects()
	{
		return subjects;
	}
	
	public ArrayList<Class> getClassesSubscribed()
	{
		ArrayList<Class> myClasses = new ArrayList<Class>();
		for (int i=0; i < subjects.size(); ++i) {
			myClasses.addAll(subjects.get(i).getClassesSubscribed());
		}
		return myClasses;
	}
	
	@SuppressWarnings("unchecked")
	public void update()
	{
		Log.v("NJCTLLOG", "Building class tree from online JSON...");
		Tripel<String, String, AsyncStringResponse> request = new Tripel<String, String, AsyncStringResponse>(jsonUrl, "application/json", this);
		new FileRetrieverTask().execute(request);
	}
	
	protected boolean buildClassTreeFromDb()
	{
		RuntimeExceptionDao<Subject, Integer> dao = Subject.getDao();
		List<Subject> oldSubjects = dao.queryForAll();
		
		ArrayList<Subject> subjects = new ArrayList<Subject>(oldSubjects);
		
		if(subjects.size() > 0)
		{
			retriever.onModelReady();
			return true;
		}
		else
		{
			return false;
		}
	}

	public void processString(String jsonString)
	{
		subjects = new ArrayList<Subject>();
		
		try
		{
			JSONObject json = new JSONObject(jsonString);
			JSONArray results = json.getJSONObject("content").getJSONArray("subjects");

			Log.v(NJCTLLOG, "Looping through " + Integer.toString(results.length()) + " subjects...");
			
			RuntimeExceptionDao<Subject, Integer> dao = Subject.getDao();
			List<Subject> oldSubjects = dao.queryForAll();
			
			for(int i = 0; i < results.length(); i++)
			{
				Subject subject = Subject.get(results.getJSONObject(i));
				subjects.add(subject);
			}
			
			//TODO store this information on the device.
			dbFilled = true;
			
			for(Subject oldSubject : oldSubjects)
			{
				//TODO this check is not working
				if(!dao.idExists(oldSubject.getId()))
				{
					Log.v("NJCTLLOG", "subject has been deleted from json!");
					
					//TODO delete oldSubject.
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
		
		retriever.onModelReady();
	}
	
	protected void onDestroy()
	{
	    if (dbHelper != null) {
	        OpenHelperManager.releaseHelper();
	        dbHelper = null;
	    }
	}
}
