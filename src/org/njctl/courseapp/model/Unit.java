package org.njctl.courseapp.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.material.Document;
import org.njctl.courseapp.model.material.Handout;
import org.njctl.courseapp.model.material.Homework;
import org.njctl.courseapp.model.material.Lab;
import org.njctl.courseapp.model.material.Presentation;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

@DatabaseTable
public class Unit implements Parcelable, DownloadFinishListener<Document>
{
	@DatabaseField(id = true)
    private Integer id;
	
	@DatabaseField
    private String title;
	
	@DatabaseField
    protected boolean subscribed = false;
	
	@ForeignCollectionField(eager = true, orderColumnName = "order")
    private ForeignCollection<Homework> homeworks;
	
	@ForeignCollectionField(eager = true, orderColumnName = "order")
    private ForeignCollection<Presentation> presentations;
	
	@ForeignCollectionField(eager = true, orderColumnName = "order")
    private ForeignCollection<Lab> labs;
	
	@ForeignCollectionField(eager = true, orderColumnName = "order")
    private ForeignCollection<Handout> handouts;
    
    @DatabaseField
    private Date lastUpdate;
    
    @DatabaseField
    protected Integer order;
    
    @DatabaseField(canBeNull = false, foreign = true)
    protected Class theClass;
    
    DownloadFinishListener<Unit> downloadFinishListener;
    Integer downloading = 0;
    
    protected final static String HW = "homework", PRES = "presentations", HANDOUT = "handouts", LABS = "labs";
    
    private static RuntimeExceptionDao<Unit, Integer> dao;
    
    protected boolean created = false;

	public static void setDao(RuntimeExceptionDao<Unit, Integer> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Unit()
    {
    	
    }
    
    public void subscribe()
    {
    	subscribed = true;
    }
    
    public void unsubscribe()
    {
    	subscribed = false;
    }
    
    public boolean isSubscribed()
    {
    	return subscribed;
    }
    
    public void download()
    {
    	downloading = homeworks.size() + presentations.size() + labs.size() + handouts.size();
    	
    	for(Homework content : homeworks)
    	{
    		content.download(this);
    	}
    	for(Presentation content : presentations)
    	{
    		content.download(this);
    	}
    	for(Lab content : labs)
    	{
    		content.download(this);
    	}
    	for(Handout content : handouts)
    	{
    		content.download(this);
    	}
    }
    
    public void download(DownloadFinishListener<Unit> listener)
    {
    	downloadFinishListener = listener;
    	
    	download();
    }
    
    public void delete()
    {
    	//TODO do for other contents too.
    	for(Homework content : homeworks)
    	{
    		content.deleteFile();
    	}
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
    
    boolean wasCreated()
	{
		return created;
	}
    
    public static Unit get(Class theClass, JSONObject json, int newOrder)
	{
		try {
			if (checkJSON(json)) {
				Unit content;
				if (dao.idExists(json.getInt("ID"))) {
					content = dao.queryForId(json.getInt("ID"));
					content.created = false;
					content.order = newOrder;
					content.setProperties(json);
				} else {
					content = new Unit(theClass, json);
					content.order = newOrder;
					content.created = true;
				}
				return content;
			} else {
				Log.v("NJCTLLOG", "class json wrong..");
				return null;
			}
		} catch (Exception e) {
			Log.v("NJCTLLOG", "unit exception: " + e.getMessage());
			Log.v("NJCTLLOG", Log.getStackTraceString(e));
			return null;
		}
	}
    
    protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getInt("ID"); //Log.v("NJCTLLOG", "unit id: " + id);
			json.getString("post_title");
			json.getString("post_name");
			json.getString("post_modified");
			
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
			id = json.getInt("ID");
			
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONObject content = json.getJSONObject("content");
			
            Log.v("NJCTLLOG", "            Created Unit " + title + ", now adding contents:");
            
            RuntimeExceptionDao<Homework, String> dao1 = Homework.getDao();
            RuntimeExceptionDao<Handout, String> dao2 = Handout.getDao();
            RuntimeExceptionDao<Lab, String> dao3 = Lab.getDao();
            RuntimeExceptionDao<Presentation, String> dao4 = Presentation.getDao();
            
            Map<String, Object> condition = new ArrayMap<String, Object>(1);
			condition.put("unit_id", this.getId());
			
			List<Homework> oldContents1 = dao1.queryForFieldValues(condition);
			List<Handout> oldContents2 = dao2.queryForFieldValues(condition);
			List<Lab> oldContents3 = dao3.queryForFieldValues(condition);
			List<Presentation> oldContents4 = dao4.queryForFieldValues(condition);
			
			List<String> newIds = new ArrayList<String>();
			
			if(content.has(HW))
			{
				JSONArray homeworkList = content.getJSONArray(HW);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(homeworkList.length()) + " homeworks...");
				
				for(int i = 0; i < homeworkList.length(); i++)
				{
					Homework hw = Homework.get(this, homeworkList.getJSONObject(i), this, i);
					if(hw != null)
					{
						newIds.add(hw.getId());
						
						if(hw.wasCreated())
						{
							homeworks.add(hw);
						}
						else
						{
							homeworks.update(hw);
						}
					}
				}
			}
			
			for(Homework oldContent : oldContents1)
			{
				if(!newIds.contains(oldContent.getId()))
				{
					Log.v("NJCTLLOG", "              hw has been deleted from json!");
					dao1.delete(oldContent);
				}
			}
			newIds.clear();
			
			if(content.has(PRES))
			{
				JSONArray presentationList = content.getJSONArray(PRES);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(presentationList.length()) + " presentations...");
				
				for(int i = 0; i < presentationList.length(); i++)
				{
					Presentation presentation = Presentation.get(this, presentationList.getJSONObject(i), this, i);
					
					if(presentation != null)
					{
						newIds.add(presentation.getId());
						
						if(presentation.wasCreated())
						{
							presentations.add(presentation);
						}
						else
						{
							presentations.update(presentation);
						}
					}
				}
			}
			else
				Log.v("NJCTLLOG", "            No presentations found.");
			
			for(Presentation oldContent : oldContents4)
			{
				if(!newIds.contains(oldContent.getId()))
				{
					Log.v("NJCTLLOG", "              pres has been deleted from json!");
					dao4.delete(oldContent);
				}
			}
			newIds.clear();

			
			if(content.has(LABS))
			{
				JSONArray labList = content.getJSONArray(LABS);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(labList.length()) + " labs...");
				
				for(int i = 0; i < labList.length(); i++)
				{
					Lab lab = Lab.get(this, labList.getJSONObject(i), this, i);
					
					if(lab != null)
					{
						newIds.add(lab.getId());
						
						if(lab.wasCreated())
						{
							labs.add(lab);
						}
						else
						{
							labs.update(lab);
						}
					}
				}
			}
			
			for(Lab oldContent : oldContents3)
			{
				if(!newIds.contains(oldContent.getId()))
				{
					Log.v("NJCTLLOG", "              lab has been deleted from json!");
					dao3.delete(oldContent);
				}
			}
			newIds.clear();
			
			if(content.has(HANDOUT))
			{
				JSONArray handoutList = content.getJSONArray(HANDOUT);
				Log.v("NJCTLLOG", "            Looping through " + Integer.toString(handoutList.length()) + " handouts...");
				
				for(int i = 0; i < handoutList.length(); i++)
				{
					Handout handout = Handout.get(this, handoutList.getJSONObject(i), this, i);
					
					if(handout != null)
					{
						newIds.add(handout.getId());
						
						if(handout.wasCreated())
						{
							handouts.add(handout);
						}
						else
						{
							handouts.update(handout);
						}
					}
				}
			}
			
			for(Handout oldContent : oldContents2)
			{
				if(!newIds.contains(oldContent.getId()))
				{
					Log.v("NJCTLLOG", "              handout has been deleted from json!");
					dao2.delete(oldContent);
				}
			}
			
			
			
		} catch (JSONException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
    	catch (SQLException e)
		{
    		e.printStackTrace();
			Log.w("SQL Unit ERR", e.toString());
		}
    	catch (ParseException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
	}
    
    public Unit(Class theKlass, JSONObject json)
    {
    	theClass = theKlass;
    	homeworks = dao.getEmptyForeignCollection("homeworks");
    	presentations = dao.getEmptyForeignCollection("presentations");
    	labs = dao.getEmptyForeignCollection("labs");
    	handouts = dao.getEmptyForeignCollection("handouts");
    	setProperties(json);
    }
    
    public Class getCTLClass()
    {
    	return theClass;
    }
    
	public Integer getId() {
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
    	dest.writeInt(id);
    	//dest.writeString(title);
    	//dest.writeParcelableArray(homeworks.toArray(new Homework[homeworks.size()]), 0);
    	//dest.writeParcelableArray(presentations.toArray(new Presentation[presentations.size()]), 0);
    	//dest.writeParcelableArray(contents.toArray(new NJCTLDocList[contents.size()]), 0);
    }
    
    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {
    	
    	public Unit createFromParcel(Parcel in) {
    		Integer id = in.readInt();
    		
    		return dao.queryForId(id);
    	}
    	
    	public Unit[] newArray(int size) {
    		return new Unit[size];
    	}
    	
    };

	@Override
	public void onDownloaded(Document content)
	{
		downloading--;
		
		Log.v("NJCTLDownload", "Downloading Unit " + getTitle() + " progressed, " + downloading + " remaining.");
		
		if(downloading == 0) Log.v("NJCTLDownload", "Downloading Unit " + getTitle() + " complete.");
		if(downloading == 0 && downloadFinishListener != null)
			downloadFinishListener.onDownloaded(this);
	}
	
	public boolean isDownloading()
	{
		return downloading != 0;
	}

	public static RuntimeExceptionDao<Unit, Integer> getDao()
	{
		return dao;
	}
	
	public String toString()
	{
		return getTitle();
	}
    
}
