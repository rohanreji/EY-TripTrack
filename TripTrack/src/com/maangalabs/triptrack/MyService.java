package com.maangalabs.triptrack;


import java.util.List;

import org.json.JSONArray;
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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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
	ConnectionFactory factory;
	Connection connection;
	Channel channel,channel1;
	Tag myTag;
	String array1[]=new String[90000];
	int t1;
	String Taglist,Taglist1;
//	private NfcAdapter NfcAdapter1;
	public double km;
//	protected NfcAdapter nfcAdapter;
//	protected PendingIntent nfcPendingIntent;
	LocationManager locationManager;
	
	static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 2; // in Meters
	static final long MINIMUM_TIME_BETWEEN_UPDATES = 0;
	Intent i;
	
	
	private PlaceEventListener placeEventListener = new PlaceEventListener() {

    @Override
    	public void placeEvent(PlaceEvent event) {
    	
    	 	
    		String placeNameAndId = "id: " + event.getPlace().getId() + " name: " + event.getPlace().getPlaceName();
    		if(event.getEventType().equals(event.PLACE_EVENT_TYPE_LEFT))
    		{
    		Toast toast = Toast.makeText(getApplicationContext(), placeNameAndId, Toast.LENGTH_LONG);
    		toast.show();
    		
    		
    		if(MainActivity.t1!=null)
				MainActivity.t1.setText("Last Geofence: "+event.getPlace().getPlaceName());
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

			int icon = R.drawable.ic_launcher;        
			CharSequence tickerText = "GImbal GEofence"; // ticker-text
			long when = System.currentTimeMillis();         
			Context context = getApplicationContext();     
			CharSequence contentTitle = "Gimbal breaked..!!";  
			CharSequence contentText = placeNameAndId;      
			Intent notificationIntent = new Intent(MyService.this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getService(MyService.this, 0, notificationIntent, 0);
			Notification notification = new Notification(icon, tickerText, when);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

			// and this
			final int HELLO_ID = 1;
			mNotificationManager.notify(HELLO_ID, notification);
    		
    		
    		
			
			
			
			
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
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
    		String tempstr="";
    		JSONObject json = new JSONObject(); 
    		JSONObject json1 = new JSONObject(); 
    		
			try {
				JSONObject jo=new JSONObject();
				json.put("format",  "break");
				tempstr+=json;
				tempstr+="\r\n";
				
				
				jo.put("stop_id",  event.getPlace().getId());

				jo.put("stop_name", event.getPlace().getPlaceName()); 
				long timestamp1 = System.currentTimeMillis();
				String timestamp=Long.toString(timestamp1);
				jo.put("time_stamp", timestamp);

				//json.put("tagid:",Taglist1);
				
				
				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
				
				jo.put("trip_id:",telephonyManager.getDeviceId());
				json1.put("broken", jo);
				tempstr+=json1;
				tempstr+="\r\n";
				new send1().execute(tempstr);

			} catch (JSONException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	private ContextCoreConnector contextCoreConnector;
      
	private ContextPlaceConnector contextPlaceConnector;
	@Override
	 public void onDestroy(){
		
	     super.onDestroy();
	   
		 Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
		
	 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String getTextData(byte[] payload) {
		  if(payload == null)
		    return null;
		  try {
		    String encoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
		    int langageCodeLength = payload[0] & 0077;
		    return new String(payload, langageCodeLength + 1, payload.length - langageCodeLength - 1, encoding);    
		  } catch(Exception e) {
		    e.printStackTrace();
		  }
		  return null;
		}
	
	

	public String[] loadArray(String arrayName, Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
	    int size = prefs.getInt(arrayName + "_size", 0);  
	    String array[] = new String[size];  
	    for(int i=0;i<size;i++)  
	        array[i] = prefs.getString(arrayName + "_" + i, null);  
	    return array;  
	}  
	
	
	
	
	public void onCreate() {
		// subscribeToLocationUpdates();
		
		
	
	
		
//		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//		
		try{
			
			factory = new ConnectionFactory();
			factory.setHost("54.89.248.120");
			// my internet connection is a bit restrictive so I have use an
			// external server
			// which has RabbitMQ installed on it. So I use "setUsername"
			// and "setPassword"
			factory.setUsername("guest");
			factory.setPassword("guest");
			//factory.setVirtualHost("/");
			factory.setPort(5672);
			System.out.println(""+factory.getHost()+factory.getPort()+factory.getRequestedHeartbeat()+factory.getUsername());
			connection = factory.newConnection();
			 channel = connection.createChannel();
			 channel1 = connection.createChannel();
				channel.exchangeDeclare(EXCHANGE_NAME, "direct");
				channel1.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		
				
		}catch(Exception e)
		{
			
		}
		
		 
	       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	       if (prefs.getBoolean("sender", true)) {
	           // etc
	       
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
	    

	    		CountDownTimer t = new CountDownTimer( Long.MAX_VALUE , 2000) {

    // This is called every interval. (Every 10 seconds in this example)
	    				public void onTick(long millisUntilFinished) {
    
//	    					String tagid="";
//	    					
//	    					//Add this 
//	    			if(i!=null)
//	    				{
//	    					if(i.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED))	
//	    					{
//	    						myTag = (Tag) i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//	    						
//	    						
//	    						
//	    						
//	    						
//	    						
//	    					
//	    						 byte[] typ,payload ;
//	    						    NdefMessage ndefMesg;
//	    						    Parcelable[] rawMsgs = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//	    						    ndefMesg = (NdefMessage) rawMsgs[0];
//	    						               
//	    						    		  
//	    						    NdefRecord[] ndefRecords = ndefMesg.getRecords();
//	    						    int len = ndefRecords.length;
//	    						    for (int i = 0; i < len; i++) {
//	    						    	typ = ndefRecords[i].getType();
//	    						    	payload = ndefRecords[i].getPayload();
//	    						    	tagid= getTextData(payload);
//	    						      // Toast.makeText(getApplicationContext(), tagid,Toast.LENGTH_SHORT).show();
//	    						       
//	    						   	}
//	    			        	
//	    					
//	    							Toast.makeText(getApplicationContext(), "Connected "+tagid,Toast.LENGTH_SHORT).show();
//	    						
//	    						
//	    						
//	    						
//	    						
//	    						
//	    						
//	    						
//	    					}
////	    					else
////	    						Taglist1=Taglist1+i.getAction().toString();
////	    					if(myTag!=null)
////	    						Taglist1=Taglist1+myTag.toString()+",";
////	    					else
////	    						Taglist1=Taglist1+"r";
//	    					
//	    				}
//	    			else
//	    				Toast.makeText(getApplicationContext(), "no intent",Toast.LENGTH_SHORT).show();
//	    			
//	    			
//	    			
//	    			
//	    			
//	    			
//	    			
//	    			
	    			
	    			
	    					 array1=loadArray("taglist", getApplicationContext());
	    					String tempstr="";
	    					JSONObject json = new JSONObject(); 
	    					JSONObject json1 = new JSONObject(); 
	    					JSONObject json2 = new JSONObject(); 
	    					try {
	    					
	    						long timestamp1 = System.currentTimeMillis();
	    						String timestamp=Long.toString(timestamp1);
	    						
	    						
	    						json2.put("format", "point");
	    						tempstr+=json2;
	    						tempstr+="\r\n";
	    						
	    						
	    						JSONObject jo1=new JSONObject();
	    						jo1.put("_index", "rabbit4");
	    						jo1.put("_type", "pin");
	    						jo1.put("_id", t1++);
	    						json1.put("create", jo1);
	    						tempstr+=json1;
	    						tempstr+="\r\n";
	    						
	    						
	    						
	    						JSONObject jo=new JSONObject();
	    						jo.put("lat", latitude);
	    						jo.put("lng", longitude);
	    						json.put("location", jo);
	    						
	    						
	    						

	    			    		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
	    						
	    						json.put("timestamp", timestamp);
		
	    						JSONArray js=new JSONArray();
	    						for(int u=0;u<array1.length;u++)
	    						{
	    						js.put(array1[u]);
	    						}
	    						json.put("employees",js);
	    						json.put("trip_id",telephonyManager.getDeviceId());
	    						tempstr+=json;
	    						tempstr+="\r\n";
	    						//Toast.makeText(getApplicationContext(), "lat: "+latitude+" long: "+longitude, Toast.LENGTH_SHORT).show();
	    						if(MainActivity.t!=null)
	    						MainActivity.t.setText( "lat: "+latitude+" long: "+longitude);
	    							
	    							if(MainActivity.t1!=null)
	    								MainActivity.t1.setText("Last Geofence: "+System.currentTimeMillis());
	    							if(latitude!=0.0)
	    							{
	    						//System.out.println(tempstr);
	    						new send().execute(tempstr);
	    							}
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

	
	
	
	private class send extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... Message) {
			try {
				//Toast.makeText(getApplicationContext(), "click",Toast.LENGTH_SHORT).show();
				
				
				
				//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				String tempstr="";
				for(int i=0;i<Message.length;i++)
					tempstr+=Message[i];
				
				if(channel==null)
				{
					
					factory = new ConnectionFactory();
					factory.setHost("54.89.248.120");
					// my internet connection is a bit restrictive so I have use an
					// external server
					// which has RabbitMQ installed on it. So I use "setUsername"
					// and "setPassword"
					factory.setUsername("guest");
					factory.setPassword("guest");
					//factory.setVirtualHost("/");
					
					factory.setPort(5672);
					System.out.println(""+factory.getHost()+factory.getPort()+factory.getRequestedHeartbeat()+factory.getUsername());
					connection = factory.newConnection();
					 channel = connection.createChannel();
					channel.exchangeDeclare(EXCHANGE_NAME, "direct");
				}
				channel.basicPublish(EXCHANGE_NAME, "key", null,
						tempstr.getBytes());
				
				System.out.println("\nsend message:"+tempstr);
				
			} catch (Exception e) {
				// TODO: handle exception
				
				
				e.printStackTrace();
			
			}
			// TODO Auto-generated method stub
			return null;
		}
		
		
		
		

	}
	private class send1 extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... Message) {
			try {
				//Toast.makeText(getApplicationContext(), "click",Toast.LENGTH_SHORT).show();
				
				
				//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
				String tempstr="";
				for(int i=0;i<Message.length;i++)
					tempstr+=Message[i];
				
				if(channel1==null)
				{
					
					factory = new ConnectionFactory();
					factory.setHost("54.89.248.120");
					// my internet connection is a bit restrictive so I have use an
					// external server
					// which has RabbitMQ installed on it. So I use "setUsername"
					// and "setPassword"
					factory.setUsername("guest");
					factory.setPassword("guest");
					//factory.setVirtualHost("/");
					factory.setPort(5672);
					System.out.println(""+factory.getHost()+factory.getPort()+factory.getRequestedHeartbeat()+factory.getUsername());
					connection = factory.newConnection();
					 channel1 = connection.createChannel();
					channel1.exchangeDeclare(EXCHANGE_NAME, "direct");
				}
				channel1.basicPublish(EXCHANGE_NAME, "key", null,
						tempstr.getBytes());
				System.out.println("\nsend message:"+tempstr);
				

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			
			}
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
