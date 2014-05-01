package org.njctl.courseapp.model.material;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.util.Log;

public class Lab extends Document
{
	public static Lab newInstance(JSONObject json)
	{
		return new Lab(json);
	}
	
	public Lab(JSONObject json)
	{
		try{
			name = json.getString("post_title");
			
			if(json.has("pdf_uri"))
			{
				url = json.getString("pdf_uri");
			}
			else
			{
				Log.w("NJCTLLOG", "                pdf_uri not found for lab " + name);
			}
			
			
			String modified = json.getString("date");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", e.toString());
		}
		catch (ParseException e)
		{
			Log.w("PARSE ERR", e.toString());
		}
	}
	

	public Lab(Parcel in)
	{
		super(in);
		// TODO Auto-generated constructor stub
	}

}
