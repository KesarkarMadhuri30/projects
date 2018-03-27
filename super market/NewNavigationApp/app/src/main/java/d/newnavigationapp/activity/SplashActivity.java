package d.newnavigationapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import d.newnavigationapp.R;

public class SplashActivity extends AppCompatActivity {
    public long SPLASH_TIME_OUT = 6000;
    public Context context;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        linearLayout=(LinearLayout)findViewById(R.id.splashlayout);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.splash_screen);
        linearLayout.startAnimation(animation);
        isWorkingInternetPersent();
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();


            }
        },SPLASH_TIME_OUT);
    }

    public void isWorkingInternetPersent()
    {

        ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            init();
        }
        else
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);


            alertDialog.setTitle("Internet Connection...");
            alertDialog.setIcon(R.drawable.internetconnection);
            alertDialog.setMessage("Internet Connection Required..");


            alertDialog.setPositiveButton("Retry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            isWorkingInternetPersent();
                        }
                    });

            alertDialog.setCancelable(false);
            alertDialog.show();

        }

    }

}
