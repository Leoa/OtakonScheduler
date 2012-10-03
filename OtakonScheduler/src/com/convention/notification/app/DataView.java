package com.convention.notification.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class DataView extends Activity implements OnItemClickListener {

	private static final String rssFeed = "http://leobee.com/android/push/login/schedule.php";
	private static final String AddEvent = "http://leobee.com/android/push/login/deleteUpdate.php";

	private static final String ARRAY_NAME = "events";
	private static final String EVENT_NAME = "event_name";
	private static final String EVENT_ID = "event_id";
	private static final String EVENT_DATE = "event_date";
	private static final String EVENT_START = "event_start";
	private static final String EVENT_END = "event_end";
	private static final String EVENT_LOCATION = "event_location";
	private static final String EVENT_DELETE_FLAG = "event_delete_flag";

	List<Item> arrayOfList;
	ListView listView;
	NewsRowAdapter objAdapter;
	List<Integer> deleteList;
	String undelete;
	String eventNameDeleted;
	String eventIdDeleted;
	String eventDateDeleted;
	String eventStartDeleted;
	String eventEndDeleted;
	String eventLocationDeleted;
	JSONObject json;
	String addBack = "0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedulelistview);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		deleteList = new ArrayList<Integer>();

		// List <Item> arrayofList = new ArrayList<Item>();
		// List with generic type of Item, from class Item, is assigned and
		// arraylist that is strict typed to Item.
		// Array list is a collection type, like Linked List ect... Item is the
		// type of element in the collection
		// Collection defines the objects features,sort manor basics are Map,
		// list, Arraylist
		// Generics strict type the list of object ex Item in List<Item>
		// The ArrayList class extends AbstractList and implements the List
		// interface. ArrayList supports dynamic
		// arrays that can grow as needed.

		arrayOfList = new ArrayList<Item>();

		if (Utils.isNetworkAvailable(DataView.this)) {
			new MyTask().execute(rssFeed);
		} else {
			showToast("No Network Connection!!!");
		}

	}

	// My AsyncTask start...

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(DataView.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showToast("No data found from web!!!");
				DataView.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(ARRAY_NAME);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);

						Item objItem = new Item();

						// here objItem is reciveing and processing data from
						// JSON string text
						objItem.setId(objJson.getInt(EVENT_ID));// names from
																// feed is in
																// static vars
																// at top
						objItem.setName(objJson.getString(EVENT_NAME));
						objItem.setDate(objJson.getString(EVENT_DATE));
						objItem.setStartTime(objJson.getString(EVENT_START));
						objItem.setEndTime(objJson.getString(EVENT_END));
						objItem.setLocation(objJson.getString(EVENT_LOCATION));
						objItem.setDeleteFlag(objJson.getInt(EVENT_DELETE_FLAG));

						arrayOfList.add(objItem);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				for (int i = 0; i < arrayOfList.size(); i++) {

					Item deletflagCheck = arrayOfList.get(i);

					if (deletflagCheck.getDeleteFlag() == 1) {

						deleteList.add(deletflagCheck.getId());
					} else {

					}

				}

			}
			setAdapterToListview();

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View viewDel, int position,
			long id) {

		Item deletflagCheck = arrayOfList.get(position);

		if (deletflagCheck.getDeleteFlag() == 1) {

			showDeleteDialog(position);

		} else {

			Intent intent = new Intent(DataView.this.getApplicationContext(),
					UpdateServer.class);

			for (int i = 0; i < 1; i++) {
				Item item = arrayOfList.get(position);
				intent.putExtra("id", item.getId());
				intent.putExtra("name", item.getName());
				intent.putExtra("date", item.getDate());
				intent.putExtra("startTime", item.getStartTime());
				intent.putExtra("endTime", item.getEndTime());
				intent.putExtra("location", item.getLocation());
				intent.putExtra("deleteFlag", item.getDeleteFlag());

			}
			startActivity(intent);
		}

	}

	private void showDeleteDialog(final int position) {
		Item item = arrayOfList.get(position);
		eventNameDeleted = item.getName();
		// eventIdDeleted=item.getId();
		eventDateDeleted = item.getDate();
		eventStartDeleted = item.getStartTime();
		eventEndDeleted = item.getEndTime();
		eventLocationDeleted = item.getLocation();

		AlertDialog alertDialog = new AlertDialog.Builder(DataView.this)
				.create();
		alertDialog.setTitle(eventNameDeleted);
		alertDialog.setMessage("Add Event Back To Schedule?");
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				undelete = Integer.toString(position);
				// objAdapter.notifyDataSetChanged();
				addBack = "1";
				System.out.println("addBack is " + addBack);
				if (Utils.isNetworkAvailable(DataView.this)) {
					new AddEventTask().execute(AddEvent);

				} else {
					showToast("No Network Connection!!!");
				}

			}
		});
		alertDialog.show();

	}

	public void setAdapterToListview() {

		objAdapter = new NewsRowAdapter(DataView.this,
				R.layout.schedulelistrow, arrayOfList, deleteList);
		objAdapter.notifyDataSetChanged();
		listView.setAdapter(objAdapter);
	}

	public void showToast(String msg) {
		Toast.makeText(DataView.this, msg, Toast.LENGTH_LONG).show();
	}

	class AddEventTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(DataView.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {

			String zero = "0";

			ConvertStdTime shCST = new ConvertStdTime();
			String startHour = shCST.toMilitaryString(eventStartDeleted);
			String endHour = shCST.toMilitaryString(eventEndDeleted);

			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> addBacktoSchedule = new ArrayList<NameValuePair>();
			addBacktoSchedule.add(new BasicNameValuePair("id", undelete));
			addBacktoSchedule.add(new BasicNameValuePair("deleteEvent", zero));
			addBacktoSchedule.add(new BasicNameValuePair("name",
					eventNameDeleted));
			addBacktoSchedule.add(new BasicNameValuePair("date",
					eventDateDeleted));
			addBacktoSchedule
					.add(new BasicNameValuePair("startTime", startHour));
			addBacktoSchedule.add(new BasicNameValuePair("endTime", endHour));
			addBacktoSchedule.add(new BasicNameValuePair("location",
					eventLocationDeleted));
			addBacktoSchedule.add(new BasicNameValuePair("addBack", addBack));
			String paramx = "id=" + undelete + "&deleteEvent=" + zero
					+ " &addback= " + addBack;
			System.out.println(paramx);
			json = jsonParser.getJSONFromUrl(AddEvent, addBacktoSchedule);

			Log.e("JSON", json.toString());
			// String x = null;
			// return json;
			return paramx;

		}

		@Override
		protected void onPostExecute(String result) {

			// System.out.println("json from delete is "+
			// json+" result from delete is "+ result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showToast("No data found from web!!!");
				DataView.this.finish();
			} else {

				try {

					String message = json.getString("msg");
					Toast.makeText(DataView.this, message, Toast.LENGTH_LONG)
							.show();

					int dialog = json.getInt("dialog");
					String time_conflicts = json.getString("time_conflicts");
					String deleted_event = json.getString("the_deleted_event");
					
					if (dialog == 1) {

						deletedEvent(time_conflicts, deleted_event);

					}
					// Toast.makeText(DataView.this,eventNameDeleted+" "+
					// message, Toast.LENGTH_LONG).show();

					// DataView.this.finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			 Intent intent = new Intent(DataView.this.getApplicationContext(),DataView.class);
			 startActivity(intent);

			// Toast.makeText(DataView.this,
			// eventNameDeleted+" added back to schedule.",
			// Toast.LENGTH_LONG).show();

		}
	}

	public void deletedEvent(final String time_conflicts,
			final String deleted_event) {

		AlertDialog alertDialog = new AlertDialog.Builder(DataView.this)
				.create();
		alertDialog.setTitle("Fix time conflict with " + eventNameDeleted
				+ " ?");
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Toast.makeText(DataView.this,
				// eventNameDeleted+" has time conflict.",
				// Toast.LENGTH_LONG).show();

				Intent intent = new Intent(DataView.this
						.getApplicationContext(), ResolveTimeConflict.class);
				intent.putExtra("time_conflicts", time_conflicts);
				intent.putExtra("deleted_event", deleted_event);

				startActivity(intent);
//********objAdapter.clear(); Crash when clear is here**************************
				
				
			}
		});
		alertDialog.show();

	}

}