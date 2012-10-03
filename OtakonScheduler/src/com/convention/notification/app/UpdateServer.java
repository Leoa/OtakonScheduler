package com.convention.notification.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class UpdateServer extends Activity {

	JSONObject json;
	private static final String Time_CONFLICTS = "time_conflicts";
	private static final String rssFeed = "http://www.leobee.com/android/push/login/update.php";
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID2 = 2;

	private TextView mTitleDisplay;
	private TextView mDateDisplay;
	private Button mPickDate;
	private TextView mTimeDisplay;
	private TextView mTimeEndDisplay;
	private Button mPickTime;
	private Button mEndPickTime;
	private TextView mLocationDisplay;
	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;

	Integer id;

	String name;
	String date;
	String startTime;
	String endTime;
	String location;
	int deleteFlag;
	String update_date;
	String update_startTime = startTime;
	String update_endTime = endTime;
	String update_location = location;
	String delete_event;
	String eventID;
	int endHour = -1;
	int endMin = -1;
	int startHour = -1;
	int startMin = -1;
	String defaultStartHour;
	String defaultStartMin;
	String defaultEndHour;
	String defaultEndMin;
	String eventMonth;
	Bundle bundle;
	String addBack = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatewiggets);

		bundle = getIntent().getExtras();

		init();

		Button button = (Button) findViewById(R.id.btnsave);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (startHour == -1) {

					String sh = startTime;

					ConvertStdTime shCST = new ConvertStdTime();
					startHour = shCST.toMilitary(sh);

					// sub string startHour to get minute
					startMin = shCST.getMin();
				}

				if (endHour == -1) {

					String sh = endTime;
					System.out.println(sh + " is end time hour");
					ConvertStdTime shCST = new ConvertStdTime();
					endHour = shCST.toMilitary(sh);
					System.out.println(" is end time hour " + endHour);
					endMin = shCST.getMin();
					System.out.println("end min is " + endMin);

				}

				if ((endHour == startHour && endMin == startMin)) {

					Toast.makeText(
							UpdateServer.this,
							"Error: Event starting time must be older than ending time.",
							Toast.LENGTH_LONG).show();

				} else {
					if ((endHour > startHour)
							|| (endHour == startHour && endMin >= startMin)) {

						if (Utils.isNetworkAvailable(UpdateServer.this)) {
							new MyTask().execute(rssFeed);
						} else {
							Toast.makeText(UpdateServer.this,
									"Error: No Network Connection!!!",
									Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(
								UpdateServer.this,
								"Error: Event starting time must be older than end time.",
								Toast.LENGTH_LONG).show();
					}

				}
			}

		});
		// go back to list
		Button button2 = (Button) findViewById(R.id.btnBack);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UpdateServer.this
						.getApplicationContext(), DataView.class);
				startActivity(intent);
			}
		});

	}// End of OnCreate

	public void init() {

		id = bundle.getInt("id");
		eventID = Integer.toString(id);
		name = bundle.getString("name");
		date = bundle.getString("date");
		startTime = bundle.getString("startTime");
		endTime = bundle.getString("endTime");
		location = bundle.getString("location");
		deleteFlag = bundle.getInt("deleteFlag");
		update_date = date;

		ConvertStdTime stDefault = new ConvertStdTime();
		update_startTime = stDefault.toMilitaryString(startTime);
		update_endTime = stDefault.toMilitaryString(endTime);
		update_location = location;
		delete_event = Integer.toString(deleteFlag);

		System.out.println("defaults are start " + update_startTime + " end "
				+ update_endTime + " date " + update_date + " update_location "
				+ update_location);

		System.out.println(" data from update server file: " + id.toString()
				+ "" + name + "" + deleteFlag);

		// capture our View elements
		mTitleDisplay = (TextView) findViewById(R.id.titleDisplay);
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		mTimeEndDisplay = (TextView) findViewById(R.id.timeEndDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);

		// set start time
		mTimeDisplay.setText(startTime);
		mTimeEndDisplay.setText(endTime);

		// set title
		mTitleDisplay.setText(name);

		// set Date
		String eventYear = date.substring(0, 4);
		String eventDay = date.substring(5, 7);
		eventMonth = date.substring(8, 10);
		mDateDisplay.setText(eventDay + "-" + eventMonth + "-" + eventYear);

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// add a click listener to the button
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// capture our View elements
		mPickTime = (Button) findViewById(R.id.pickTime);
		mEndPickTime = (Button) findViewById(R.id.pickTime2);
		mLocationDisplay = (TextView) findViewById(R.id.locationDisplay);
		mLocationDisplay.setText(" " + location);

		// add a click listener to the button
		mPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
		mEndPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID2);
			}
		});

		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.locations_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		int count = 0;

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			mLocationDisplay.setText(" " + location);

			if (count >= 1) {

				update_location = parent.getItemAtPosition(pos).toString();
				mLocationDisplay.setText(" " + update_location);
				Toast.makeText(
						parent.getContext(),
						"New location is "
								+ parent.getItemAtPosition(pos).toString(),
						Toast.LENGTH_LONG).show();

			} else {
				update_location = location;
			}
			count++;
		}

		public void onNothingSelected(AdapterView parent) {
			mLocationDisplay.setText(" " + location);
		}
	}

	public void updateDisplayTime() {

		startHour = mHour;
		startMin = mMinute;
		ConvertStdTime convertTime = new ConvertStdTime();
		mTimeDisplay.setText(convertTime.toStandard(mHour, mMinute));

	}

	public void updateDisplayEndTime2() {

		endHour = mHour;
		endMin = mMinute;
		ConvertStdTime convertTime = new ConvertStdTime();
		mTimeEndDisplay.setText(convertTime.toStandard(mHour, mMinute));

	}

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			mHour = hourOfDay;
			mMinute = minute;
			update_startTime = mHour + ":" + mMinute + ":00";
			updateDisplayTime();

		}

	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			mHour = hourOfDay;
			mMinute = minute;
			update_endTime = mHour + ":" + mMinute + ":00";
			updateDisplayEndTime2();
		}
	};

	// updates the date in the TextView
	private void updateDisplay() {
		mDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			Time chosenDate = new Time();
			chosenDate.set(dayOfMonth, monthOfYear, year);
			long dtDob = chosenDate.toMillis(true);
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

			String strDate = sdfDate.format(dtDob);

			update_date = strDate;
			System.out.println("update date" + update_date);
			if (update_date == null) {
				update_date = date;
			}

			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);

		case TIME_DIALOG_ID2:
			return new TimePickerDialog(this, mEndTimeSetListener, mHour,
					mMinute, false);

		}

		return null;
	}

	public void onCheckboxClicked(View v) {
		// Perform action on clicks, depending on whether it's now checked
		if (((CheckBox) v).isChecked()) {

			delete_event = "1";
			addBack = "0";
			Toast.makeText(this, "Event Removed From Schedule",
					Toast.LENGTH_SHORT).show();
		} else {

			delete_event = "0";
			addBack = "1";
			Toast.makeText(this, "Event Scheduled", Toast.LENGTH_SHORT).show();
		}
	}

	class MyTask extends AsyncTask<String, Void, JSONObject> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(UpdateServer.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// return Utils.getJSONString(params[0]);
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
			paramsx.add(new BasicNameValuePair("id", eventID));
			paramsx.add(new BasicNameValuePair("name", name));
			paramsx.add(new BasicNameValuePair("date", update_date));
			paramsx.add(new BasicNameValuePair("startTime", update_startTime));
			paramsx.add(new BasicNameValuePair("endTime", update_endTime));
			paramsx.add(new BasicNameValuePair("location", update_location));
			paramsx.add(new BasicNameValuePair("deleteEvent", delete_event));
			paramsx.add(new BasicNameValuePair("addBack", addBack));

			String paramx = "id=" + eventID + "&date" + update_date
					+ "&startTime=" + update_startTime + "&endTime="
					+ update_endTime + "&location=" + update_location
					+ "&deleteEvent=" + delete_event;

			System.out.println("paramax is " + paramx);
			json = jsonParser.getJSONFromUrl(rssFeed, paramsx);

			Log.v("JSON", json.toString());
			// String x = null;
			return json;

		}

		@Override
		protected void onPostExecute(JSONObject result) {
			if (null != pDialog && pDialog.isShowing()) {

				pDialog.dismiss();
			}
			System.out.println(" in Update the resut is " + result);
			// try{
			// JSONObject mainJson = new JSONObject();
			// JSONArray jsonArray = mainJson.getJSONArray(Time_CONFLICTS);
			// for (int i = 0; i < jsonArray.length(); i++) {
			// JSONObject objJson = jsonArray.getJSONObject(i);
			// // add a list object here
			//
			// String event=objJson.getString("event_name");
			// }
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			pDialog.dismiss();
			Intent intent = new Intent(
					UpdateServer.this.getApplicationContext(), DataView.class);

			startActivity(intent);

		}
	}

}
