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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import android.telephony.TelephonyManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.MapFragment;
import com.maangalabs.transitnow.FavFragment.LongOperation1;


@SuppressLint("NewApi") 
public class MainActivity extends FragmentActivity
{
    int height,width;
    int yar[],xar[];
    String dar[];
    int maxcount=250;
    CalFragment cal;
	LinearLayout l;
    public static int scx=0;
	public double lati[];
	public int timer;
	LatLng point1[];
	ArrayList<LinearLayout> layoutList;
	ArrayList<TextView> textList1;
	ArrayList<TextView> textList2;
	Location loc;
	LocationManager locationManager;
	static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	static final long MINIMUM_TIME_BETWEEN_UPDATES = 500;
	   String stop1,stop1name,stops1[];
	    String stop2,stop2name,stops2[];
	    ArrayList<String> worldlist;
	public double longi[],latit[],longit[];
	 public String s1[],s2[],stopname[];
	public SharedPreferences preferences;
	MapView mapView;
	//LocationManager  locationManager;
	//Location loc;
	int p=0;
	  int pos;
	String names1="no stops!";
	GoogleMap map;
	RoutesAdapter adapter;
	private ViewPager viewPager,viewPager1;
    private TabsPagerAdapter mAdapter;
    ImageView my_image;
    String names;
    ViewFlipper viewFlipper;
    public static final int progress_bar_type = 0; 
    private ProgressDialog pDialog;
    TextView t9;
    private GoogleMap googleMap;
  //  static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 3; // in Meters
	//static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
    @Override
    public void onBackPressed()
   {
    
    	android.os.Process.killProcess(android.os.Process.myPid());     	
   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
  
   
   @Override
   public boolean onMenuItemSelected(int featureId, MenuItem item) {

       int itemId = item.getItemId();
       switch (itemId) {
       case R.id.action_cart:
    	 //  Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_SHORT).show();
    	   if(item.getTitle().toString().equals("stops")){
    	   if (viewFlipper.getDisplayedChild() == 1)
              break;
          
           // set the required Animation type to ViewFlipper
           // The Next screen will come in form Left and current Screen will go OUT from Right
           viewFlipper.setInAnimation(this, R.anim.in_from_right);
           viewFlipper.setOutAnimation(this, R.anim.out_to_left);
           // Show the next Screen
           viewFlipper.showNext(); 
           item.setTitle("map");
          // new StopSetter().execute(" ");

           // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
    	   }
    	   else
    	   {
    		   if (viewFlipper.getDisplayedChild() == 0)
    	              break;
    	          
    	           // set the required Animation type to ViewFlipper
    	           // The Next screen will come in form Left and current Screen will go OUT from Right
    	           viewFlipper.setInAnimation(this, R.anim.in_from_left);
    	           viewFlipper.setOutAnimation(this, R.anim.out_to_right);
    	           // Show the next Screen
    	           viewFlipper.showNext(); 
    	           item.setTitle("stops");
    	   }
           break;
           

       }

       return true;
   }
   public void addMarker()
   {
	   float zoom=googleMap.getCameraPosition().zoom;
	   CameraPosition cameraPosition = new CameraPosition.Builder().target(
	 			new LatLng(loc.getLatitude(),loc.getLongitude())).zoom(zoom).build();
	   googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	/*   MarkerOptions options = new MarkerOptions();
		
		
		options.position(new LatLng(loc.getLatitude(),loc.getLongitude()));
	


	googleMap.addMarker(options);*/
	//addMarkers();
   }
   protected void showCurrentLocation() {
   	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
   	if (location != null) {
   		loc=location;
   		addMarker();
	
   	}
   }  
   private class MyLocationListener implements LocationListener {
   	
   	public void onLocationChanged(Location location) {
           	
   		   		showCurrentLocation();
           	           
           	          
   	}
   	public void onStatusChanged(String s, int i, Bundle b) {
   	}
   	public void onProviderDisabled(String s) {
          }
   	public void onProviderEnabled(String s) {
     }
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    
    l=(LinearLayout)findViewById(R.id.scrollhold);
   cal=new CalFragment(MainActivity.this);
    	//n
    	  layoutList=new ArrayList<LinearLayout>();
    	  textList1=new ArrayList<TextView>();
    	  textList2=new ArrayList<TextView>();
    	  timer=-2;
    	//ColorDrawable cd = new ColorDrawable(0xD7DF0100);
    	//getActionBar().setBackgroundDrawable(cd);
    	
    	/*
    	 * setting the viewpager up, now it points to 0th element.
    	 */
        viewPager = (ViewPager) findViewById(R.id.pager1);
        
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
      
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper13);
        
    	preferences = PreferenceManager.getDefaultSharedPreferences(this);
	 	int flag = preferences.getInt("f", 0);
	 

	 	if(flag==0)
	  	{
		  //Toast.makeText(getActivity(), "gugi",Toast.LENGTH_SHORT).show();
		  
	 		
	  			
		  //Fragment fr=(Fragment)v.findViewById(R.id.map)			      
	  	}
	  	else
	  	{
		 
	  		

	  		TextView t=(TextView)findViewById(R.id.textView10);
    		preferences = PreferenceManager.getDefaultSharedPreferences(this);
    		String unames = preferences.getString("nameuser", "Jane Doe");
    		t.setText(unames);
    		Typeface tf=Typeface.createFromAsset(getAssets(),"font/font1.ttf");
    		t.setTypeface(tf);
	  	}
        /*
         * Initialize the map, and set it point using camera update...
         */
      
    //    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	  /*  locationManager.requestLocationUpdates(
				                 LocationManager.GPS_PROVIDER,
				 
				                 MINIMUM_TIME_BETWEEN_UPDATES,
				 
				                 MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
				 
				                 new MyLocationListener()
				 
				         );*/
        try {
            	initilizeMap();
            	if(googleMap!=null)
            	{
            		googleMap.setMyLocationEnabled(true);
            		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            	
            		CameraPosition cameraPosition = new CameraPosition.Builder().target(
            				new LatLng(8.551006600000000000, 76.910830700000020000)).zoom(12).build();
         
            		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            		googleMap.setMyLocationEnabled(true);
                      	
            	}
            	
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
     			                 LocationManager.GPS_PROVIDER,
     			 
     			                 MINIMUM_TIME_BETWEEN_UPDATES,
     			 
     			                 MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
     			 
     			                 new MyLocationListener()
     			 
     			         );
        

    	/*
    	 * Check whether customer is logged in or not using shared preferences.
    	 */
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	int flag1 = preferences.getInt("f", 0);
    	
    	if(flag1!=0)
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
    		}*/
    	/*	MarkerOptions options = new MarkerOptions();
    		
	
    			options.position(new LatLng(lati[i],longi[i]));
    		


    		googleMap.addMarker(options);
    		addMarkers();*/
    	//	db.close(); 
    		
    		
    	//	new GetDbVersion().execute("");
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
       
     new Authenticate().execute(" ");
    
    }
    public void subs1(View v)
    {
    	
    	   viewPager.setCurrentItem(1);
    }

    public void back(View v)
    {
    	
    	   viewPager.setCurrentItem(0);
    }
    public void rmap(View v)

    {
    	RelativeLayout rl=(RelativeLayout)findViewById(R.id.mapL);
    	if(rl.getLayoutParams().height==LayoutParams.WRAP_CONTENT)
    	{    	    rl.getLayoutParams().height = 200;
    	 
    	}
    	else
    	{
    		 rl.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
    	}
    	viewFlipper.showNext(); 
    	    viewFlipper.showNext(); 
    	    
    	
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
    	
    	if(latit!=null){
         String waypoints = "waypoints=optimize:true|";
         if(latit.length>=1)
             	waypoints=waypoints+latit[0]+","+longit[0];
         
         for(int i=0;i<latit.length;i++)
         {
           waypoints= waypoints+"|" + "|" + latit[i] + "," + longit[i];
         }

         String sensor = "sensor=false";
         String params = waypoints + "&" + sensor;
         String output = "json";
         String url = "https://maps.googleapis.com/maps/api/directions/"
            + output + "?" + params;
        
         return url;
    	}
    	return "";
    }
    
    /*
     * Add markers at all the stops in the route..
     */
    private void addMarkers() {
    /*	 DataBaseHelper db = new DataBaseHelper(MainActivity.this);
    	 double[]  lati2=new double[db.getContactsCount()];
         double[] longi2=new double[db.getContactsCount()];
         lati2=db.getLati();
         longi2=db.getLongi();*/
    	
         if (googleMap != null) {
        //	 ReadTask downloadTask = new ReadTask();
        //	 String url = getMapsApiDirectionsUrl();
        	// downloadTask.execute(url);
        	 //if(db.getContactsCount()!=0)
        	 //{
        	
        		 CameraPosition cameraPosition = new CameraPosition.Builder().target(
        	 			new LatLng(loc.getLatitude(),loc.getLongitude())).zoom(12).build();
           
        		 googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        	// }
        	
        		 googleMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude()))
            			.title("You"));
        	 
         
         
    }
    //db.close();
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
    
    
   //given the vehicle number this asynctask will return the route id.
 
 
 
 
private class LongOperation extends AsyncTask<String, Void, String> {
	 String f=new String();
        @Override
        protected String doInBackground(String... params) {
        	try{
        	
        		/*
        		 * Login api call
        		 */
        			Log.d("task","adas");
        			//f="error";
        		
                	StringBuilder builder = new StringBuilder();
                	HttpClient client = new DefaultHttpClient();
	            	   
	            	   
        	    	HttpGet httpGet = new HttpGet("http://54.89.248.120:1337/vehicle/findtrip/"+params[0]);
        	   // 	 Toast.makeText(getApplicationContext(), "AS", Toast.LENGTH_SHORT).show();
        	    
        	    		HttpResponse response = client.execute(httpGet);
        	    		StatusLine statusLine = response.getStatusLine();
        	    		int statusCode = statusLine.getStatusCode();
        	    		Log.d("tag1","http://192.168.0.181:1337/asset/findAssetDetails/XEN49WAN4LC");
        	    		if (statusCode == 200) {
        	    			HttpEntity entity = response.getEntity();
        	    			InputStream content = entity.getContent();
        	    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        	    			String line;
        	    			while ((line = reader.readLine()) != null) {
        	    				builder.append(line);
        	              	}
        	    		
            	      }
            	  
               	    Log.d("checker",builder.toString());
            	    JSONArray jsonObj = new JSONArray(builder.toString());
            	    JSONObject jo=jsonObj.getJSONObject(0);
        	       f=jo.getString("trip_id");
        	       f=f.replaceAll("\"","");
        	       Log.d("tag1",f);
        	}
            catch(Exception e)
            {
            	
           	}
       		return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
        	if(result.equals("Executed"))
        	{
        		Toast.makeText(getApplicationContext(), f, Toast.LENGTH_SHORT).show();
        		new GetLatLong().execute(f);
        	}
        	else
        		Toast.makeText(getApplicationContext(), "sorry", Toast.LENGTH_SHORT).show();
        	
//           	if(p!=0)
//        	{
//           		/*
//           		 * making the second viewflipper 
//           		 */
//        		
//       /*		 	
//       		 	Routes weather_data[] = new Routes[]
//	                      {
//	                          new Routes("Pattom","1:30 pm"),
//	                          
//	                      };
//       		 	RoutesAdapter adapter;
//	                     
//       		 	adapter = new RoutesAdapter(MainActivity.this,
//	                              R.layout.list_item_row_home, weather_data);
//	                     
//	                    // adapter.notifyDataSetChanged();
//	              
//	                     
//       		 	ListView listView1 = (ListView)findViewById(R.id.listView1);
//	                      
//	                  //  adapter.notifyDataSetChanged();
//       		 	listView1.setAdapter(adapter);*/
//       		 	
//       		 	new GetDbVersion().execute("");
//       		 	
//       		 /*
//       	    	 * on Login Display the route with the databse , move this code to LongOperation's postexecute.
//       	    	 */
//       	    	DataBaseHelper db = new DataBaseHelper(MainActivity.this);
//       	    	double lati1[]={8.502072,8.583205};
//       	    	double longi1[]={76.949892,76.878550};
//       	    	for(int h=0;h<lati1.length;h++)
//       	    	{
//       	    		db.addContact(lati1[h],longi1[h]);        
//       	    	}
//       	        Log.d("Name: ", db.getContactsCount()+"");
//       	        lati=new double[db.getContactsCount()];
//       	        longi=new double[db.getContactsCount()];
//       	        lati=db.getLati();
//       	        longi=db.getLongi();
//       	        MarkerOptions options = new MarkerOptions();
//       	        for(int i=0;i<db.getContactsCount();i++)
//       	        {
//       	        	options.position(new LatLng(lati[i],longi[i]));
//       	        }
//       	      
//       	      
//       	       googleMap.addMarker(options);
//       	       addMarkers();
//       	       db.close();
//       	       this.cancel(true);
//     	   	}
//        	/*
//        	 * Avoid this code once api is set
//        	 */
//        	else
//        	{
//        		
//        		//comment this once api is set
//            	
//            	EditText t=(EditText)findViewById(R.id.textView1);
//        		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        		SharedPreferences.Editor editor = preferences.edit();
//        		editor.putInt("f",Integer.parseInt(t.getText().toString()));
//        		
//        		editor.commit();
//
//        	
//        		
//        	   		
//        	   	new GetDbVersion().execute("");
//        /*	   	Routes weather_data[] = new Routes[]
// 	            {
//        	   			new Routes("Pattom","1:30 pm"),
// 	            };
//     	        RoutesAdapter adapter;
// 	                     
//     	        adapter = new RoutesAdapter(MainActivity.this,
// 	                              R.layout.list_item_row_home, weather_data);
// 	                     
//     	        // adapter.notifyDataSetChanged();
// 	              
// 	                     
//     	        ListView listView1 = (ListView)findViewById(R.id.listView1);
// 	                      
// 	                  //  adapter.notifyDataSetChanged();
//     	        listView1.setAdapter(adapter);
//        	   	*/
//          		 /*
//          	    	 * on Login Display the route with the databse , move this code to LongOperation's postexecute.
//          	    	 */
//          	    DataBaseHelper db = new DataBaseHelper(MainActivity.this);
//          	    double lati1[]={8.502072,8.583205};
//          	    double longi1[]={76.949892,76.878550};
//          	    for(int h=0;h<lati1.length;h++)
//          	    {
//          	    		db.addContact(lati1[h],longi1[h]);        
//          	    }
//          	    Log.d("Name: ", db.getContactsCount()+"");
//          	    lati=new double[db.getContactsCount()];
//          	    longi=new double[db.getContactsCount()];
//          	    lati=db.getLati();
//          	    longi=db.getLongi();
//          	    MarkerOptions options = new MarkerOptions();
//          	    for(int i=0;i<db.getContactsCount();i++)
//          	    {
//          	       	options.position(new LatLng(lati[i],longi[i]));
//          	    }
//          	      
//          	      
//          	    googleMap.addMarker(options);
//          	    addMarkers();
//          	    db.close();
          	         	   		
//      	   		this.cancel(true);
//        		
//        	}
        
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	


//Initial email authenticatio returns the vehicle number

private class Authenticate extends AsyncTask<String, Void, String> {
	 String f=new String();
        @Override
        protected String doInBackground(String... params) {
        	try{
        	
        		preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        		String emailid = preferences.getString("emailuser", "Jane Doe");
        		
                	StringBuilder builder = new StringBuilder();
                	HttpClient client = new DefaultHttpClient();
	            	   
	            	   
        	    	HttpGet httpGet = new HttpGet("http://54.89.248.120:1337/userlogin/findvehicle/"+emailid);
        	   // 	 Toast.makeText(getApplicationContext(), "AS", Toast.LENGTH_SHORT).show();
        	    
        	    		HttpResponse response = client.execute(httpGet);
        	    		StatusLine statusLine = response.getStatusLine();
        	    		int statusCode = statusLine.getStatusCode();
        	    		Log.d("tag1","fj   "+emailid);
        	    		if (statusCode == 200) {
        	    			HttpEntity entity = response.getEntity();
        	    			InputStream content = entity.getContent();
        	    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        	    			String line;
        	    			while ((line = reader.readLine()) != null) {
        	    				builder.append(line);
        	              	}
        	    		
            	      }
            	  
               	    Log.d("checker",builder.toString());
            	    JSONArray jsonObj = new JSONArray(builder.toString());
            	    JSONObject jo=jsonObj.getJSONObject(0);
        	       f=jo.getString("vehicle_id");
        	       f=f.replaceAll("\"","");
        	       Log.d("tag1",f);
        	}
            catch(Exception e)
            {
            	
           	}
       		return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
        	if(result.equals("Executed"))
        	{
        		Toast.makeText(getApplicationContext(), f, Toast.LENGTH_SHORT).show();
        		TextView t=(TextView)findViewById(R.id.textView5);
        		
        		t.setText("Vehicle Number : "+f);
        		new LongOperation().execute(f);
        	}
        	else
        	{
        		Toast.makeText(getApplicationContext(), "failed to validate", Toast.LENGTH_SHORT).show();
        		
        		TextView t=(TextView)findViewById(R.id.textView5);
        		t.setText("Vehicle Number : Not assigned");
        	}
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
	
	










//Get the lat and long s in the trip.




private class GetLatLong extends AsyncTask<String, Void, String> {
	 String f=new String();
       @Override
       protected String doInBackground(String... params) {
       	try{
       		Log.d("tag1","cjkvkgk");
       		
               	StringBuilder builder = new StringBuilder();
               	HttpClient client = new DefaultHttpClient();
	            	   
	            	   
       	    	HttpGet httpGet = new HttpGet("http://54.89.248.120:1337/stops/findstops/"+params[0]);
       	   // 	 Toast.makeText(getApplicationContext(), "AS", Toast.LENGTH_SHORT).show();
       	    
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
       	              	}
       	    		
           	      }
           	  
              	    Log.d("checker",builder.toString());
           	    JSONArray jsonObj = new JSONArray(builder.toString());
           	    latit=new double[jsonObj.length()];
           	 longit=new double[jsonObj.length()];
           	 stopname=new String[jsonObj.length()];
           	    for(int i=0;i<jsonObj.length();i++)
           	    {
           	    JSONObject jo=jsonObj.getJSONObject(i);
       	       latit[i]=jo.getDouble("stop_lat");
       	       longit[i]=jo.getDouble("stop_lng");
       	       stopname[i]=jo.getString("stop_name");
       	       stopname[i]=stopname[i].replace("\"","");
       	    
       	     
           	    }
           	
       	       Log.d("tag1",f);
       	}
           catch(Exception e)
           {
           	
          	}
      		return "Executed";
       }
       @Override
       protected void onPostExecute(String result) {
       	if(result.equals("Executed"))
       	{
       	String url=getMapsApiDirectionsUrl();
       	new ReadTask().execute(url);
       	}
       	else
       	{
       		Toast.makeText(getApplicationContext(), "failed to validate", Toast.LENGTH_SHORT).show();
       		
       		TextView t=(TextView)findViewById(R.id.textView5);
       		t.setText("Vehicle Number : Not assigned");
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
		 if(routes!=null){
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
		    		  polyLineOptions.color(Color.RED);
		    	  }
		      }
		      if(points!=null)
		    	  googleMap.addPolyline(polyLineOptions);
		  	CameraPosition cameraPosition = new CameraPosition.Builder().target(
    				new LatLng(latit[0],longit[0])).zoom(12).build();
 
    		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    		
    		new StopSetter().execute(" ");
    		 //new DownloadJSON().execute();
		    }
		    }
		    }}
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
		   * on click of goback text
		   */
		  
	/*	  public void goBack(View v)
		    {
			  if (FavFragment.viewFlipper.getDisplayedChild() == 1)
	                return;
	            // set the required Animation type to ViewFlipper
	            // The Next screen will come in form Right and current Screen will go OUT from Left
	            FavFragment.viewFlipper.setInAnimation(MainActivity.this, R.anim.in_from_right);
	            FavFragment.viewFlipper.setOutAnimation(MainActivity.this, R.anim.out_to_left);
	            // Show The Previous Screen
	           FavFragment.viewFlipper.showPrevious();
		    }*/
		  
		  
		  /*
		   * on click of button show near..
		   */
		
		  
		  
	/*	  
		  public void shownear(View v)
		    {
			 
			  /*
			   * enabling location..
			   */
		/*	 locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
			    if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
			        AlertDialog.Builder builder = new AlertDialog.Builder(this);
			        builder.setTitle("No GPS");  // GPS not found
			        builder.setMessage("Location Not Enabled"); // Want to enable?
			        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialogInterface, int i) {
			                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			            }
			        });
			        builder.setNegativeButton("no", null);
			        builder.create().show();
			        return;
			    } 
			    
			   
			  
		    	ListView l=(ListView)findViewById(R.id.listView2);
		    	l.setVisibility(View.VISIBLE);
		    	if(loc!=null)
		    	{
		    		if(haveNetworkConnection())
		        		 new LongOperation1().execute(" ");
		    		else
		    			Toast.makeText(getApplicationContext(), "Not connected to network!",Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "fetching location please wait",Toast.LENGTH_SHORT).show();
		    	}
		    	
		    }
		  
		  protected void showCurrentLocation() {
	        	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        	if (location != null) {
	        		loc=location;
	        		
			
	        	}
	        }  
		  private class MyLocationListener implements LocationListener {
          	
	        	public void onLocationChanged(Location location) {
	                	
	        		   		showCurrentLocation();
	                	           
	                	          
	        	}
	        	public void onStatusChanged(String s, int i, Bundle b) {
	        	}
	        	public void onProviderDisabled(String s) {
	               }
	        	public void onProviderEnabled(String s) {
	          }
	        }
		  */
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
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  /*
		   * 
		   * TO get the list of nearby stops..
		   */
		  
		  
		/*  
		  

			public class LongOperation1 extends AsyncTask<String, Void, String> {

		        @Override
		        protected String doInBackground(String... params) {
		        	try{
		        	if(loc!=null){
		        		 SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		       		  int flag = preferences.getInt("f", 0);
		        	   		             		
		            	
		                	
		            	  
		            	        
		            	        
		       		 JSONArray jsonA;
		       		
						try {
							jsonA = JSONfunctions.getJSONfromURL("http://192.168.0.129:1010/nearest_stops?lat="+loc.getLatitude()+"&lon="+loc.getLongitude()+"&limit=10");
							System.out.println("http://192.168.0.129:1010/nearest_stops?lat="+loc.getLatitude()+"&lon="+loc.getLongitude()+"&limit=10");
							s1=new String[jsonA.length()];
							s2=new String[jsonA.length()];
							for(int i=0;i<jsonA.length();i++){
								
								 try {
			            		    	
			            		    	JSONObject e2 = jsonA.getJSONObject(i);
			            		    	s1[i]=e2.getString("stop_name");
			            		    	s2[i]=e2.getString("stop_id");
								 }
								 catch(Exception e)
								 {
									 System.out.print("sorry");
								 }
							}
		
						}
						catch(Exception e)
						{
							 System.out.print("sorry1");
						}
		            	    
		              //  deviceName.remove(i);
		            	//deviceName.add(i,"Dell Inspiron");
		                	
		        	     
		                
		            	
		        	}
		            	
		            	
		        	
		        	}
		        	catch(Exception e)
		        	{
		        		
		        	}
		        	       	
		            return "Executed";
		        }
		       
		        
		        @Override
		        protected void onPostExecute(String result) {
		            // txt.setText(result);
		            // might want to change "executed" for the returned string passed
		            // into onPostExecute() but that is upto you
		        	
		        		
		       		          	
		    		 
		    		 /*
		    		  * set the cab details here 
		    		  * using some for loop
		    		  */
		       /* 	
 	  	    		Toast.makeText(getApplicationContext(), "finished",Toast.LENGTH_SHORT).show();
 	  	    		
		        	  Cabs weather_data[] = new Cabs[s1.length];
		        	  for(int i=0;i<s1.length;i++)
	        	 		{
	        	 			weather_data[i]=new Cabs(s1[i]);
	        	 		}
		                     
		         adapter = new CabAdapter(MainActivity.this,
		                              R.layout.listview_item_row, weather_data);
		                     
		                    // adapter.notifyDataSetChanged();
		              
		                     
		         ListView listView1 = (ListView)findViewById(R.id.listView2);
		                      
		                  //  adapter.notifyDataSetChanged();
		         listView1.setOnItemClickListener(new OnItemClickListener() {
		             @Override
		             public void onItemClick(AdapterView<?> parent, View view, int position,
		                     long id) {
		            	 if (FavFragment.viewFlipper.getDisplayedChild() == 1)
		    	                return;
		    	            // set the required Animation type to ViewFlipper
		    	            // The Next screen will come in form Right and current Screen will go OUT from Left
		    	            FavFragment.viewFlipper.setInAnimation(MainActivity.this, R.anim.in_from_right);
		    	            FavFragment.viewFlipper.setOutAnimation(MainActivity.this, R.anim.out_to_left);
		    	            // Show The Previous Screen
		    	           FavFragment.viewFlipper.showNext();
		             }
		         });
		     		this.cancel(true);
 	  	    		
		        }

		        @Override
		        protected void onPreExecute() {
		        	 Cabs weather_data[] = new Cabs[]
		                      {
		                          new Cabs("Loading.."),
		                          
		                      };
		                     
		         adapter = new CabAdapter(MainActivity.this,
		                              R.layout.listview_item_row, weather_data);
		                     
		                    // adapter.notifyDataSetChanged();
		              
		                     
		         ListView listView1 = (ListView)findViewById(R.id.listView2);
		                      
		                  //  adapter.notifyDataSetChanged();
		         listView1.setAdapter(adapter);
		         
		        
		        	
		        }

		        @Override
		        protected void onProgressUpdate(Void... values) {
		        	
		        }
		    }
		    */
		  
		  
		  
		  /*
		   * 
		   * sets the list of stops..
		   */
		    
		  
		  public class StopSetter extends AsyncTask<String, Void, String> {

		        @Override
		        protected String doInBackground(String... params) {
		        	try{
		        	
		        	
		        	}
		        	catch(Exception e)
		        	{
		        		
		        	}
		        	       	
		            return "Executed";
		        }
		       
		        
		        @Override
		        protected void onPostExecute(String result) {
		            // txt.setText(result);
		            // might want to change "executed" for the returned string passed
		            // into onPostExecute() but that is upto you
		        	
		        		
		       		          	
		    	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	if(stopname!=null)
		        	{
		        		
		        		
		        		
		        		
		            	l.addView(cal);
		            	
		            	final ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView1);
			        	
			        scrollView.setBackgroundColor(Color.WHITE);
			     
			        	
			        	//scrollView.smoothScrollTo(4000, 30);
			        //	setContentView(scrollView);
			        	scrollView.post(new Runnable() { 
			                public void run() { 
			                     scrollView.smoothScrollTo(0, scx);
			                } 
			        });
		        	
//		        	for(int i=0;i<stopname.length;i++)
//		        	{
//		        	
//		       	 LinearLayout LL = new LinearLayout(MainActivity.this);
//			      TextView tt1=new TextView(MainActivity.this);
//			     tt1.setTextSize(30);
//			    
//			      final TextView tt2=new TextView(MainActivity.this);
//			      tt1.setText(stopname[i]);
//			      tt2.setText("Waiting..");
//			       LL.setOrientation(LinearLayout.VERTICAL);
//			       LL.setPadding(20, 0, 0, 0);
//			       
//			       LayoutParams LLParams = new LayoutParams(LayoutParams.FILL_PARENT,200);
//			       
//			       LinearLayout.LayoutParams ladderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//
//			       LinearLayout.LayoutParams dummyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//			       tt1.setLayoutParams(ladderParams);
//			       
//			       tt2.setLayoutParams(dummyParams);
//			       tt2.setPadding(-1,0, 0, 0);
//			      
//			      
//			       LL.setLayoutParams(LLParams);
//			       LL.addView(tt1);
//			       LL.addView(tt2);
//			       layoutList.add(i, LL);
//			       textList1.add(i, tt1);
//			       textList2.add(i, tt2);
//			       
//			       
//			       
//			       for(LinearLayout l1:layoutList)
//			       {
//			    	   ((LinearLayout) findViewById(R.id.scrollhold)).removeView(l1);   
//			    	   ((LinearLayout) findViewById(R.id.scrollhold)).addView(l1);
//			       }
//			        
//			      
//			        
//			        
//		        
//			        
//			        
//			        
//			        
//			        
//			        
//			        
//		        }
//			        
//			        
//		        	
		        	
		        	
		        	
		        	
		        	
		        	}
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	

		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	CountDownTimer t = new CountDownTimer( Long.MAX_VALUE , 2000) {

		        	    // This is called every interval. (Every 10 seconds in this example)
		        		    				public void onTick(long millisUntilFinished) {
		        	    

		        		    					if(timer<stopname.length)
		        		    					{
		        		    						if(timer>=0){
		        		    							
		        		    						    timer++;
		        		    							
		        		    							l.removeView(cal);
		        		    							cal=new CalFragment(MainActivity.this);
		        		    							l.addView(cal);
		        		    						
		        		    							final ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView1);
		        		    				        	
		        		    				        	scrollView.setBackgroundColor(Color.WHITE);
		        		    				     
		        		    				        	
		        		    				        	//scrollView.smoothScrollTo(4000, 30);
		        		    				      //  	setContentView(scrollView);
		        		    				        	scrollView.post(new Runnable() { 
		        		    				                public void run() { 
		        		    				                     scrollView.smoothScrollTo(0, scx);
		        		    				                } 
		        		    				        });
		        		    							
//		        		    						Toast.makeText(getApplicationContext(), timer+" ", Toast.LENGTH_SHORT).show();
//		        		    					 LinearLayout LL = layoutList.get(timer);
//		        		    					 ((LinearLayout) findViewById(R.id.scrollhold)).removeView(LL); 
//		        		    				      TextView tt1=textList1.get(timer);
//		        		    				   
//		        		    				    
//		        		    				      final TextView tt2=textList2.get(timer);
//		        		    				   
//		        		    				      
//		        		    				      
//		        		    				      LL.setBackgroundColor(Color.GREEN);
//		        		    				      LL.removeView(tt2);
//		        		    				      tt2.setText("Reached");
//		        		    				    
//		        		    				      LL.addView(tt2);
//		        		    				      
//		        		    				      layoutList.remove(timer);
//		        		    				       textList1.remove(timer);
//		        		    				       textList2.remove(timer);
//		        		    				       layoutList.add(timer, LL);
//		        		    				       textList1.add(timer, tt1);
//		        		    				       textList2.add(timer, tt2);
//		        		    				       
//		        		    				       for(LinearLayout l1:layoutList)
//		        		    				       {
//		        		    				    	   ((LinearLayout) findViewById(R.id.scrollhold)).removeView(l1); 
//		        		    				    	   ((LinearLayout) findViewById(R.id.scrollhold)).addView(l1);
//		        		    				       }
		        		    				   
		        		    						}else
		        		    				       timer++;
		        		    				}
		        		    				}
		        		    				public void onFinish() {
		        	        
		        		    					start();
		        		    				}
		        		    		}.start();

		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		        	
		    		 /*
		    		  * set the cab details here 
		    		  * using some for loop
		    		  */
		   	
//	  	    		if(stopname!=null)
//	  	    		{
//	  	    			Routes weather_data[]=new Routes[stopname.length];
//	  	    		for(int i=0;i<stopname.length;i++)
//	  	    		{
//		        	 weather_data[i] = 
//		        			  new Routes(stopname[i],(i+1)+"");
//	  	    		}
//		        	 
//		        	
//		                     
//		         adapter = new RoutesAdapter(MainActivity.this,
//		                              R.layout.list_item_row_home, weather_data);
//		                     
//		                    // adapter.notifyDataSetChanged();
//		              
//		                     
//		         ListView listView1 = (ListView)findViewById(R.id.listView12);
//		                      
//		                  //  adapter.notifyDataSetChanged();
//		         listView1.setAdapter(adapter);
//	  	    		}	
		         this.cancel(true);
	  	    		
		        }

		        @Override
		        protected void onPreExecute() {
//		        	Routes weather_data[] = new Routes[]
//		                      {
//		                          new Routes("Loading..",""),
//		                          
//		                      };
//		                     
//		         adapter = new RoutesAdapter(MainActivity.this,
//		                              R.layout.list_item_row_home, weather_data);
//		                     
//		                    // adapter.notifyDataSetChanged();
//		              
//		                     
//		         ListView listView1 = (ListView)findViewById(R.id.listView12);
//		                      
//		                  //  adapter.notifyDataSetChanged();
//		         listView1.setAdapter(adapter);
		         
		        
		        	
		        }

		        @Override
		        protected void onProgressUpdate(Void... values) {
		        	
		        }
		    }
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		
		  
		  private class DownloadJSON extends AsyncTask<Void, Void, Void> {
			  
		        @Override
		        protected Void doInBackground(Void... params) {
		            // Locate the WorldPopulation Class   
		           
		            // Create an array to populate the spinner
		            worldlist = new ArrayList<String>();
		            
		            // JSON file URL address
		            JSONArray jsonA;
					try {
						jsonA = JSONfunctions.getJSONfromURL("http://192.168.0.129:1010/all_stops");
						s1=new String[jsonA.length()];
		            	for(int i=0;i<jsonA.length();i++){
		            		
		            		JSONObject e = jsonA.getJSONObject(i);
		            		s1[i]=e.getString("stop_id");
		                    // Populate spinner with country names
		                    worldlist.add((e.getString("stop")));
		            	}
		              
		           
					} catch (ClientProtocolException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		           
		           
		             
		            return null;
		        }
		 
		        @Override
		        protected void onPostExecute(Void args) {
		            // Locate the spinner in activity_main.xml
		            Spinner mySpinner = (Spinner) findViewById(R.id.fromDrop);
		 
		            // Spinner adapter
		            mySpinner
		                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
		                            android.R.layout.simple_spinner_dropdown_item,
		                            worldlist));
		 
		            // Spinner on item click listener
		            mySpinner
		                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		 
		                        @Override
		                        public void onItemSelected(AdapterView<?> arg0,
		                                View arg1, int position, long arg3) {
		                            // TODO Auto-generated method stub
		                            // Locate the textviews in activity_main.xml
		                        	
		                        	pos=position;
		                        	stop1=s1[pos];
		                        	stop1name=worldlist.get(pos);
		                        	new DownloadJSON1().execute();
		                        	
		                            
		                        }
		 
		                        @Override
		                        public void onNothingSelected(AdapterView<?> arg0) {
		                            // TODO Auto-generated method stub
		                        }
		                    });
		        }
		    }
		  
		  
		  
		  
		  
		  
		  
		  
		  
	
		  
		  
		  

		  
		  
		  
		  
		  
		  private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {
			  ArrayList<String> worldlist1;
			  
		        @Override
		        protected Void doInBackground(Void... params) {
		            // Locate the WorldPopulation Class   
		           
		            // Create an array to populate the spinner
		           worldlist1 = new ArrayList<String>();
		            
		            // JSON file URL address
		            JSONArray jsonA;
					try {
						System.out.println(s1[pos]);
						jsonA = JSONfunctions.getJSONfromURL("http://192.168.0.129:1010/stops_from?stop_id="+s1[pos]);
						System.out.println(s1[pos]);
						s2=new String[jsonA.length()];
		            	for(int i=0;i<jsonA.length();i++){
		            		
		            		JSONObject e = jsonA.getJSONObject(i);
		            		
			            	s2[i]=e.getString("stop_id");
			                    // Populate spinner with country names
			                		            	
		                    // Populate spinner with country names
		                    worldlist1.add((e.getString("stop")));
		            	}
		              
		           
					} catch (ClientProtocolException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		           
		           
		             
		            return null;
		        }
		 
		        @Override
		        protected void onPostExecute(Void args) {
		            // Locate the spinner in activity_main.xml
		            Spinner mySpinner = (Spinner) findViewById(R.id.toDrop);
		 
		            // Spinner adapter
		            mySpinner
		                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
		                            android.R.layout.simple_spinner_dropdown_item,
		                            worldlist1));
		 
		            // Spinner on item click listener
		            mySpinner
		                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		 
		                        @Override
		                        public void onItemSelected(AdapterView<?> arg0,
		                                View arg1, int position, long arg3) {
		                            // TODO Auto-generated method stub
		                            // Locate the textviews in activity_main.xml
		                        	//new DownloadJSON1().execute();
		                        	stop2=s2[position];
		                        	stop2name=worldlist1.get(position);
		                            
		                        }
		 
		                        @Override
		                        public void onNothingSelected(AdapterView<?> arg0) {
		                            // TODO Auto-generated method stub
		                        }
		                    });
		        }
		    }
		  
		  
		  
		  
		  
		  
		  public class CalFragment extends View  {
			    
			    //public CalFragment(){}
			
			  //  private class DrawView extends View {
			        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			       
			        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint7 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint8 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint9 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint10 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint11= new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint12 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint5 = new Paint(Paint.ANTI_ALIAS_FLAG);
			        Paint paint3 = new Paint();
			        
			      //  public DrawView(Context context) {
			        public CalFragment(Context context) {
			            super(context);
			            yar=new int[250];
			            xar=new int[250];
			            dar=new String[250];
			            paint.setStyle(Paint.Style.STROKE); 
			            paint.setColor(Color.YELLOW);
			            paint.setStrokeWidth(8);
			            paint1.setStyle(Paint.Style.STROKE); 
			            paint1.setColor(Color.YELLOW);
			            paint1.setStrokeWidth(8);
			            paint7.setStyle(Paint.Style.FILL); 
			            paint7.setColor(Color.RED);
			            paint7.setARGB(255, 255, 0, 0);
			            paint8.setStyle(Paint.Style.FILL); 
			            paint8.setColor(Color.parseColor("#FFFF00"));
			            paint9.setStyle(Paint.Style.FILL); 
			            paint9.setColor(Color.parseColor("#3333CC"));
			            paint10.setStyle(Paint.Style.FILL); 
			            paint10.setColor(Color.parseColor("#99FF66"));
			            paint11.setStyle(Paint.Style.FILL); 
			            paint11.setColor(Color.parseColor("#999966"));
			            paint12.setStyle(Paint.Style.FILL); 
			            paint12.setColor(Color.parseColor("#FF9900"));
			           // paint2.setStrokeWidth(8);
			           // paint8.setARGB(255, 255, 0, 0);
			            paint5.setStyle(Paint.Style.FILL); 
			            paint5.setColor(Color.RED);
			           // paint2.setStrokeWidth(8);
			            paint5.setARGB(255, 0, 255, 0);
			            
			           
			            
			            
			            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			            Display display = wm.getDefaultDisplay();
			            Point size = new Point();
			            display.getSize(size);
			            
			            height = size.y;
			            width=size.y;

			            paint3.setColor(Color.WHITE);
			            paint3.setTextSize(22f);
			            paint3.setAntiAlias(true);
			            paint3.setTextAlign(Paint.Align.CENTER);
			            
			            paint9.setColor(Color.BLACK);
			            paint9.setTextSize(22f);
			            paint9.setAntiAlias(true);
			            paint9.setTextAlign(Paint.Align.CENTER);
			            

			        }
			        public CalFragment(Context context, AttributeSet attrs) {
			            super(context, attrs);
			            paint.setColor(Color.WHITE);
			           
			        }
			        public CalFragment(Context context, AttributeSet attrs, int defStyle) {
			            super(context, attrs, defStyle);
			            paint.setColor(Color.BLACK);
			        }

			        @Override
			        protected void onDraw(Canvas canvas) {
			        	super.onDraw(canvas);
			        	
			        	Date dateObj;
						try {
							
				        	 for(int i=0;i<stopname.length;i++)
				                 
				        	{
				        		
				        		
				        		 
				        		
				        		 
				        		 Rect bounds = new Rect();
					            	paint3.getTextBounds(stopname[i], 0, stopname[i].length(), bounds);
					            	
					            	
					            	
					            	
				        		if(i!=0)
				        		{
				            	if(i%2==0)
				            canvas.drawLine(canvas.getWidth()/2, 400*i+100,canvas.getWidth()/2 , 400*(i+1), paint);
				            	else
				            		 canvas.drawLine(canvas.getWidth()/2, 400*i+100,canvas.getWidth()/2 , 400*(i+1), paint1);
				        		}
//				            	if(newDateStr.equals(cdate))
//				            	{
//				            		canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint5);
//				            		scx=400*(i+1)-height/2;
//				            	}
//				            	else
////				            	{
//				            		if(AllVar.ccord1[i].equals("North"))
//				            	canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint7);
//				            		else if(AllVar.ccord1[i].equals("South"))
//				    	            	canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint8);
//				            		else if(AllVar.ccord1[i].equals("Central"))
//				    	            	canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint9);
//				            		else if(AllVar.ccord1[i].equals("North Central"))
//				    	            	canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint10);
//				            		else if(AllVar.ccord1[i].equals("Eastern"))
//				    	            	canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint11);
//				            		else if(AllVar.ccord1[i].equals("South Central"))
//				    	            	canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint12);
//				            		else 
//				            			canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint8);
//				            		
				            		if(i<timer)
				            		{
				            			
							            	paint9.getTextBounds("Left", 0, "Left".length(), bounds);
				            			canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint11);
				            			canvas.drawText("Left", 3*canvas.getWidth()/4, 400*(i+1), paint9);
				            		}
				            		else 
				            			if(i==timer)
					            	{
				            				paint9.getTextBounds("Reached", 0, "Reached".length(), bounds);
				            				canvas.drawText("Reached", 3*canvas.getWidth()/4, 400*(i+1), paint9);
					            		canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint5);
					            		
					            		scx=400*(i+1)-height/2;
					            	}
				        		else
				        			
				        		{
				        			paint9.getTextBounds("Waiting", 0, "Waiting".length(), bounds);
				        			canvas.drawText("Waiting", 3*canvas.getWidth()/4, 400*(i+1), paint9);
				        			canvas.drawCircle(canvas.getWidth()/2, 400*(i+1), 100f, paint7);
				        	}
				            		
				            		
//				            	}
				            	 canvas.drawText(stopname[i], canvas.getWidth()/2, 400*(i+1), paint3);
				            	 yar[i]=400*(i+1);
				            	 xar[i]=canvas.getWidth()/2;
//				            	 dar[i]=newDateStr;
				        		 }
				            
				           
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
			        	 
			            
			           
			           
			           
			        }

			        @Override 
			        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
			            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			            int parentHeight = stopname.length*500;
			            this.setMeasuredDimension(parentWidth, parentHeight);
			        }
			        private boolean inCircle(float x, float y, float circleCenterX, float circleCenterY, float circleRadius) {
			            double dx = Math.pow(x - circleCenterX, 2);
			            double dy = Math.pow(y - circleCenterY, 2);

			            if ((dx + dy) < Math.pow(circleRadius, 2)) {
			                return true;
			            } else {
			                return false;
			            }
			      
			  /*  @Override
			    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			            Bundle savedInstanceState) {
			    	 View rootView = inflater.inflate(R.layout.fragment_cal, container, false);
			    	
			    	
			    	return new DrawView(this.getActivity());
			       
			          
			        
			    }*/
			//}
		    
			        }
		  }
}
