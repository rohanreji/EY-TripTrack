
package com.maangalabs.transitnow;


import java.io.IOException;
import java.util.List;
import java.util.Locale;












import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


@SuppressLint("NewApi") public class HomeFragment extends Fragment implements onRefreshListener{

public SharedPreferences preferences;


private GestureDetector gestureScanner;
       	
    public static ViewFlipper vflipper;
    Animation animFadein;
	View v;
	public String addr="sorry";
	 
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		
    		 	preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    		 	int flag = preferences.getInt("f", 0);
    		 
    	
    		 	if(flag==0)
    		  	{
    			  //Toast.makeText(getActivity(), "gugi",Toast.LENGTH_SHORT).show();
    			  
    		 		v = inflater.inflate(R.layout.home_layout, container, false);
    		  			
    			  //Fragment fr=(Fragment)v.findViewById(R.id.map)			      
    		  	}
    		  	else
    		  	{
    			 
    		  		v = inflater.inflate(R.layout.home_layout, container, false);

    		  		TextView t=(TextView)v.findViewById(R.id.textView10);
    	    		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	    		String unames = preferences.getString("nameuser", "Jane Doe");
    	    		t.setText(unames);
    	    		Typeface tf=Typeface.createFromAsset(getActivity().getAssets(),"font/font1.ttf");
    	    		t.setTypeface(tf);
    			    	      	 
    	    		DataBaseHelper db = new DataBaseHelper(getActivity());
    	    		double[]  lati2=new double[db.getContactsCount()];
    	    		double[] longi2=new double[db.getContactsCount()];
    	    		lati2=db.getLati();
    	    		longi2=db.getLongi();
    	    		db.close();
    	         
    	    		Routes route_data[] = new Routes[]
    	    		{
	                          new Routes("Pattom","9:50 am"),new Routes("Ulloor","10:20 am"),new Routes("Pongummodu","10:35 am"),
	                          new Routes("Sreekaryam","10:50 am"),new Routes("Chavadimmuku","11:00 am"),new Routes("AlSaj","11.30 pm"),
	                          
	                };
    	    		RoutesAdapter adapter;
	                     
    	    		adapter = new RoutesAdapter(getActivity(),
	                              R.layout.list_item_row_home, route_data);
    	         
	                     
	                    // adapter.notifyDataSetChanged();
	              
	                     
    	    		ListView listView1 = (ListView)v.findViewById(R.id.listView1);
	                      
	                  //  adapter.notifyDataSetChanged();
    	    		listView1.setAdapter(adapter);
    		  	}
    	
    		 	return v;
    		}
    	
    		@Override
    		public void onResume() {
    			//mapView.onResume();
    			super.onResume();
    		}
    		@Override
    		public void onDestroy() {
    			super.onDestroy();
    			//	mapView.onDestroy();
    		}
    		@Override
    		public void onLowMemory() {
    			super.onLowMemory();
    			//	mapView.onLowMemory();
    		}

    		@Override
    		public void onRefresh() {
			// TODO Auto-generated method stub
			
    		}
    		public boolean haveNetworkConnection() {
    			boolean haveConnectedWifi = false;
    			boolean haveConnectedMobile = false;

    			ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
		

	
}