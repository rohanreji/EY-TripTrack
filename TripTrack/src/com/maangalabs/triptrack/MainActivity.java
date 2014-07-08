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
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;


import com.qualcommlabs.usercontext.Callback;
import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
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
	 int p;
	 String name;
	 String dname;
    private NfcAdapter mNfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       new LongOperation().execute(" ");
        setContentView(R.layout.activity_main);
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
    	}
    	else
    		Toast.makeText(getApplicationContext(), "Wait,  fetching details from server",Toast.LENGTH_SHORT).show();
    	    }
    private void checkContextConnectorStatus() {
        if (contextCoreConnector.isPermissionEnabled()) {
         
        }
        else {
            contextCoreConnector.enable(this, new Callback<Void>() {

                @Override
                public void success(Void arg0) {
                  
                }

                @Override
                public void failure(int arg0, String arg1) {
                    Log.e("failed to enable", arg1);
                }
            });
        }
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
	    
    

}
