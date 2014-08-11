package com.maangalabs.triptrack;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi") public class Listers extends Activity {
	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;
	String tagid;
	 String array1[]=new String[90000];
	
	
	public boolean saveArray(String[] array, String arrayName, Context mContext) {   
	    SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
	    SharedPreferences.Editor editor = prefs.edit();  
	    editor.putInt(arrayName +"_size", array.length);  
	    for(int i=0;i<array.length;i++)  
	        editor.putString(arrayName + "_" + i, array[i]);  
	    return editor.commit();  
	} 
	
	
	public String[] loadArray(String arrayName, Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
	    int size = prefs.getInt(arrayName + "_size", 0);  
	    String array[] = new String[size];  
	    for(int i=0;i<size;i++)  
	        array[i] = prefs.getString(arrayName + "_" + i, null);  
	    return array;  
	}  
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listers);
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		//enableForegroundMode();
	
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(this.getIntent().getAction()))
		{
			
			   byte[] typ,payload ;
			    NdefMessage ndefMesg;
			/*  
			    Tag myTag = (Tag) in.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			    
			    Ndef ndefTag = Ndef.get(myTag);
			    int size = ndefTag.getMaxSize();         // tag size
			    String type = ndefTag.getType();  // tag type
			     ndefMesg = ndefTag.getCachedNdefMessage();*/
			    
			 
			    	 Parcelable[] rawMsgs = this.getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			          
			            	
			            
			            	ndefMesg = (NdefMessage) rawMsgs[0];
			               
			    	
			  
			    NdefRecord[] ndefRecords = ndefMesg.getRecords();
			    int len = ndefRecords.length;
			    for (int i = 0; i < len; i++) {
			      typ = ndefRecords[i].getType();
			       payload = ndefRecords[i].getPayload();
			       tagid= getTextData(payload);
			      
			       Toast.makeText(getApplicationContext(), tagid+" scan again",Toast.LENGTH_SHORT).show();
			       
			       array1=loadArray("taglist", getApplicationContext());
//			       for(int j=0;j<array1.length;j++)
//			       {
//			    	   Toast.makeText(getApplicationContext(), array1[j], Toast.LENGTH_SHORT).show();
//			       }
			       if(array1.length==0)
			       {
			    	   String array2[]=new String[1];
			    	   array2[0]=tagid;
			    	   
			    	   saveArray(array2,"taglist", getApplicationContext());
			    	   array1=loadArray("taglist", getApplicationContext());
			    	   for(int j=0;j<array1.length;j++)
				       {
				    	   Toast.makeText(getApplicationContext(), array1[j], Toast.LENGTH_SHORT).show();
				       }
			    	   
			    	   
			    	   
			    	   Routes weather_data[] = new Routes[array1.length];
			        	for(int y=0;y<array1.length;y++)
			        	{
			        		weather_data[y]=new Routes(array1[y],"");
			        	}
			                     
			    	   RoutesAdapter adapter = new RoutesAdapter(Listers.this,
			                              R.layout.list_item_row_home, weather_data);
			                     
			                    // adapter.notifyDataSetChanged();
			              
			                     
			         ListView listView1 = (ListView)findViewById(R.id.listView12);
			                      
			                  //  adapter.notifyDataSetChanged();
			         listView1.setAdapter(adapter);
			    	   
			    	   
			    	   
			    	   
			    	   
			       }
			       else
			       {
			    	   String array2[]=new String[array1.length+1];
			    	   int h;
			    	   for( h=0;h<array1.length;h++)
			    	   {
			    		   array2[h]=array1[h];
			    	   }
			    	   array2[h]=tagid;
			    	   saveArray(array2,"taglist", getApplicationContext());
			    	   array1=loadArray("taglist", getApplicationContext());
			    	   for(int j=0;j<array1.length;j++)
					       {
					    	   Toast.makeText(getApplicationContext(), array1[j], Toast.LENGTH_SHORT).show();
					       }
			    	  
			    	   Routes weather_data[] = new Routes[array1.length];
			        	for(int y=0;y<array1.length;y++)
			        	{
			        		weather_data[y]=new Routes(array1[y],"");
			        	}
			                     
			    	   RoutesAdapter adapter = new RoutesAdapter(Listers.this,
			                              R.layout.list_item_row_home, weather_data);
			                     
			                    // adapter.notifyDataSetChanged();
			              
			                     
			         ListView listView1 = (ListView)findViewById(R.id.listView12);
			                      
			                  //  adapter.notifyDataSetChanged();
			         listView1.setAdapter(adapter);
			    	   
			       }
			 //      array1[array1.length]=tagid;
			       
			       
			       
			      
			       
			    }
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	@SuppressLint("NewApi") public void enableForegroundMode() {
		Log.d("TAG", "enableForegroundMode");

		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED); // filter for all
		IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
		nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
	}

	@SuppressLint("NewApi") public void disableForegroundMode() {
		Log.d("TAG", "disableForegroundMode");

		nfcAdapter.disableForegroundDispatch(this);
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void onNewIntent(Intent intent) {
		Log.d("TAG", "onNewIntent");
	

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {		
				Intent in=intent;
				 byte[] typ,payload ;
				    NdefMessage ndefMesg;
				    Parcelable[] rawMsgs = in.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				    ndefMesg = (NdefMessage) rawMsgs[0];
				               
				    		  
				    NdefRecord[] ndefRecords = ndefMesg.getRecords();
				    int len = ndefRecords.length;
				    for (int i = 0; i < len; i++) {
				    	typ = ndefRecords[i].getType();
				    	payload = ndefRecords[i].getPayload();
				    	tagid= getTextData(payload);
				      // Toast.makeText(getApplicationContext(), tagid,Toast.LENGTH_SHORT).show();
				       
				   	}
	        	
			
					Toast.makeText(getApplicationContext(), "Connected "+tagid,Toast.LENGTH_SHORT).show();
					
					
					
					
					
					
					 array1=loadArray("taglist", getApplicationContext());
				      
				     
				       if(array1.length==0)
				       {
				    	   String array2[]=new String[1];
				    	   array2[0]=tagid;
				    	   
				    	   saveArray(array2,"taglist", getApplicationContext());
				    	   array1=loadArray("taglist", getApplicationContext());
				    	   for(int j=0;j<array1.length;j++)
					       {
					    	   Toast.makeText(getApplicationContext(), array1[j], Toast.LENGTH_SHORT).show();
					       }
				    	   
				    	   
				    	   
				    	   Routes weather_data[] = new Routes[array1.length];
				        	for(int y=0;y<array1.length;y++)
				        	{
				        		weather_data[y]=new Routes(array1[y],"");
				        	}
				                     
				    	   RoutesAdapter adapter = new RoutesAdapter(Listers.this,
				                              R.layout.list_item_row_home, weather_data);
				                     
				                    // adapter.notifyDataSetChanged();
				              
				                     
				         ListView listView1 = (ListView)findViewById(R.id.listView12);
				                      
				                  //  adapter.notifyDataSetChanged();
				         listView1.setAdapter(adapter);
				    	   
				    	   
				    	   
				    	   
				    	   
				       }
				       else
				       {
				    	   String array2[]=new String[array1.length+1];
				    	   int h;
				    	   for( h=0;h<array1.length;h++)
				    	   {
				    		   array2[h]=array1[h];
				    	   }
				    	   array2[h]=tagid;
				    	   saveArray(array2,"taglist", getApplicationContext());
				    	   array1=loadArray("taglist", getApplicationContext());
				    	   for(int j=0;j<array1.length;j++)
						       {
						    	   Toast.makeText(getApplicationContext(), array1[j], Toast.LENGTH_SHORT).show();
						       }
				    	  
				    	   Routes weather_data[] = new Routes[array1.length];
				        	for(int y=0;y<array1.length;y++)
				        	{
				        		weather_data[y]=new Routes(array1[y],"");
				        	}
				                     
				    	   RoutesAdapter adapter = new RoutesAdapter(Listers.this,
				                              R.layout.list_item_row_home, weather_data);
				                     
				                    // adapter.notifyDataSetChanged();
				              
				                     
				         ListView listView1 = (ListView)findViewById(R.id.listView12);
				                      
				                  //  adapter.notifyDataSetChanged();
				         listView1.setAdapter(adapter);
				    	   
				    	   
				       }
				 //      array1[array1.length]=tagid;
				       
				       
				       
				      
				       
				    
				
				
				
			
		} else {
			Toast.makeText(getApplicationContext(),"No Tag Discovered", Toast.LENGTH_SHORT).show();
			
		}
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
	
	
	private void vibrate() {
		
		
		Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
		vibe.vibrate(500);
	}

	
	@Override
	protected void onResume() {
		

		super.onResume();

		enableForegroundMode();
	}

	@Override
	protected void onPause() {
		

		super.onPause();

		disableForegroundMode();
	}
	
	

}
