package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.material.Handout;
import org.njctl.courseapp.model.material.Homework;
import org.njctl.courseapp.model.material.Lab;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@DatabaseTable
public class Unit implements Parcelable
{
	@DatabaseField(id = true)
    private String id;
	
	@DatabaseField
    private String title;
	
	@ForeignCollectionField(eager = true)
    private ForeignCollection<Homework> homeworks;
	
	@ForeignCollectionField(eager = true)
    private ForeignCollection<Presentation> presentations;
	
	@ForeignCollectionField(eager = true)
    private ForeignCollection<Lab> labs;
	
	@ForeignCollectionField(eager = true)
    private ForeignCollection<Handout> handouts;
    
    @DatabaseField
    private Date lastUpdate;
    
    @DatabaseField(canBeNull = false, foreign = true)
    protected Class theClass;
    
    protected final static String HW = "homework", PRES = "presentations", HANDOUT = "handouts", LABS = "labs";
    
    private static RuntimeExceptionDao<Unit, Integer> dao;

	public static void setDao(RuntimeExceptionDao<Unit, Integer> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Unit()
    {
    	
    }
    
    public boolean isDownloaded()
    {
    	for(Homework content : homeworks)
    	{
    		if(!content.isDownloaded())
    			return false;
    	}
    	for(Presentation content : presentations)
    	{
    		if(!content.isDownloaded())
    			return false;
    	}
    	for(Lab content : labs)
    	{
    		if(!content.isDownloaded())
    			return false;
    	}
    	for(Handout content : handouts)
    	{
    		if(!content.isDownloaded())
    			return false;
    	}

    	return true;
    }
    
    public ArrayList<Homework> getHomeworks()
    {
    	return new ArrayList<Homework>(homeworks);
    }
    
    public ArrayList<Presentation> getPresentations()
    {
    	return new ArrayList<Presentation>(presentations);
    }
    
    public ArrayList<Lab> getLabs()
    {
    	return new ArrayList<Lab>(labs);
    }
    
    public ArrayList<Handout> getHandouts()
    {
    	return new ArrayList<Handout>(handouts);
    }
    
    public static Unit get(Class theClass, JSONObject json)
	{
		try {
			if (checkJSON(json)) {
				if (dao.idExists(json.getInt("ID"))) {
					Unit content = dao.queryForId(json.getInt("ID"));
					content.setProperties(json);
					dao.update(content);
					return content;
				} else {
					Unit content = new Unit(theClass, json);
					dao.create(content);

					return content;
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
			
			json.getString("post_title");
    		
    		JSONObject content = json.getJSONObject("content");
    		
    		if(!content.has(HW) && !content.has(PRES) && !content.has(HANDOUT) && !content.has(LABS))
    		{
    			//Throws exception
    			content.getJSONArray("presentations");
    		}

			return true;
		} catch (JSONException e) {
			Log.w("NJCTLLOG", "    Unit contents not found...");
			return false;
		}
	}
    
    protected void setProperties(JSONObject json)
	{
    	try {
			title = json.getString("post_title");
			
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONObject content = json.getJSONObject("content");
			
			if(content.has(HW))
			{
				JSONArray homeworkList = content.getJSONArray(HW);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(homeworkList.length()) + " homeworks...");
				
				for(int i = 0; i < homeworkList.length(); i++)
				{
					Homework hw = Homework.get(this, homeworkList.getJSONObject(i));
					homeworks.add(hw);
				}
			}
			
			if(content.has(PRES))
			{
				JSONArray presentationList = content.getJSONArray(PRES);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(presentationList.length()) + " presentations...");
				
				for(int i = 0; i < presentationList.length(); i++)
				{
					Presentation presentation = Presentation.get(this, presentationList.getJSONObject(i));
					
					if(presentation != null)
					{
						presentations.add(presentation);
					}
				}
			}
			
			if(content.has(LABS))
			{
				JSONArray labList = content.getJSONArray(LABS);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(labList.length()) + " labs...");
				
				for(int i = 0; i < labList.length(); i++)
				{
					Lab lab = Lab.get(this, labList.getJSONObject(i));
					
					if(lab != null)
					{
						labs.add(lab);
					}
				}
			}
			
			if(content.has(HANDOUT))
			{
				JSONArray handoutList = content.getJSONArray(HANDOUT);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(handoutList.length()) + " handouts...");
				
				for(int i = 0; i < handoutList.length(); i++)
				{
					Handout handout = Handout.get(this, handoutList.getJSONObject(i));
					
					if(handout != null)
					{
						handouts.add(handout);
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		} catch (ParseException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
	}
    
    public Unit(Class theKlass, JSONObject json)
    {
    	theClass = theKlass;
    	setProperties(json);
    }
    
    // Mandatory Parcelable constructor.
    public Unit(Parcel in) {
    	readFromParcel(in);
    }
    
	public String getId() {
    	return id;
    }
    
    public String getTitle() {
    	return title;
    }
    
    // Mandatory Parcelable methods.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(id);
    	dest.writeString(title);
    	dest.writeParcelableArray(homeworks.toArray(new Homework[homeworks.size()]), 0);
    	//dest.writeParcelableArray(presentations.toArray(new Presentation[presentations.size()]), 0);
    	//dest.writeParcelableArray(contents.toArray(new NJCTLDocList[contents.size()]), 0);
    }

    private void readFromParcel(Parcel in) {
    	this.id = in.readString();
    	this.title = in.readString();
    	/*this.homeworks = new ArrayList<Homework>();
    	in.readList(this.homeworks, Homework.class.getClassLoader());
    	this.presentations = new ArrayList<Presentation>();
        in.readList(this.presentations, Presentation.class.getClassLoader());*/
    }
    
    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {
    	
    	public Unit createFromParcel(Parcel in) {
    		return new Unit(in);
    	}
    	
    	public Unit[] newArray(int size) {
    		return new Unit[size];
    	}
    	
    };
    
}
