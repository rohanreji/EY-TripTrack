package com.maangalabs.transitnow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

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

import com.facebook.Session;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.*;
import com.facebook.model.*;
public class FbAuth extends Activity {
	ViewFlipper viewFlipper;
	JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<String> worldlist;
    int pos;
    String s1[];
	public static int ba=1;
	int code;
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	 SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
	     	int flag = preferences.getInt("f", 0);
	     	if(flag!=0)
	     	{
	     		
	     		Intent i=new Intent(this,MainActivity.class);
	     		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	     		startActivity(i);
	     		finish(); 
	     		
	     	}	
	    	setContentView(R.layout.fb_auth);
	    	  viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
	    	 TextView welcome = (TextView) findViewById(R.id.welcome);
	    	 Typeface tf=Typeface.createFromAsset(getAssets(),"font/font1.ttf");
             welcome.setTypeface(tf);
             EditText e1=(EditText)findViewById(R.id.editText1);
             e1.setVisibility(View.INVISIBLE);
             Button b1=(Button)findViewById(R.id.button1);
             b1.setVisibility(View.INVISIBLE);
             TextView welcome1 = (TextView) findViewById(R.id.welcome1);
	    	 
             welcome1.setTypeface(tf);
	
	    	/*
	    	 * use this to generate fb hash key.
	    	 */
	    	
	    	/*  	try {
	            PackageInfo info = getPackageManager().getPackageInfo(
	                    "com.maangalabs.transitnow", 
	                    PackageManager.GET_SIGNATURES);
	            for (Signature signature : info.signatures) {
	                MessageDigest md = MessageDigest.getInstance("SHA");
	                md.update(signature.toByteArray());
	                Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	                }
	        } catch (NameNotFoundException e) {

	        } catch (NoSuchAlgorithmException e) {

	        }*/
	    	
	    	 Session.openActiveSession(this, true, new Session.StatusCallback() {

	    	      // callback when session changes state
	    	      @Override
	    	      public void call(Session session, SessionState state, Exception exception) {
	    	        if (session.isOpened()) {

	    	          // make request to the /me API
	    	          Request.newMeRequest(session, new Request.GraphUserCallback() {

	    	            // callback after Graph API response with user object
	    	            @Override
	    	            public void onCompleted(GraphUser user, Response response) {
	    	              if (user != null) {
	    	                TextView welcome = (TextView) findViewById(R.id.welcome);
	    	                welcome.setText("Hello, " + user.getName() + "!");
	    	                Typeface tf=Typeface.createFromAsset(getAssets(),"font/font1.ttf");
	    	                welcome.setTypeface(tf);
	    	                EditText e1=(EditText)findViewById(R.id.editText1);
	    	                e1.setVisibility(View.VISIBLE);
	    	                Button b1=(Button)findViewById(R.id.button1);
	    	                b1.setVisibility(View.VISIBLE);
	    	                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FbAuth.this);
	    	        		SharedPreferences.Editor editor = preferences.edit();
	    	        		editor.putString("nameuser",user.getName());
	    	        		editor.commit();
	    	                
	    	              }
	    	            }
	    	          }).executeAsync();
	    	        }
	    	      }
	    	    });
	}
	public void clicks(View v)
	{
		new SendM().execute(" ");
		
		
	}
	public void backs(View v)
	{
		if (viewFlipper.getDisplayedChild() == 0)
            return;
       
        // set the required Animation type to ViewFlipper
        // The Next screen will come in form Left and current Screen will go OUT from Right
        viewFlipper.setInAnimation(this, R.anim.in_from_left);
        viewFlipper.setOutAnimation(this, R.anim.out_to_right);
        // Show the next Screen
        viewFlipper.showPrevious();
	}
	public void subs(View v)
	{
		EditText e2=(EditText)findViewById(R.id.editText11);
		
			
		try{
		if(Integer.parseInt(e2.getText().toString())==code)
		{
			Toast.makeText(getApplicationContext(), "got it", Toast.LENGTH_SHORT).show();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FbAuth.this);
    		SharedPreferences.Editor editor = preferences.edit();
    		editor.putInt("f",Integer.parseInt(e2.getText().toString()));
    		
    		editor.commit();
    		Intent i=new Intent(this,MainActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(i);
    		finish(); 
		}
		else
			
		{
			  if (viewFlipper.getDisplayedChild() == 2)
	                return;
	            // set the required Animation type to ViewFlipper
	            // The Next screen will come in form Right and current Screen will go OUT from Left
	            viewFlipper.setInAnimation(FbAuth.this, R.anim.in_from_right);
	            viewFlipper.setOutAnimation(FbAuth.this, R.anim.out_to_left);
	            // Show The Previous Screen
	            viewFlipper.showNext();
	            new DownloadJSON().execute();
		}
		}
		catch(Exception e){
			
		}
		
			
	}
	/*float lastX;
	
	  public boolean onTouchEvent(MotionEvent touchevent)
      {
                   switch (touchevent.getAction())
                   {
                          // when user first touches the screen to swap
                           case MotionEvent.ACTION_DOWN:
                           {
                               lastX = touchevent.getX();
                               break;
                          }
                           case MotionEvent.ACTION_UP:
                           {
                               float currentX = touchevent.getX();
                              
                               // if left to right swipe on screen
                               if (lastX < currentX)
                               {
                                    // If no more View/Child to flip
                                   if (viewFlipper.getDisplayedChild() == 0)
                                       break;
                                  
                                   // set the required Animation type to ViewFlipper
                                   // The Next screen will come in form Left and current Screen will go OUT from Right
                                   viewFlipper.setInAnimation(this, R.anim.in_from_left);
                                   viewFlipper.setOutAnimation(this, R.anim.out_to_right);
                                   // Show the next Screen
                                   viewFlipper.showNext();
                               }
                              
                               // if right to left swipe on screen
                               if (lastX > currentX)
                               {
                                   if (viewFlipper.getDisplayedChild() == 1)
                                       break;
                                   // set the required Animation type to ViewFlipper
                                   // The Next screen will come in form Right and current Screen will go OUT from Left
                                   viewFlipper.setInAnimation(this, R.anim.in_from_right);
                                   viewFlipper.setOutAnimation(this, R.anim.out_to_left);
                                   // Show The Previous Screen
                                   viewFlipper.showPrevious();
                               }
                               break;
                           }
                   }
                   return false;
      }
	*/
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      if(requestCode==1)
	      {
	    	 finish(); 
	      }
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	     
	    }
	 
	 
	  private class SendM extends AsyncTask<String, String, String> {
		  
		
		  
	        @Override
	        protected String doInBackground(String... params) {
	        		
	        	 EditText e1=(EditText)findViewById(R.id.editText1);
				 String sender1=e1.getText().toString();
	            GMailSender sender = new GMailSender("rohanreji93@gmail.com", "squarednotcubed1!");
	            try {
	            	Random rn = new Random();
	            	int n = 888888 + 1;
	            	int i = rn.nextInt() % n;
	            	int randomNum =  111111 + i;
	            	randomNum=Math.abs(randomNum);
	            	code=randomNum;
					sender.sendMail("TransitNow Employee Verification code:",   
					        "Your Verification code: "+randomNum,   
					        "rohanreji@yahoo.co.in",sender1);
					return "executed";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
					return " ";
				} 
	        		
	        }
	        @Override
	        protected void onPostExecute(String result) {
	        	
	    		if(result.equals("executed"))
	    		{
	    			 Toast.makeText(getApplicationContext(), "mail send", Toast.LENGTH_SHORT).show();
	    	            if (viewFlipper.getDisplayedChild() == 1)
	    	                return;
	    	            // set the required Animation type to ViewFlipper
	    	            // The Next screen will come in form Right and current Screen will go OUT from Left
	    	            viewFlipper.setInAnimation(FbAuth.this, R.anim.in_from_right);
	    	            viewFlipper.setOutAnimation(FbAuth.this, R.anim.out_to_left);
	    	            // Show The Previous Screen
	    	            viewFlipper.showNext();
	    		}
	    		else
	    		{
	    			Toast.makeText(getApplicationContext(), "unable to send mail", Toast.LENGTH_SHORT).show();
	    		}
	        }

	        @Override
	        protected void onPreExecute() {
	       
	        }

	        protected void onProgressUpdate(String... progress) {
	   		
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
					jsonA = JSONfunctions.getJSONfromURL("http://192.168.0.105:1111/all_stops");
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
	                    .setAdapter(new ArrayAdapter<String>(FbAuth.this,
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
					jsonA = JSONfunctions.getJSONfromURL("http://192.168.0.105:1111/stops_from?stop_id="+s1[pos]);
					System.out.println(s1[pos]);
					
	            	for(int i=0;i<jsonA.length();i++){
	            		
	            		JSONObject e = jsonA.getJSONObject(i);
	            		
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
	                    .setAdapter(new ArrayAdapter<String>(FbAuth.this,
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
	                            
	                        }
	 
	                        @Override
	                        public void onNothingSelected(AdapterView<?> arg0) {
	                            // TODO Auto-generated method stub
	                        }
	                    });
	        }
	    }
}
