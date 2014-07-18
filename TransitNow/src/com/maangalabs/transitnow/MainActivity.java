package com.maangalabs.transitnow;

import java.io.BufferedInputStream;
import java.io.BufferedReader;

import com.facebook.*;
import com.facebook.model.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.MapFragment;


@SuppressLint("NewApi") 
public class MainActivity extends FragmentActivity
{
	public double lati[];
	LatLng point1[];
	
	public double longi[];
	public SharedPreferences preferences;
	MapView mapView;
	int p=0;
	GoogleMap map;
	CabAdapter adapter;
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    ImageView my_image;
    String names;
    public static final int progress_bar_type = 0; 
    private ProgressDialog pDialog;
    TextView t9;
    private GoogleMap googleMap;
    @Override
    public void onBackPressed()
   {
    
    	android.os.Process.killProcess(android.os.Process.myPid());     	
   }
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	//ColorDrawable cd = new ColorDrawable(0xD7DF0100);
    	//getActionBar().setBackgroundDrawable(cd);
    	
    	/*
    	 * setting the viewpager up, now it points to 0th element.
    	 */
        viewPager = (ViewPager) findViewById(R.id.pager);
        
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
      
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
   
        /*
         * Initialize the map, and set it point using camera update...
         */
      
        try {
            	initilizeMap();
            	if(googleMap!=null)
            	{
            		googleMap.setMyLocationEnabled(true);
            		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            	
            		CameraPosition cameraPosition = new CameraPosition.Builder().target(
            				new LatLng(8.551006600000000000, 76.910830700000020000)).zoom(12).build();
         
            		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                      	
            	}
            	
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        

    	/*
    	 * Check whether customer is logged in or not using shared preferences.
    	 */
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	int flag = preferences.getInt("f", 0);
    	
    	if(flag!=0)
    	{
    		/*
    		 * use this to check the status of the database. using some api call whether updated or not.
    		 */
    		
    		
    	/*	DataBaseHelper db = new DataBaseHelper(MainActivity.this);
    		lati=new double[db.getContactsCount()];
    		longi=new double[db.getContactsCount()];
    		lati=db.getLati();
    		longi=db.getLongi();
    		Double[] coders=new Double[2];
    		if(db.getContactsCount()!=0)
    		{
    			coders[0]=lati[0];
    			coders[1]=longi[0];
    		//new ReverseGeocodingTask().execute(coders);
    		}
    		MarkerOptions options = new MarkerOptions();
    		for(int i=0;i<db.getContactsCount();i++)
    		{
	
    			options.position(new LatLng(lati[i],longi[i]));
    		}


    		googleMap.addMarker(options);
    		addMarkers();
    		db.close(); */
    		
    		
    		new GetDbVersion().execute("");
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), "Not Logged", Toast.LENGTH_SHORT).show();
    		
    		/*
    		 * Call the mmmmaaaain activity
    		 */
    	}
       
      
        
        
        
        
        
        
        
        
        
        
        
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
   		 
            @Override
            public void onPageSelected(int position) {
            
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
               	  onRefreshListener fragment = (onRefreshListener) mAdapter.instantiateItem(viewPager, arg0);
                  if (fragment != null) {
                      fragment.onRefresh();
                  } 
            
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                
            }
        });
       
        
     
    }

    
    
    
    
    private void initilizeMap() {
        
    	if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
        
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
     
    }
   
    
    
    
    
    
    
       /*
     * Sets the api for google maps for the given routes and return the url corresponding to given route.
     */
    private String getMapsApiDirectionsUrl() {
    	 DataBaseHelper db = new DataBaseHelper(MainActivity.this);
    	 double[]  lati2=new double[db.getContactsCount()];
         double[] longi2=new double[db.getContactsCount()];
         lati2=db.getLati();
         longi2=db.getLongi();
    	
         String waypoints = "waypoints=optimize:true|";
         if(db.getContactsCount()!=0)
         {
        	waypoints=waypoints+lati2[0]+","+longi2[0];
         }
         for(int i=0;i<db.getContactsCount();i++)
         {
           waypoints= waypoints+"|" + "|" + lati2[i] + "," + longi2[i];
         }

         String sensor = "sensor=false";
         String params = waypoints + "&" + sensor;
         String output = "json";
         String url = "https://maps.googleapis.com/maps/api/directions/"
            + output + "?" + params;
         db.close();
         return url;
    }
    
    /*
     * Add markers at all the stops in the route..
     */
    private void addMarkers() {
    	 DataBaseHelper db = new DataBaseHelper(MainActivity.this);
    	 double[]  lati2=new double[db.getContactsCount()];
         double[] longi2=new double[db.getContactsCount()];
         lati2=db.getLati();
         longi2=db.getLongi();
    	
         if (googleMap != null) {
        	 ReadTask downloadTask = new ReadTask();
        	 String url = getMapsApiDirectionsUrl();
        	 downloadTask.execute(url);
        	 if(db.getContactsCount()!=0)
        	 {
        		 CameraPosition cameraPosition = new CameraPosition.Builder().target(
        	 			new LatLng(lati2[0],longi2[0])).zoom(12).build();
           
        		 googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        	 }
        	 for(int i=0;i<db.getContactsCount();i++)
        	 {
        		 googleMap.addMarker(new MarkerOptions().position(new LatLng(lati[i],longi[i]))
            			.title("Point"+i));
        	 }
         
         
    }
    db.close();
 }
	
  /*
   * To create progress dialog on downloading
   */
    
 @Override
 protected Dialog onCreateDialog(int id) {
        switch (id) {
        case progress_bar_type:
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        default:
            return null;
        }
 }
    
    
    /*
     * To check whether on login authentication
     * use authentication api here  
     */

private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
        	try{
        	
        		/*
        		 * Login api call
        		 */
        			Log.d("task","adas");
            		EditText t=(EditText)findViewById(R.id.textView1);
            		String s=t.getText().toString();
                	StringBuilder builder = new StringBuilder();
            	    HttpClient client = new DefaultHttpClient();
            	    HttpGet httpGet = new HttpGet("http://192.168.0.103:1337/employee/findEmpbyId/"+s);
            	    Log.d("tag1","http://192.168.0.103:1337/employee/findEmpbyId/"+s);
            	    try {
            	      HttpResponse response = client.execute(httpGet);
            	      StatusLine statusLine = response.getStatusLine();
            	      int statusCode = statusLine.getStatusCode();
            	      if (statusCode == 200) {
            	    	  HttpEntity entity = response.getEntity();
            	    	  InputStream content = entity.getContent();
            	    	  BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            	    	  String line;
            	    	  while ((line = reader.readLine()) != null) {
            	    		  builder.append(line);
            	    		  //adding the line to origdevicename
            	    	  }        	        
            	      }
            	    } catch (ClientProtocolException e) {
            	    	e.printStackTrace();
            	    } catch (IOException e) {
            	    	e.printStackTrace();
            	    } 
               	    Log.d("checker",builder.toString());
            	    JSONObject jsonObj = new JSONObject(builder.toString());
        	        p=jsonObj.getInt("id");
        	}
            catch(Exception e)
            {
           		Log.e("sdsa",e.getMessage());
           	}
       		return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
           	if(p!=0)
        	{
           		/*
           		 * making the second viewflipper 
           		 */
        		
       		 	
       		 	Routes weather_data[] = new Routes[]
	                      {
	                          new Routes("Pattom","1:30 pm"),
	                          
	                      };
       		 	RoutesAdapter adapter;
	                     
       		 	adapter = new RoutesAdapter(MainActivity.this,
	                              R.layout.list_item_row_home, weather_data);
	                     
	                    // adapter.notifyDataSetChanged();
	              
	                     
       		 	ListView listView1 = (ListView)findViewById(R.id.listView1);
	                      
	                  //  adapter.notifyDataSetChanged();
       		 	listView1.setAdapter(adapter);
       		 	
       		 	new GetDbVersion().execute("");
       		 	
       		 /*
       	    	 * on Login Display the route with the databse , move this code to LongOperation's postexecute.
       	    	 */
       	    	DataBaseHelper db = new DataBaseHelper(MainActivity.this);
       	    	double lati1[]={8.502072,8.583205};
       	    	double longi1[]={76.949892,76.878550};
       	    	for(int h=0;h<lati1.length;h++)
       	    	{
       	    		db.addContact(lati1[h],longi1[h]);        
       	    	}
       	        Log.d("Name: ", db.getContactsCount()+"");
       	        lati=new double[db.getContactsCount()];
       	        longi=new double[db.getContactsCount()];
       	        lati=db.getLati();
       	        longi=db.getLongi();
       	        MarkerOptions options = new MarkerOptions();
       	        for(int i=0;i<db.getContactsCount();i++)
       	        {
       	        	options.position(new LatLng(lati[i],longi[i]));
       	        }
       	      
       	      
       	       googleMap.addMarker(options);
       	       addMarkers();
       	       db.close();
       	       this.cancel(true);
     	   	}
        	/*
        	 * Avoid this code once api is set
        	 */
        	else
        	{
        		
        		//comment this once api is set
            	
            	EditText t=(EditText)findViewById(R.id.textView1);
        		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        		SharedPreferences.Editor editor = preferences.edit();
        		editor.putInt("f",Integer.parseInt(t.getText().toString()));
        		
        		editor.commit();

        	
        		
        	   		
        	   	new GetDbVersion().execute("");
        	   	Routes weather_data[] = new Routes[]
 	            {
        	   			new Routes("Pattom","1:30 pm"),
 	            };
     	        RoutesAdapter adapter;
 	                     
     	        adapter = new RoutesAdapter(MainActivity.this,
 	                              R.layout.list_item_row_home, weather_data);
 	                     
     	        // adapter.notifyDataSetChanged();
 	              
 	                     
     	        ListView listView1 = (ListView)findViewById(R.id.listView1);
 	                      
 	                  //  adapter.notifyDataSetChanged();
     	        listView1.setAdapter(adapter);
        	   	
          		 /*
          	    	 * on Login Display the route with the databse , move this code to LongOperation's postexecute.
          	    	 */
          	    DataBaseHelper db = new DataBaseHelper(MainActivity.this);
          	    double lati1[]={8.502072,8.583205};
          	    double longi1[]={76.949892,76.878550};
          	    for(int h=0;h<lati1.length;h++)
          	    {
          	    		db.addContact(lati1[h],longi1[h]);        
          	    }
          	    Log.d("Name: ", db.getContactsCount()+"");
          	    lati=new double[db.getContactsCount()];
          	    longi=new double[db.getContactsCount()];
          	    lati=db.getLati();
          	    longi=db.getLongi();
          	    MarkerOptions options = new MarkerOptions();
          	    for(int i=0;i<db.getContactsCount();i++)
          	    {
          	       	options.position(new LatLng(lati[i],longi[i]));
          	    }
          	      
          	      
          	    googleMap.addMarker(options);
          	    addMarkers();
          	    db.close();
          	         	   		
      	   		this.cancel(true);
        		
        	}
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * TO read the maps api url and parse it.
	 */
	
	
	
	
	
	 private class ReadTask extends AsyncTask<String, Void, String> {
		    @Override
		    protected String doInBackground(String... url) {
		    	String data = "";
		    	try {
		        HttpConnection http = new HttpConnection();
		        data = http.readUrl(url[0]);
		    	} catch (Exception e) {
		    		Log.d("Background Task", e.toString());
		    	}
		    	return data;
		    }
		 
		    @Override
		    protected void onPostExecute(String result) {
		    	super.onPostExecute(result);
		    	new ParserTask().execute(result);
		    }
	 }
		 /*
		  * parse the json to draw polygon specifies polygon's width.
		  */
		  private class ParserTask extends
		      AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
		 
			  @Override
			  protected List<List<HashMap<String, String>>> doInBackground(
					  String... jsonData) {
		 
				  JSONObject jObject;
				  List<List<HashMap<String, String>>> routes = null;
		 
				  try {
					  jObject = new JSONObject(jsonData[0]);
					  PathJSONParser parser = new PathJSONParser();
					  routes = parser.parse(jObject);
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				  return routes;
		    }
		 
		    @Override
		    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
		    if(haveNetworkConnection())
		    {
		    	ArrayList<LatLng> points = null;
		    	PolylineOptions polyLineOptions = null;
		 
		      // traversing through routes
		    	for (int i = 0; i < routes.size(); i++) {
		    	  points = new ArrayList<LatLng>();
		    	  polyLineOptions = new PolylineOptions();
		    	  List<HashMap<String, String>> path = routes.get(i);
		 
		    	  for (int j = 0; j < path.size(); j++) {
		    		  HashMap<String, String> point = path.get(j);
		 
		    		  double lat = Double.parseDouble(point.get("lat"));
		    		  double lng = Double.parseDouble(point.get("lng"));
		    		  LatLng position = new LatLng(lat, lng);
		    		  points.add(position);
		    	  }
		    	  if(points!=null)
		    	  {
		    		  polyLineOptions.addAll(points);
		    		  polyLineOptions.width(5);
		    		  polyLineOptions.color(Color.BLUE);
		    	  }
		      }
		      if(points!=null)
		    	  googleMap.addPolyline(polyLineOptions);
		    }
		    }
		    }
		  public boolean haveNetworkConnection() {
				 boolean haveConnectedWifi = false;
				 boolean haveConnectedMobile = false;

				 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				 NetworkInfo[] netInfo = cm.getAllNetworkInfo();
				 for (NetworkInfo ni : netInfo) {
					 if (ni.getTypeName().equalsIgnoreCase("WIFI"))
						 if (ni.isConnected())
							 haveConnectedWifi = true;
					 if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
						 if (ni.isConnected())
							 haveConnectedMobile = true;
				 }
			return haveConnectedWifi || haveConnectedMobile;
			 }
		  
	
		  
		  
		  
		  
		  
		  
		  
		  /*
		   * Download the database.
		   */
		  
		  
		  private class DownloadDb extends AsyncTask<String, String, String> {
			  ProgressDialog dialog = new ProgressDialog(MainActivity.this);
			  
			  
		        @Override
		        protected String doInBackground(String... params) {
		        		try {
		        		
		        		URL url = new URL("http://192.168.0.38:1100/get_db");
		        		URLConnection conexion = url.openConnection();
		        		conexion.connect();

		        		int lenghtOfFile = conexion.getContentLength();
		        		Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
		        		
		        		InputStream input = new BufferedInputStream(url.openStream());
		        		OutputStream output = new FileOutputStream("/sdcard/new.db");

		        		byte data[] = new byte[1024];

		        		long total = 0;
		        		int count;
		        			while ((count = input.read(data)) != -1) {
		        				total += count;
		        				publishProgress(""+(int)((total*100)/lenghtOfFile));
		        				output.write(data, 0, count);
		        			}

		        			output.flush();
		        			output.close();
		        			input.close();
		        		} catch (Exception e) {}
		       		
		        	return "Executed";
		        }
		        @Override
		        protected void onPostExecute(String result) {
		        	 if (dialog.isShowing()) {
		                 dialog.dismiss();
		                 
		             }
		        	 /*
		        	  * saving the version in shared preferences.
		        	  */
		        	 
		        	 
		        	 	
						
		        	
		        	
		        /*	 File dbfile = new File("/sdcard/new.db" ); 
		     		SQLiteDatabase  db1 = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		     		String selectQuery = "select stop_name,stop_id from stops order by stop_name;";
		     		Cursor cursor = db1.rawQuery(selectQuery, null);
		     		if (cursor.moveToFirst()) {
		     			
		     			Toast.makeText(getApplicationContext(),"ds: "+cursor.getString(0), Toast.LENGTH_SHORT).show();
		     			
		     		
		     			}
		     		else

		     			Toast.makeText(getApplicationContext(),"es: ", Toast.LENGTH_SHORT).show();
		     			
		     			cursor.close();
		     			db1.close();*/
		        }

		        @Override
		        protected void onPreExecute() {
		       	dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		        	dialog.setMessage("Progress start");
		           dialog.show();
		        }

		        protected void onProgressUpdate(String... progress) {
		   		 Log.d("ANDRO_ASYNC",progress[0]);
		   		 dialog.setProgress(Integer.parseInt(progress[0]));
		   	}
		  }			
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  private class GetDbVersion extends AsyncTask<String, String, String> {
			  
			  String versi;
			  
		        @Override
		        protected String doInBackground(String... params) {
		        		try {
		        		
		        			/*
				        	  * saving the version in shared preferences.
				        	  */
				        	 
				        	 
				        	 	StringBuilder builder = new StringBuilder();
			            	    HttpClient client = new DefaultHttpClient();
			            	    HttpGet httpGet = new HttpGet("http://192.168.0.38:1100/get_db_version");
			            	    Log.d("tag1","http://192.168.0.38:1100/get_db_version");
			            	    try {
			            	      HttpResponse response = client.execute(httpGet);
			            	      StatusLine statusLine = response.getStatusLine();
			            	      int statusCode = statusLine.getStatusCode();
			            	      if (statusCode == 200) {
			            	    	  HttpEntity entity = response.getEntity();
			            	    	  InputStream content = entity.getContent();
			            	    	  BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			            	    	  String line;
			            	    	  while ((line = reader.readLine()) != null) {
			            	    		  builder.append(line);
			            	    		  //adding the line to origdevicename
			            	    	  }        	        
			            	      }
			            	    } catch (ClientProtocolException e) {
			            	    	e.printStackTrace();
			            	    } catch (IOException e) {
			            	    	e.printStackTrace();
			            	    } 
			               	    Log.d("checker",builder.toString());
			            	    JSONObject jsonObj;
								try {
									jsonObj = new JSONObject(builder.toString());
								
									versi=jsonObj.getString("version");
			        	       
				       		 	
								}
								catch(Exception e)
								{
									
								}
		        		}
		        		catch(Exception e)
		        		{
		        			
		        		}
		        		return "executed";
		        }
		        @Override
		        protected void onPostExecute(String result) {
		        	preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		         	String vers = preferences.getString("ver", "0");
		        	if(!vers.equals(versi))
		    		{
		        		 preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			        		SharedPreferences.Editor editor = preferences.edit();
			        		
			       		 	editor.putString("ver",vers);
			       		 	editor.commit();
			       		 	
		        		new DownloadDb().execute(" ");
		    		}
		        	else
		        	{
		        		
		        	}
		        	
		        	
		        /*	 File dbfile = new File("/sdcard/new.db" ); 
		     		SQLiteDatabase  db1 = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		     		String selectQuery = "select stop_name,stop_id from stops order by stop_name;";
		     		Cursor cursor = db1.rawQuery(selectQuery, null);
		     		if (cursor.moveToFirst()) {
		     			
		     			Toast.makeText(getApplicationContext(),"ds: "+cursor.getString(0), Toast.LENGTH_SHORT).show();
		     			
		     		
		     			}
		     		else

		     			Toast.makeText(getApplicationContext(),"es: ", Toast.LENGTH_SHORT).show();
		     			
		     			cursor.close();
		     			db1.close();*/
		        }

		        @Override
		        protected void onPreExecute() {
		       
		        }

		        protected void onProgressUpdate(String... progress) {
		   		
		   	}
		  }			
		  
		  
 }
