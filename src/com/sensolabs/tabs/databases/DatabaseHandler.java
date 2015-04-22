package com.sensolabs.tabs.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Contacts Table Columns names
/**
 * @author hiren
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// Database fields
	public SQLiteDatabase db;
    
	/**
	 * 
	Context is current state of the application/object. It provides services like resolving resources, 
	obtaining access to databases and preferences*/
	 
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// --->DatabaseHandler Class<----//

		private static final int DATABASE_VERSION =6;

		private static final String DATABASE_NAME = "dataManager";
		public static final String NAME_TABLE = "nametable";
		
		// Contacts table name
		public static final String KEY_NAME = "name";
		public static final String KEY_HR = "firstName";
		public static final String KEY_IBI = "patientID";
		public static final String KEY_HMIN = "MinimumofHeartRate";
		public static final String KEY_HMAX = "MaximumofHeartRate";
		public static final String KEY_IMIN = "MinimumofIBI";
		public static final String KEY_IMAX = "MaximumofIBI";
		public static final String KEY_BMIN = "MinimumofBreathingRate";
		public static final String KEY_BMAX = "MaximumofBreathingRate";

		private static final String TEXT_TYPE = " TEXT";
	//	private static final boolean bool_type=" Boolean"
		private static final String COMMA_SEP = ", ";
		public static final String KEY_ALARM = "SoundAlarm";
		public static final String KEY_MESSAGE = "SendMessage";
		
	
	// Creating Tables
	/**
	 is a String which used to create an SQL table in the Database based on Primary/Unique key as PatientID
	 */
	static final String DATABASE_CREATE_FIRST = "CREATE TABLE "
			+ NAME_TABLE + "(" + KEY_NAME + TEXT_TYPE + COMMA_SEP + KEY_HR
			+ TEXT_TYPE + COMMA_SEP + KEY_IBI + TEXT_TYPE + COMMA_SEP
			+ KEY_HMIN + TEXT_TYPE + COMMA_SEP + KEY_HMAX + TEXT_TYPE
			+ COMMA_SEP + KEY_IMIN + TEXT_TYPE + COMMA_SEP + KEY_IMAX
			+ TEXT_TYPE + COMMA_SEP + KEY_BMIN + TEXT_TYPE + COMMA_SEP
			+ KEY_BMAX + TEXT_TYPE+ COMMA_SEP+KEY_ALARM+" int"+COMMA_SEP+KEY_MESSAGE
			+" int"+"," + " PRIMARY KEY("+ KEY_IBI+"))";

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(DATABASE_CREATE_FIRST);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
	    Log.e(DatabaseHandler.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);
		onCreate(db);
	}
@Override
public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	Log.e(DatabaseHandler.class.getName(),
            "Downgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
	// TODO Auto-generated method stub
	db.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);
	onCreate(db);
}
}
