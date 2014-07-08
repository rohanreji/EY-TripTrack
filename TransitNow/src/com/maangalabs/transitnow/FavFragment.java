package com.maangalabs.transitnow;

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

import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 /*
  * Fragment class for Favourite layout
  */
@SuppressLint("NewApi") public class FavFragment extends Fragment implements onRefreshListener {
	String names="no cabs!";
	View rootView;
	CabAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
      rootView = inflater.inflate(R.layout.fav_layout, container, false);
      
        Log.d("tag1", "invalidate");
       // container.removeAllViews();
      
     
		 
        return rootView;
    }
    
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		
		  new LongOperation1().execute(" ");
		
	}
    
    
	
	
	
	
	 /*
	  * Calling api that returns the list of cabs
	  */
    
    
		public class LongOperation1 extends AsyncTask<String, Void, String> {

	        @Override
	        protected String doInBackground(String... params) {
	        	try{
	        	
	        		 SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	       		  int flag = preferences.getInt("f", 0);
	        	   		             		
	            	
	                	StringBuilder builder = new StringBuilder();
	            	    HttpClient client = new DefaultHttpClient();
	            	    HttpGet httpGet = new HttpGet("http://192.168.0.103:1337/employee/findEmpbyId/"+flag);
	            	    Log.d("tag1","http://192.168.0.103:1337/employee/findEmpbyId/"+flag);
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
	                /*checker
	                 * 
	                 */
	            	    Log.d("checker",builder.toString());
	            	    JSONObject jsonObj = new JSONObject(builder.toString());
	        	      //  System.out.print("rr  "+jsonObj.getInt("success"));
	        	        names=jsonObj.getString("name");
	              //  deviceName.remove(i);
	            	//deviceName.add(i,"Dell Inspiron");
	                	
	        	        Log.d(names,names+" kgu");
	                
	            	
	        	}
	            	
	            	
	        	
	        	
	        	catch(Exception e)
	        	{
	        		
	        	}
	        	/*checker
	        	 * *
	        	 */
	        
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
	        	  Cabs weather_data[] = new Cabs[]
	                      {
	                          new Cabs(names),
	                          
	                      };
	                     
	         adapter = new CabAdapter(getActivity(),
	                              R.layout.listview_item_row, weather_data);
	                     
	                    // adapter.notifyDataSetChanged();
	              
	                     
	         ListView listView1 = (ListView)rootView.findViewById(R.id.listView1);
	                      
	                  //  adapter.notifyDataSetChanged();
	         listView1.setAdapter(adapter);
	     		this.cancel(true);
	        	
	        }

	        @Override
	        protected void onPreExecute() {}

	        @Override
	        protected void onProgressUpdate(Void... values) {
	        	
	        }
	    }
	    
	    
	    
	
	
}
