package com.convention.notification.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResolveTimeConflict extends Activity  {
	


	String errorEventId;
	String errorEventName;
	String errorEventDate;
	String errorEventStart;
	String errorEventEnd;
	String errorEventLocation;
	String errorEventDelete;
	
	String conflictEventId;
	String conflictEventName;
	String conflictEventDate;
	String conflictEventStart;
	String conflictEventEnd;
	String conflictEventLocation;


	TextView NameDisplay;
	TextView DateDisplay;
	TextView StartTimeDisplay;
	TextView EndTimeDisplay;
	TextView LocationDisplay;

	Button btnUpdateSchedule;
	Button btnSeeSchedule;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		setContentView(R.layout.resolvetimeconflict);
		
		 ScrollView sv = new ScrollView(this);
	        LinearLayout ll = new LinearLayout(this);
	        ll.setOrientation(LinearLayout.VERTICAL);
	        sv.addView(ll);
		
		Bundle bundle = getIntent().getExtras();
		String time_conflicts = bundle.getString("time_conflicts");
		String deleted_event = bundle.getString("deleted_event");
		System.out.println(deleted_event + "  " + time_conflicts);


		
		NameDisplay = new TextView(this);
		DateDisplay= new TextView(this);
		StartTimeDisplay = new TextView(this);
		EndTimeDisplay = new TextView(this);
		LocationDisplay= new TextView(this);
		btnUpdateSchedule= new Button(this);
		btnSeeSchedule = new Button(this);
		
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(deleted_event);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				errorEventId = jsonObject.getString("event_id");
				errorEventName = jsonObject.getString("event_name");
				errorEventDate = jsonObject.getString("event_date");
				errorEventStart = jsonObject.getString("event_start");
				errorEventEnd = jsonObject.getString("event_end");
				errorEventLocation = jsonObject.getString("event_location");
				errorEventDelete= jsonObject.getString("event_delete_flag");

				NameDisplay.setText("Event: "+errorEventName +"      Date: "+errorEventDate+"");
				ll.addView(NameDisplay);

				StartTimeDisplay.setText("Start Time: "+errorEventStart+"  End Time: "+errorEventEnd);
				ll.addView(StartTimeDisplay);

				LocationDisplay.setText("Location: "+errorEventLocation+"\n");
				ll.addView(LocationDisplay);
				
				
			        

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		

/////////////////////////////////////////////////////
		
		try {
		JSONArray jsonArray2 = new JSONArray(time_conflicts);
		

			for (int i = 0; i < jsonArray2.length(); i++) {
				JSONObject jsonObject = jsonArray2.getJSONObject(i);

				conflictEventId = jsonObject.getString("event_id");
				conflictEventName = jsonObject.getString("event_name");
				conflictEventDate = jsonObject.getString("event_date");
				conflictEventStart = jsonObject.getString("event_start");
				conflictEventEnd = jsonObject.getString("event_end");
				conflictEventLocation = jsonObject.getString("event_location");
				
				System.out.println(conflictEventName+" \n Date: "+ conflictEventDate+" \n Times: "+ conflictEventStart+" to "+ conflictEventEnd+" \n Location: "+ conflictEventLocation);



				//conflictEventName+" \n Date: "+ conflictEventDate+" \n Times: "+ conflictEventStart+" to "+ conflictEventEnd+" \n Location: "+ conflictEventLocation

				 TextView tv2 = new TextView(this);
			        tv2.setText(conflictEventName+" \n Date: "+ conflictEventDate+" \n Times: "+ conflictEventStart+" to "+ conflictEventEnd+" \n Location: "+ conflictEventLocation+" \n");
			        ll.addView(tv2);
			        }
			        this.setContentView(sv);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		
		 Button btnUpdateSchedule = new Button(this);
		btnUpdateSchedule.setOnClickListener(new View.OnClickListener() {
			
			int id=Integer.parseInt(errorEventId);
			int df=Integer.parseInt(errorEventDelete);
					public void onClick(View v) {
						Intent intent = new Intent(ResolveTimeConflict.this
								.getApplicationContext(), UpdateServer.class);
						
						intent.putExtra("id", id);
						intent.putExtra("name", errorEventName);
						intent.putExtra("date", errorEventDate);
						intent.putExtra("startTime", errorEventStart);
						intent.putExtra("endTime", errorEventEnd);
						intent.putExtra("location", errorEventLocation);
						intent.putExtra("deleteFlag", df);
						
						
						startActivity(intent);
						
				
					}
				});
		btnUpdateSchedule.setText("Update Schedule");
		 ll.addView(btnUpdateSchedule);
		 
		 Button btnSeeSchedule = new Button(this);
		btnSeeSchedule.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ResolveTimeConflict.this
						.getApplicationContext(), WebviewSchedule.class);
				
				
				
				startActivity(intent);
				
		
			}
		});
		btnSeeSchedule.setText("See Schedule");
		 ll.addView(btnSeeSchedule);
		
		
	}// end OnCreate

	
	
	
//http://screencast.com/t/ODrHgQOGy4q0
}


