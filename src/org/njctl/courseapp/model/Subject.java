package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

@DatabaseTable
public class Subject implements Parcelable, DownloadFinishListener<Class>
{
	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField
	private String name;

	@DatabaseField
	private String title;

	@DatabaseField
	private Date lastUpdate;

	@DatabaseField
	protected Integer order;
	
	@ForeignCollectionField(eager = true, orderColumnName = "order")
	protected ForeignCollection<Class> classes;
	
	private int bottomColorBarResource = 0;
	private int bigSideColorBarResource = 0;
	private int smallSideColorBarResource = 0;
	
	private static RuntimeExceptionDao<Subject, Integer> dao;
	
	protected DownloadFinishListener<Subject> downloadFinishListener;

	public static void setDao(RuntimeExceptionDao<Subject, Integer> newDao)
	{
		if (dao == null)
			dao = newDao;
	}

	public static RuntimeExceptionDao<Subject, Integer> getDao()
	{
		return dao;
	}

	// For ORM.
	Subject()
	{
		
	}

	public Integer getId()
	{
		return id;
	}
	
	public Integer getNumberClasses()
	{
		return classes.size();
	}

	public static Subject get(JSONObject json, DownloadFinishListener<Subject> listener, int newOrder)
	{
		try
		{
			if (checkJSON(json))
			{
				Subject subject;
				
				if (dao.idExists(json.getInt("ID")))
				{
					subject = dao.queryForId(json.getInt("ID"));
					subject.downloadFinishListener = listener;
					
					Log.v("NJCTLLOG", "Loaded Subject with " + subject.getNumberClasses() + " classes.");
					subject.order = newOrder;
					subject.setProperties(json);
					Log.v("NJCTLLOG", "Subject classes after property setting: " + subject.getNumberClasses() + " classes.");
					dao.update(subject);
				}
				else
				{
					subject = new Subject(json);
					subject.downloadFinishListener = listener;
					subject.order = newOrder;
					dao.create(subject);
					Log.v("NJCTLLOG", "Saved Subject with " + subject.getNumberClasses() + " classes.");
				}
				
				return subject;
			}
			else
			{
				return null;
			}
		} catch (Exception e) { // never executed..
			Log.v("NJCTLLOGSUBJECT", Log.getStackTraceString(e));
			return null;
		}
	}

	protected static boolean checkJSON(JSONObject json)
	{
		try 
		{
			json.getString("ID");
			json.getString("post_name");
			json.getString("post_title");
			json.getString("post_modified");
			json.getJSONObject("content").getJSONArray("classes");

			return true;
		}
		catch (JSONException e) 
		{
			Log.w("NJCTLLOG", "    subject contents not found...");
			return false;
		}
	}

	public Subject(JSONObject json)
	{
		classes = dao.getEmptyForeignCollection("classes");
		setProperties(json);
	}
	
	public void delete()
	{
		for(Class theClass : classes)
		{
			theClass.unsubscribe();
		}
		
		dao.delete(this);
	}
	
	protected void setProperties(JSONObject json)
	{
		try {
			id = json.getInt("ID");
			name = json.getString("post_name");
			title = json.getString("post_title");
			if(classes == null)
				classes = dao.getEmptyForeignCollection("classes");

			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);

			RuntimeExceptionDao<Class, Integer> dao = Class.getDao();
			Map<String, Object> condition = new ArrayMap<String, Object>(1);
			condition.put("subject_id", this.getId());
			List<Class> oldContents = dao.queryForFieldValues(condition);
			List<Integer> newIds = new ArrayList<Integer>();
			
			JSONArray classList = json.getJSONObject("content").getJSONArray("classes");
			Log.v("NJCTLLOG", "    Looping through " + Integer.toString(classList.length()) + " classes in Subject " + title +"...");

			for (int i = 0; i < classList.length(); i++)
			{
				Class theClass = Class.get(this, classList.getJSONObject(i), this, i);
				
				if(theClass == null)
				{
					Log.v("NJCTLLOG", "The class is null and will therefore not be added to the subject");
				}
				else
				{
					newIds.add(theClass.getId());
					
					if (theClass.wasCreated())
					{
						Log.v("NJCTLLOG", "Adding subject class with id " + theClass.getId());
						classes.add(theClass);
					}
					else
					{
						Log.v("NJCTLLOG", "updating subject class with id " + theClass.getId());
						classes.update(theClass);
					}
				}
			}
			
			for(Class oldContent : oldContents)
			{
				if(!newIds.contains(oldContent.getId()))
				{
					Log.v("NJCTLLOG", "class has been deleted from json!");
					dao.delete(oldContent);
				}
			}
			
		} catch (Exception e) {
			Log.w("PARSE ERR", e.toString());
			Log.w("NJCTLLOG", Log.getStackTraceString(e));
		}
	}

	public ArrayList<Class> getClassesSubscribed()
	{
		ArrayList<Class> classList = new ArrayList<Class>();
		Log.v("NJCTLLOG", "Current number of classes in subject " + getTitle() + ":" + classes.size());
		
		for (Class klass : classes) {
			if (klass.isSubscribed())
				classList.add(klass);
		}
		
		Log.v("NJCTLLOG", "Current number of subscribed classes in subject " + getTitle() + ":" + classList.size());

		return classList;
	}

	public ArrayList<Class> getClassesDownloaded()
	{
		ArrayList<Class> classList = new ArrayList<Class>();
		Log.v("NJCTLLOG", "Current number of classes in subject " + getTitle() + ":" + classes.size());
		
		for (Class klass : classes) {
			if (klass.isDownloaded())
				classList.add(klass);
		}
		
		Log.v("NJCTLLOG", "Current number of downloaded classes in subject " + getTitle() + ":" + classList.size());

		return classList;
	}

	public String getTitle()
	{
		return title;
	}

	public int getBottomColorBarResource()
	{
		return bottomColorBarResource;
	}

	public int getBigSideColorBarResource()
	{
		return bigSideColorBarResource;
	}

	public int getSmallSideColorBarResource()
	{
		return smallSideColorBarResource;
	}

	public ArrayList<Class> getContents()
	{
		return new ArrayList<Class>(classes);
	}

	// Methods for Parcelable implementation.
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
	}

	public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>()
	{
		public Subject createFromParcel(Parcel in)
		{
			Integer id = in.readInt();
			Subject subject = dao.queryForId(id);
			Log.v("NJCTLLOG", "Trying to create subject from dao with id " + id + ", received subject with " + subject.getNumberClasses() + " classes.");
			return subject;
		}

		public Subject[] newArray(int size)
		{
			return new Subject[size];
		}
	};

	public Collection<Class> getClasses()
	{
		return classes;
	}

	@Override
	public void onDownloaded(Class content)
	{
		// TODO Auto-generated method stub
		
	}

}
