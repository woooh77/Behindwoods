package com.viki.behindwoods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen_activity extends Activity{
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_layout);
 
        new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {
            	ConnectivityManager cm =
            	        (ConnectivityManager)SplashScreen_activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            	 
            	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            	boolean isConnected = activeNetwork != null &&
            	                      activeNetwork.isConnectedOrConnecting();
            	if(isConnected) {
            		Intent i = new Intent(SplashScreen_activity.this, News_activity.class);
            		startActivity(i);
            	}
            	else
            		Toast.makeText(SplashScreen_activity.this, "No Internet Connection. Exiting...", Toast.LENGTH_LONG).show();
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
