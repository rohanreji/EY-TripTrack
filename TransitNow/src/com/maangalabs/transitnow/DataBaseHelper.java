package com.maangalabs.transitnow;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;



import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = DataBaseHelper.class.getName();
	

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// Table Names
	private static final String TABLE_TODO = "todos";
	private static final String TABLE_TAG = "tags";
	private static final String TABLE_TODO_TAG = "todo_tags";

	



	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		 String CREATE_CONTACTS_TABLE = "CREATE TABLE " + "Categories" + "("
					+ 
					"CategoryID" + " INTEGER," + "Lat" + " REAL,"
					+ "Lon" + " REAL" + ")";
		 db.execSQL(CREATE_CONTACTS_TABLE);
			
		//new LongOperation().execute(db);	
		
		    
	}
	
	
	public double[] getLati()
	{
		String selectQuery = "SELECT  * FROM " + "Categories";
		double[] lati = new double[200];
		int i=0;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
		do {
		lati[i]=cursor.getFloat(1);
		i++;
		} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lati;
		
	}
	public double[] getLongi()
	{
		String selectQuery = "SELECT  * FROM " + "Categories";
		double[] longi = new double[200];
		int i=0;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
		do {
		longi[i]=cursor.getFloat(2);
		i++;
		} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return longi;
	}
	
	void addContact(double l1,double l2) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put("Lat", l1); // Contact Phone
		values.put("Lon",l2);
		// Inserting Row
		db.insert("Categories", null, values);
		db.close(); // Closing database connection
		
		
		
		
	}
	
	
	private class LongOperation extends AsyncTask<SQLiteDatabase, Void, String> {

        @Override
        protected String doInBackground(SQLiteDatabase... params) {
        
        	 try {
     		SQLiteDBDeploy.deploy(params[0], "http://ingenious-camel.googlecode.com/svn/trunk/SQLiteDBDeployer/assets/northwind.zip");
        		
        	 } 
        	 catch (Exception e) {
     			// TODO Auto-generated catch block
     			
     			
     			e.printStackTrace();
     		}
            return "Executed";
        }
       
        
        @Override
        protected void onPostExecute(String result) {
          
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    
	
	
	/*
	 * 
	 * A dummy function to get the number of rows in categories
	 */
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + "Categories";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int y=cursor.getCount();
		cursor.close();

		// return count
		return y;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS Categories");
	
	// create new tables
		onCreate(db);
	}

	
}