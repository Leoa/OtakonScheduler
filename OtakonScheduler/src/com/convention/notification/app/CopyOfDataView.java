package com.convention.notification.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class CopyOfDataView extends ListActivity{

	FileInputStream inputStream;
	String s = "";
	List<String> eventNames;
	List<String>eventDate;
	List<String>eventStart;
	List<String>eventEnd;
	List<String>eventLocation;
	List<String>eventDelete;
	
    @Override
    protected void onCreate(Bundle pop) {
    	
    	 super.onCreate(pop);
    	
	        setContentView(R.layout.columns);
        eventNames = new ArrayList<String>();
       
  		eventDate = new ArrayList<String>();
  		eventStart = new ArrayList<String>();
  		eventEnd = new ArrayList<String>();
  		eventLocation = new ArrayList<String>();
  		eventDelete = new ArrayList<String>();
        
          
        File myFile = new File(getApplicationContext().getFilesDir().getAbsolutePath()+ "/Test.txt");
		
		DownloadFileAsync FileTask = new DownloadFileAsync();
		
		FileTask.execute(myFile);
    	  
	}// end of onCreate
    
    
    
    private class DownloadFileAsync extends AsyncTask<File, Void, String> {

		@Override
		protected String doInBackground(File... params) {
			// TODO Auto-generated method stub
			
			File myFile = new File(getApplicationContext().getFilesDir().getAbsolutePath()+ "/Schedule.txt");
  			
			while(!myFile.exists()){
				try {
					Thread.sleep(1000);
			
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		
			File myDir = new File(getApplicationContext().getFilesDir().getAbsolutePath());
  			BufferedReader br = null;
			try {
			br = new BufferedReader(new FileReader(myDir + "/Schedule.txt"));
			
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			    try {
  					s = br.readLine();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  
			
			
			String result = s;
			return result;
		}// end of do in background
		
		
		@Override
		 protected void onPostExecute(String result) {
			  
	  		JSONArray jsonArray;
	  		try {
	  			jsonArray = new JSONArray(result);
	  			
	  			for (int i = 0; i < jsonArray.length(); i++) {
	  				JSONObject jsonObject = jsonArray.getJSONObject(i);
	  				String eventname =jsonObject.getString("event_name");
	  				String eventdate =jsonObject.getString("event_date");
	  				String eventstart =jsonObject.getString("event_start");
	  				String eventend =jsonObject.getString("event_end");
	  				String eventlocation =jsonObject.getString("event_location");
	  				String eventdelete =jsonObject.getString("event_delete_flag");
	  				
	  				Log.v("event web address", eventname+ " "+ eventdate+ " "+eventstart+ " "+eventend+ " "+eventlocation+ " "+eventdelete);
	  				eventNames.add(eventname);
	  				eventDate.add(eventdate);
	  				eventStart.add(eventstart);
	  				eventEnd.add(eventend);
	  				eventLocation.add(eventlocation);
	  				eventDelete.add(eventdelete);
	  				
	  				
	  			}
	  			
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}

	  		String[] EventName = new String[ eventNames.size()];
	  		eventNames.toArray(EventName);
	  		
	  		String[] EventDate = new String[ eventDate.size()];
	  		eventDate.toArray( EventDate );
	  		
	  		String[] EventStart = new String[ eventStart.size()];
	  		eventStart.toArray(EventStart);
	  		
	  		String[] EventEnd = new String[ eventEnd.size()];
	  		eventEnd.toArray( EventEnd);
	  		
	  		String[] EventLocation = new String[ eventLocation.size()];
	  		eventLocation.toArray(EventLocation);
	  		
	  		String[] EventDelete = new String[ eventDelete.size()];
	  		eventDelete.toArray( EventDelete);
 
	  		displaylist(EventName,EventDate,EventStart,EventEnd,EventLocation, EventDelete);
	  	    	
	  		
		}// end of onPostExecute
		   
	       
    }/// end of  DownloadFileAsync
 public void displaylist(String[] EventName, String[] EventDate, String[] EventStart, String[] EventEnd, String[] EventLocation, String[] EventDelete){
    	
    	
	 
	// ListView list = (ListView) findViewById(R.id.mylist);
	 ArrayList<HashMap<String, String>> mylistData =new ArrayList<HashMap<String, String>>();
	// String[] columnTags = new String[] {"col1", "col2", "col3"};
	// int[] columnIds = new int[] {R.id.column1, R.id.column2, R.id.column3};
	 int x=(int)EventName.length;
	 for(int i=0; i<x; i++)
	 {
		 HashMap<String,String> map = new HashMap<String, String>();
		 //initialize row data
		 for(int j=0; j<x; j++)
		 {
		    map.put("Event Name", EventName[i]);
		    map.put("Event Date", EventDate[i]);
		    map.put("Event Time", EventStart[i]);
		    
		 }
		 mylistData.add(map);
		}
	 
	 SimpleAdapter arrayAdapter = new SimpleAdapter(this, mylistData, R.layout.mylistrow,new String []{"Event Name", "Event Date","Event Time"} , new int[]{R.id.text1,R.id.text2,R.id.text3});
	setListAdapter(arrayAdapter);
	 
 }
 
 protected void onListItemClick(ListView l, View v, int position, long id) {

	    super.onListItemClick(l, v, position, id);
	    Object o = this.getListAdapter().getItem(position);
	    String event = o.toString();
	    Toast.makeText(this, "You have chosen event: " + " " + event, Toast.LENGTH_LONG).show();
	    startActivity(new Intent(this,UpdateServer.class));
	}

}
