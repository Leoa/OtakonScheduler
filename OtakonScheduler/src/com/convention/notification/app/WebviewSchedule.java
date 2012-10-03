package com.convention.notification.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class WebviewSchedule  extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviewschedule);
		WebView webview =(WebView)findViewById(R.id.webView);
		webview.loadUrl("http://leobee.com/android/push/login/scheduleView.php");

	}

}
