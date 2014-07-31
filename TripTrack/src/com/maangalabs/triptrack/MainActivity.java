package com.maangalabs.triptrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;

import com.qualcommlabs.usercontext.Callback;
import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;

@SuppressLint("NewApi") public class MainActivity extends ActionBarActivity {
	  private ContextCoreConnector contextCoreConnector;
      
	  private ContextPlaceConnector contextPlaceConnector;
	  private TextView mTextView;
	  private String QUEUE_NAME = "queue1";
		private String EXCHANGE_NAME = "realtime";
	 int p;
	 String name;
	 String dname;
	 Button b1,b2;
    private NfcAdapter mNfcAdapter;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       new LongOperation().execute(" ");
       
       
       
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);
        if(isMyServiceRunning())
        {
        	b1.setVisibility(View.INVISIBLE);
        	b2.setVisibility(View.VISIBLE);
        }
        mTextView = (TextView) findViewById(R.id.textView1);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            mTextView.setText("NFC is not supported.");
        }
     
        else if (!mNfcAdapter.isEnabled()) {
        	 Toast.makeText(this, " NFC Not enabled.", Toast.LENGTH_LONG).show();
        	 mTextView.setText("NFC is disabled.");
        } else {
        
        	 Toast.makeText(this, " NFC Enabled.", Toast.LENGTH_LONG).show();
        	 mTextView.setText("Enabled");
        }
        contextCoreConnector = ContextCoreConnectorFactory.get(this);
        contextPlaceConnector = ContextPlaceConnectorFactory.get(this);
        checkContextConnectorStatus();
       
        
    }
    
    
    private boolean isMyServiceRunning() {
 		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
 		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
 			if (MyService.class.getName().equals(service.service.getClassName())) {
 				return true;
 			}	
 		}
 		return false;
 	}
    
    public void ev1(View v)
    {
    	//	new send().execute(message);
    	EditText t2=(EditText)findViewById(R.id.editText1);
    	if(!t2.getText().toString().equals(""))
    	{
    	Intent i= new Intent(getApplicationContext(), MyService.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);	
    	
    	// potentially add data to the intent
    	i.putExtra("KEY1", "Value to be used by the service");
    	this.startService(i); 
    	b1.setVisibility(View.INVISIBLE);
    	b2.setVisibility(View.VISIBLE);
    	}
    	else
    		Toast.makeText(getApplicationContext(), "Wait,  fetching details from server",Toast.LENGTH_SHORT).show();
    	    }
    public void ev2(View v)
    {
    	stopService(new Intent(MainActivity.this, MyService.class));
    	b1.setVisibility(View.VISIBLE);
    	b2.setVisibility(View.INVISIBLE);		
    }
    private void checkContextConnectorStatus() {
        if (contextCoreConnector.isPermissionEnabled()) {
        	startListeningForGeofences();
        }
        else {
            contextCoreConnector.enable(this, new Callback<Void>() {

                @Override
                public void success(Void arg0) {
                	startListeningForGeofences();
                }

                @Override
                public void failure(int arg0, String arg1) {
                    Log.e("failed to enable", arg1);
                }
            });
        }
    }
    
//    private PlaceEventListener placeEventListener = new PlaceEventListener() {
//
//        @Override
//        	public void placeEvent(PlaceEvent event) {
//        	
//        	 	
//        		String placeNameAndId = "id: " + event.getPlace().getId() + " name: " + event.getPlace().getPlaceName();
//        		Toast toast = Toast.makeText(getApplicationContext(), placeNameAndId, Toast.LENGTH_LONG);
//        		toast.show();
//        		//vibrate();
//        		
//        		Log.i("found place", placeNameAndId);
        		
//        		JSONObject json = new JSONObject(); 
//    			try {
//    				json.put("id:",  event.getPlace().getId());
//
//    				json.put("place:", event.getPlace().getPlaceName()); 
//    				long timestamp = System.currentTimeMillis();
//    				json.put("time_stamp:", timestamp);
//
//    			
//    				
//    				
//    				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
//    				
//    				json.put("trip_id:",telephonyManager.getDeviceId());
//
//    				new send1().execute(json);
//
//    			} catch (JSONException e) {
//    // TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//        		
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
        		
//        		JSONObject json = new JSONObject(); 
//    			try {
//    				json.put("id:",  event.getPlace().getId());
//
//    				json.put("place:", event.getPlace().getPlaceName()); 
//    				long timestamp = System.currentTimeMillis();
//    				json.put("time_stamp:", timestamp);
//
//    				json.put("tagid:",Taglist1);
//    				
//    				
//    				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
//    				
//    				json.put("trip_id:",telephonyManager.getDeviceId());
//
//    				new send1().execute(json);
//
//    			} catch (JSONException e) {
//    // TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//        	}
//    	};
    private void startListeningForGeofences() {
    	Toast.makeText(getApplicationContext(), "gimbal permission enabled",Toast.LENGTH_SHORT).show();
     //   contextPlaceConnector.addPlaceEventListener(placeEventListener);
        
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    


private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
        	try{
        	
        		/*
        		 * Login api call
        		 */
        			Log.d("task","adas");
            		
            		String s="30000";
                	StringBuilder builder = new StringBuilder();
            	    HttpClient client = new DefaultHttpClient();
            	    HttpGet httpGet = new HttpGet("http://192.168.0.64:1337/employee/findEmpbyId/"+s);
            	    Log.d("tag1","http://192.168.0.64:1337/employee/findEmpbyId/"+s);
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
        	        name=jsonObj.getString("name");
        	        dname=jsonObj.getString("mac");
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
        		EditText t=(EditText)findViewById(R.id.editTextSimple);
        		
       		 	
       		 	
       		 	t.setText(name);
       		 EditText t1=(EditText)findViewById(R.id.editText1);
       		 t1.setText(dname);
     		
    		 	
    		 	
    		 	
       		 	this.cancel(true);
     	   	}
        	/*
        	 * Avoid this code once api is set
        	 */
        	
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
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
