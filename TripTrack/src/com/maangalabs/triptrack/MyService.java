package com.maangalabs.triptrack;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.qualcommlabs.usercontext.Callback;
import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.protocol.Place;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi") public class MyService extends Service {
	Location loc;
	public double latitude;
	public double longitude;
	private String QUEUE_NAME = "queue1";
	private String EXCHANGE_NAME = "realtime";
	Tag myTag;
	int t1;
	String Taglist,Taglist1;
	private NfcAdapter NfcAdapter1;
	public double km;
	LocationManager locationManager;
	
	static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
	static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	Intent i;
	private void vibrate() {
	
		
		Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
		vibe.vibrate(500);
	}
	
	
	private PlaceEventListener placeEventListener = new PlaceEventListener() {

    @Override
    	public void placeEvent(PlaceEvent event) {
    	
    	 	
    		String placeNameAndId = "id: " + event.getPlace().getId() + " name: " + event.getPlace().getPlaceName();
    		Toast toast = Toast.makeText(getApplicationContext(), placeNameAndId, Toast.LENGTH_LONG);
    		toast.show();
    		//vibrate();
    		
    		Log.i("found place", placeNameAndId);
    		
    		/*if(i.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))	
			{
				myTag = (Tag) i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			}
			else
				Taglist1=Taglist1+i.getAction().toString();
			if(myTag!=null)
				Taglist1=Taglist1+myTag.toString()+",";
			else
				Taglist1=Taglist1+"r";*/
    		
    		JSONObject json = new JSONObject(); 
			try {
				JSONObject jo=new JSONObject();
				jo.put("lat:", latitude);
				jo.put("long:", longitude);
				json.put("current_loc:", jo);
				json.put("id:",  event.getPlace().getId());

				json.put("place:", event.getPlace().getPlaceName()); 
				long timestamp = System.currentTimeMillis();
				json.put("time_stamp:", timestamp);

				json.put("tagid:",Taglist1);
				
				
				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
				
				json.put("trip_id:",telephonyManager.getDeviceId());

				new send1().execute(json);

			} catch (JSONException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	};
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		i=arg0;
		Taglist="";
		return null;
	}
	 @Override
     public int onStartCommand(Intent intent, int flags, int startId) { 
		 super.onStartCommand(intent, flags, startId);
		 return START_STICKY;
     }
	 NfcAdapter mNfcAdapter;
	private ContextCoreConnector contextCoreConnector;
      
	private ContextPlaceConnector contextPlaceConnector;
	@Override
	 public void onDestroy(){
		
	     super.onDestroy();
	   
		 Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
		
	 }
	public void onCreate() {
		// subscribeToLocationUpdates();
		
		
		
		i=new Intent();
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		contextCoreConnector = ContextCoreConnectorFactory.get(this);
	    contextPlaceConnector = ContextPlaceConnectorFactory.get(this);
	    checkContextConnectorStatus();
	      t1=0; 
		
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(
	    		LocationManager.GPS_PROVIDER,

	    		MINIMUM_TIME_BETWEEN_UPDATES,

	    		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,

	    		new MyLocationListener());
	    

	    		CountDownTimer t = new CountDownTimer( Long.MAX_VALUE , 10000) {

    // This is called every interval. (Every 10 seconds in this example)
	    				public void onTick(long millisUntilFinished) {
    
	    
	    					
	    					//Add this 
	    		/*		if(i!=null)
	    				{
	    					if(i.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))	
	    					{
	    						myTag = (Tag) i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	    					}
	    					else
	    						Taglist1=Taglist1+i.getAction().toString();
	    					if(myTag!=null)
	    						Taglist1=Taglist1+myTag.toString()+",";
	    					else
	    						Taglist1=Taglist1+"r";
	    					
	    				}*/
	    					JSONObject json = new JSONObject(); 
	    					try {
	    						JSONObject jo=new JSONObject();
	    						jo.put("lat:", latitude);
	    						jo.put("long:", longitude);
	    						json.put("current_loc:", jo);

	    			    		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
	    						long timestamp = System.currentTimeMillis();
	    						json.put("time_stamp:", timestamp);
		
	    						json.put("version:",t1++);
	    						json.put("trip_id:",telephonyManager.getDeviceId());
	    						new send().execute(json);
		
	    					} catch (JSONException e) {
				// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
    
	    				}

	    				public void onFinish() {
        
	    					start();
	    				}
	    		}.start();

	}
	
	
	
	
	
	
	
	
	
	/*
	 private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
	        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

	        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
	        byte[] textBytes = text.getBytes(utfEncoding);

	        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
	        char status = (char) (utfBit + langBytes.length);

	        byte[] data = new byte[1 + langBytes.length + textBytes.length];
	        data[0] = (byte) status;
	        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
	        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

	        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	    }

	
	*/
	 private void checkContextConnectorStatus() {
	        if (contextCoreConnector.isPermissionEnabled()) {
	        	
	            startListeningForGeofences();
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(), "no gimbal permission",Toast.LENGTH_SHORT).show();
	        }
	       
	    }
	 
	 
	 
	    
	        private void startListeningForGeofences() {
	        	Toast.makeText(getApplicationContext(), "gimbal permission enabled",Toast.LENGTH_SHORT).show();
	            contextPlaceConnector.addPlaceEventListener(placeEventListener);
	            
	        }
	    

	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
protected void showCurrentLocation() {
			
		 
 Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
   if (location != null) {
   loc=location;

   }
  
   if(loc!=null)
   {
	   latitude = loc.getLatitude();
	   longitude = loc.getLongitude();
	   
	   
	   
	 
   }
 
  } 
	
	
	
	
	
	
	
	
	
	
	
	

	private class MyLocationListener implements LocationListener {
		 
		  public void onLocationChanged(Location location) {
		         
		  showCurrentLocation();
		         
		         
		  }
		 
		  public void onProviderDisabled(String s) {
		        }
		  public void onProviderEnabled(String s) {
		   }
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		 }

	
	
	
	private class send extends AsyncTask<JSONObject, Void, Void> {

		@Override
		protected Void doInBackground(JSONObject... Message) {
			try {
				//Toast.makeText(getApplicationContext(), "click",Toast.LENGTH_SHORT).show();
				
				ConnectionFactory factory = new ConnectionFactory();
				factory.setHost("54.80.250.130");
				// my internet connection is a bit restrictive so I have use an
				// external server
				// which has RabbitMQ installed on it. So I use "setUsername"
				// and "setPassword"
				factory.setUsername("guest");
				factory.setPassword("guest");
				//factory.setVirtualHost("/");
				factory.setPort(5672);
				System.out.println(""+factory.getHost()+factory.getPort()+factory.getRequestedHeartbeat()+factory.getUsername());
				Connection connection = factory.newConnection();
				Channel channel = connection.createChannel();
				
				channel.exchangeDeclare(EXCHANGE_NAME, "direct");
				//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				String tempstr="";
				for(int i=0;i<Message.length;i++)
					tempstr+=Message[i];
				

				channel.basicPublish(EXCHANGE_NAME, "key", null,
						tempstr.getBytes());
				System.out.println("\nsend message:"+tempstr);
				channel.close();

				connection.close();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			
			}
			// TODO Auto-generated method stub
			return null;
		}

	}
	private class send1 extends AsyncTask<JSONObject, Void, Void> {

		@Override
		protected Void doInBackground(JSONObject... Message) {
			try {
				//Toast.makeText(getApplicationContext(), "click",Toast.LENGTH_SHORT).show();
				
				ConnectionFactory factory = new ConnectionFactory();
				factory.setHost("54.80.250.130");
				// my internet connection is a bit restrictive so I have use an
				// external server
				// which has RabbitMQ installed on it. So I use "setUsername"
				// and "setPassword"
				factory.setUsername("guest");
				factory.setPassword("guest");
				//factory.setVirtualHost("/");
				factory.setPort(5672);
				System.out.println(""+factory.getHost()+factory.getPort()+factory.getRequestedHeartbeat()+factory.getUsername());
				Connection connection = factory.newConnection();
				Channel channel1 = connection.createChannel();
				
				channel1.exchangeDeclare(EXCHANGE_NAME, "direct");
				//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				String tempstr="";
				for(int i=0;i<Message.length;i++)
					tempstr+=Message[i];
				

				channel1.basicPublish(EXCHANGE_NAME, "key1", null,
						tempstr.getBytes());
				System.out.println("\nsend message:"+tempstr);
				channel1.close();

				connection.close();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			
			}
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
    
	
	
}
