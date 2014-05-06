package org.njctl.courseapp.model;

import org.njctl.courseapp.R;
import org.njctl.courseapp.model.material.*;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "njctl.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
	private boolean justCreated = false;
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1)
	{
		justCreated = true;
		
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			// Do that for more
			TableUtils.createTable(connectionSource, Subject.class);
			TableUtils.createTable(connectionSource, Class.class);
			TableUtils.createTable(connectionSource, Unit.class);
			TableUtils.createTable(connectionSource, Handout.class);
			TableUtils.createTable(connectionSource, Lab.class);
			TableUtils.createTable(connectionSource, Homework.class);
			TableUtils.createTable(connectionSource, Presentation.class);
			TableUtils.createTable(connectionSource, Topic.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}
	
	public boolean isJustCreated()
	{
		return justCreated;
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Subject.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
}
