package org.njctl.courseapp.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.useful.Tripel;
import org.njctl.courseapp.model.material.*;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;
import android.util.Log;

public class Model implements AsyncResponse<String>, DownloadFinishListener<Subject>
{
	final String NJCTLLOG = "NJCTLLOG";
	private String jsonUrl = "http://content.sandbox-njctl.org/courses.json";
	protected Context context;
	protected DatabaseHelper dbHelper;
	protected ArrayList<Subject> subjects;
	
	protected ModelRetriever retriever;
	protected DownloadFinishListener<Model> downloadFinishListener;
	
	protected RuntimeExceptionDao<Class, Integer> classDao;
	
	protected Integer downloadingSubjects = 0;
	
	public Model(Context ctx)
	{
		dbHelper = OpenHelperManager.getHelper(ctx, DatabaseHelper.class);
		
		RuntimeExceptionDao<Subject, Integer> dao1 = dbHelper.getRuntimeExceptionDao(Subject.class);
		Subject.setDao(dao1);
		classDao = dbHelper.getRuntimeExceptionDao(Class.class);
		Class.setDao(classDao);
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
	
	public Class getLastOpenedClass()
	{
		try
		{
			QueryBuilder<Class, Integer> builder = classDao.queryBuilder();
			builder.orderBy("lastOpened", false).limit(1l).where().eq("subscribed", true);
		
			PreparedQuery<Class> preparedQuery = builder.prepare();
			List<Class> classes = classDao.query(preparedQuery);
			
			return classes.get(0);
		}
		catch (SQLException e)
		{
			Log.v("NJCTLSQL", Log.getStackTraceString(e));
			return null;
		}

		//return subjects.get(0).getContents().get(0);
	}
	
	public boolean fetchManifest(ModelRetriever retrieverObject)
	{
		retriever = retrieverObject;
		
		if(buildClassTreeFromDb())
		{
			return true;
		}
		else
		{
			//update();
			return false;
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
	
	public ArrayList<Class> getClasses()
	{
		ArrayList<Class> myClasses = new ArrayList<Class>();
		for (int i=0; i < subjects.size(); ++i) {
			myClasses.addAll(subjects.get(i).getClasses());
		}
		return myClasses;
	}
	
	@SuppressWarnings("unchecked")
	public void update()
	{
		Log.v("NJCTLLOG", "Building class tree from online JSON...");
		Tripel<String, String, AsyncResponse<String>> request = new Tripel<String, String, AsyncResponse<String>>(jsonUrl, "application/json", this);
		new StringRetrieverTask().execute(request);
	}
	
	public void update(DownloadFinishListener<Model> listener)
	{
		downloadFinishListener = listener;
		update();
	}
	
	protected boolean buildClassTreeFromDb()
	{
		RuntimeExceptionDao<Subject, Integer> dao = Subject.getDao();
		List<Subject> oldSubjects = dao.queryForAll();
		
		ArrayList<Subject> subjects = new ArrayList<Subject>(oldSubjects);
		
		if(subjects.size() > 0)
		{
			this.subjects = subjects;
			retriever.onModelReady();
			return true;
		}
		else
		{
			return false; //when local copy is empty
		}
	}

	public void processResult(String jsonString)
	{
		subjects = new ArrayList<Subject>();
		
		try
		{
			JSONObject json = new JSONObject(jsonString);
			JSONArray results = json.getJSONObject("content").getJSONArray("subjects");

			Log.v(NJCTLLOG, "Looping through " + Integer.toString(results.length()) + " subjects...");
			
			RuntimeExceptionDao<Subject, Integer> dao = Subject.getDao();
			List<Subject> oldSubjects = dao.queryForAll();
			List<Integer> newIds = new ArrayList<Integer>();
			
			for(int i = 0; i < results.length(); i++)
			{
				Subject subject = Subject.get(results.getJSONObject(i), this, i);
				downloadingSubjects++;
				subjects.add(subject);
				newIds.add(subject.getId());
			}
			
			for(Subject oldSubject : oldSubjects)
			{
				if(!newIds.contains(oldSubject.getId()))
				{
					Log.v("NJCTLLOG", "subject has been deleted from json!");
					dao.delete(oldSubject);
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

	@Override
	public void onDownloaded(Subject content)
	{
		downloadingSubjects--;
		if(downloadingSubjects == 0 && downloadFinishListener != null)
			downloadFinishListener.onDownloaded(this);
	}
}
