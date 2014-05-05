package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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
import android.util.Log;

@DatabaseTable
public class Subject implements Parcelable
{
	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField
	private String name;

	@DatabaseField
	private String title;

	@DatabaseField
	private Date lastUpdate;

	@ForeignCollectionField(eager = true)
	protected ForeignCollection<Class> classes;

	private int bottomColorBarResource = 0;
	private int bigSideColorBarResource = 0;
	private int smallSideColorBarResource = 0;
	
	private static RuntimeExceptionDao<Subject, Integer> dao;

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

	public static Subject get(JSONObject json)
	{
		try {
			if (checkJSON(json)) {
				if (dao.idExists(json.getInt("ID"))) {
					Subject subject = dao.queryForId(json.getInt("ID"));
					subject.setProperties(json);
					dao.update(subject);
					return subject;
				} else {
					Subject subject = new Subject(json);
					dao.create(subject);

					return subject;
				}
			} else {
				return null;
			}
		} catch (Exception e) { // never executed..
			return null;
		}
	}

	protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getString("ID");
			json.getString("post_name");
			json.getString("post_modified");
			json.getJSONObject("content").getJSONArray("pages");

			return true;
		} catch (JSONException e) {
			Log.w("NJCTLLOG", "    subject contents not found...");
			return false;
		}
	}

	public Subject(JSONObject json)
	{
		setProperties(json);
	}
	
	protected void setProperties(JSONObject json)
	{
		try {
			id = json.getInt("ID");
			name = json.getString("post_name");
			title = json.getString("post_title");

			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);

			JSONArray classList = json.getJSONObject("content").getJSONArray("pages");
			Log.v("NJCTLLOG", "    Looping through " + Integer.toString(classList.length()) + " classes...");

			//TODO account for existing classes that have been deleted out of the json.
			for (int i = 0; i < classList.length(); i++) {
				Class theClass = Class.get(this, classList.getJSONObject(i));

				if (theClass != null && !classes.contains(theClass))
				{
					classes.add(theClass);
				}
			}
		} catch (Exception e) {
			Log.w("PARSE ERR", e.toString());
		}
	}

	public ArrayList<Class> getClassesDownloaded()
	{
		ArrayList<Class> classList = new ArrayList<Class>();

		for (Class klass : this.classes) {
			if (klass.isDownloaded())
				classList.add(klass);
		}

		return classList;
	}

	public void add(Class aClass)
	{
		this.classes.add(aClass);
	}

	// Parcelable constructor.
	public Subject(Parcel in)
	{
		readFromParcel(in);
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
		// dest.writeInt(subjectId);
		dest.writeString(title);
		dest.writeParcelableArray(classes.toArray(new Class[classes.size()]), 0);
	}

	private void readFromParcel(Parcel in)
	{
		// subjectId = in.readInt();
		title = in.readString();
		// classes = new ArrayList<Class>();
		in.readList(getContents(), Class.class.getClassLoader());
	}

	public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
		public Subject createFromParcel(Parcel in)
		{
			return new Subject(in);
		}

		public Subject[] newArray(int size)
		{
			return new Subject[size];
		}
	};

}
