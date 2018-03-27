package com.winax.musicplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.winax.musicplayer.R;

public class SplashActivity extends AppCompatActivity {
    private long SPLASH_TIME_OUT = 4000;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        init();
    }
    private void init() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
              //  ConnectivityManager connMgr = (ConnectivityManager)
               //         getSystemService(Context.CONNECTIVITY_SERVICE);
               // NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
               // if ((networkInfo != null && networkInfo.isConnected())) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(context, "welcome", Toast.LENGTH_SHORT).show();
                //}
                //else {
                  //  Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
               // }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
