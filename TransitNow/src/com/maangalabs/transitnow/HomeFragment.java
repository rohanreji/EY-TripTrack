
package com.maangalabs.transitnow;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;


public class HomeFragment extends Fragment implements onRefreshListener {
	MapView mapView;
GoogleMap map;
public SharedPreferences preferences;
       	
    public static ViewFlipper vflipper;
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
    			  //Fragment fr=(Fragment)v.findViewById(R.id.map);
    			   vflipper = (ViewFlipper) v.findViewById(R.id.viewFlipper);
    		       if(vflipper!=null){
    		          vflipper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
    		          vflipper.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
    		       }
    		       else
    		       {
    		    	   Toast.makeText(getActivity(), "null",Toast.LENGTH_SHORT).show();
    		       }
    			   
    			   
    		 }
    		 else
    		 {
    			 v = inflater.inflate(R.layout.home_layout, container, false);
    			 RelativeLayout r=(RelativeLayout)v.findViewById(R.id.relativeLayout1);
    			 r.setVisibility(View.INVISIBLE);
    			 RelativeLayout r1=(RelativeLayout)v.findViewById(R.id.relativeLayout3);
    			 r1.setVisibility(View.VISIBLE);
    	      	 TextView t4=(TextView)v.findViewById(R.id.textView4);
    	      	 t4.setText("Logged in");
    	      	 DataBaseHelper db = new DataBaseHelper(getActivity());
    	    	 double[]  lati2=new double[db.getContactsCount()];
    	         double[] longi2=new double[db.getContactsCount()];
    	         lati2=db.getLati();
    	         longi2=db.getLongi();
    	         db.close();
    			if(haveNetworkConnection())
    			{
    	         Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());   
    			
    	         String result = null;
    	         String result1=null;
    	         try {
    	              List<Address> list = geocoder.getFromLocation(
    	             lati2[0],longi2[0], 1);
    	              List<Address> list1 = geocoder.getFromLocation(
    	     	             lati2[db.getContactsCount()-1],longi2[db.getContactsCount()-1], 1);
    	            
    	              if (list != null && list.size() > 0) {
    	                   Address address = list.get(0);
    	                    // sending back first address line and locality
    	                    result = address.getAddressLine(0);
    	                    result=result.substring(0, result.indexOf(","));
    	              }
    	              if(list1!=null && list1.size() > 0){
    	            	  Address address = list1.get(0);
  	                    // sending back first address line and locality
  	                    result1 = address.getAddressLine(0);
  	                    result1=result1.substring(0, result1.indexOf(","));
    	              }
    	            	  
    	         } catch (IOException e) {
    	                Log.e("TAG", "Impossible to connect to Geocoder", e);
    	          } 	
    	         TextView t9=(TextView)v.findViewById(R.id.textView9);
	        		t9.setText(result+" - "+result1);
    		  }
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