package com.convention.notification.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class Main extends Activity {
    /** Called when the activity is first created. */
	
	
	private static String LOG_APP_TAG = "tag";
	String FILENAME = "hello_file";
	FileOutputStream fos=null;
	String x;
	Intent intent;
	String msg;
	private ProgressDialog pd;
	WakeLock wl;
	PowerManager pm;
	String SetAsync;
	Singleton singlt;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
     // start wakelock
    	pm = (PowerManager)getSystemService(Context.POWER_SERVICE) ;
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Full wake lock");
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolvetimeconflict);
        
     //Acquire wake lock 
        wl.acquire();
        
      // Start dialogue  
        pd=ProgressDialog.show(this, "Loading ...", "");
      
        //is network available?
        Boolean net =isNetworkAvailable();
        
        Log.v("networks is ", net.toString());
        
        //IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
      ConnectionMonitor connectionMonitor = new ConnectionMonitor();
       registerReceiver(connectionMonitor, filter);

        
      // Async Task
	       DownloadWebPageTask task = new DownloadWebPageTask();
	      
		   task.execute(new String[] { "http://leobee.com/android/push/login/schedule.php" });
		
    }
    
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
    	
		@Override
		protected String doInBackground(String... urls) {
			
			String response = "";
			for (String url : urls) {
				
				
				
				HttpGet httpGet = new HttpGet(url);
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				int timeoutConnection = 3000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 3000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);



				
				try {
					
					HttpResponse execute = httpClient.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						
						response += s;
					}

				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
			}
			
			return response;
		}


    @Override
	protected void onPostExecute(String result) {

    		  x = result;

    		try {
//flush  file ?//////////////////////////////////////////////////////////////////////
			    File myDir = new File(getApplicationContext().getFilesDir().getAbsolutePath());
			    String s = "";

			    FileWriter fw = new FileWriter(myDir + "/Schedule.txt");
			    fw.write(x);
			    fw.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(LOG_APP_TAG,e.getMessage());
				e.printStackTrace();
			}

    		Log.v("values",x);
    		msg =x;
    		pd.dismiss();
    		wl.release();
    		Log.v( "Wake lock released", "relased");
    		Intent intent = new Intent(getApplicationContext(),DataView.class);
				//intent.putExtra("value", item);
				startActivity(intent);
    		//Boolean x= true;
    		//Singleton.getInstance().setgotoServer(x);
    		//Log.v("Bool values", Singleton.getInstance().getgoToServer()?"yes":"no");

   	     
    }


   }
    
    
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) 
        	getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    
    private class ConnectionMonitor extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
                return;
            Boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false); 
            NetworkInfo aNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);                     

            if (!noConnectivity)
            {
                if ((aNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) ||
                    (aNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI))
                {
                   Log.v("connection", noConnectivity.toString()); // start your service stuff  here
                }
            }

            else
            {
                if ((aNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) ||
                        (aNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI))
                {
                	Log.v("connection", noConnectivity.toString()); // stop your service stuff here
                }
            }
        }
    }

}