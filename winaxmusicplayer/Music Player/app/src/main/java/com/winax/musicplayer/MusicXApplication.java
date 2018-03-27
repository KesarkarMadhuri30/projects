package com.winax.musicplayer;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.winax.musicplayer.utils.Extras;

import static com.winax.musicplayer.utils.Constants.SAVE_EQ;

public class MusicXApplication extends Application{
    private static SharedPreferences mPreferences, metaData, eqPref;
    private MusicXApplication instance;
    private Context mContext;

    public static SharedPreferences getEqPref() {
        return eqPref;
    }

    public static SharedPreferences getmPreferences() {
        return mPreferences;
    }

    public static MusicXApplication get(Activity activity) {
        return (MusicXApplication) activity.getApplication();
    }


    public static SharedPreferences getMetaData() {
        return metaData;
    }


    public Context getContext() {
        return mContext;
    }

    public MusicXApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;
        //createDirectory();
        Extras.init();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
      //  metaData = mContext.getSharedPreferences(TAG_METADATA, MODE_PRIVATE);
        eqPref = mContext.getSharedPreferences(SAVE_EQ, MODE_PRIVATE);
      //  Extras.getInstance().setwidgetPosition(100);
        Extras.getInstance().eqSwitch(false);
      /*  mBilling.addPlayStoreListener(new PlayStoreListener() {
            @Override
            public void onPurchasesChanged() {
                Toast.makeText(MusicXApplication.this, "Play Store: purchases have changed!", Toast.LENGTH_LONG).show();
            }
        });
        Fabric.with(this, new Crashlytics());
        AndroidNetworking.initialize(this);*/
    }

   /* private void createDirectory() {
        if (permissionManager.writeExternalStorageGranted(mContext)) {
            Helper.createAppDir("Lyrics");
            Helper.createAppDir(".AlbumArtwork");
            Helper.createAppDir(".ArtistArtwork");
        } else {
            Log.d("oops error", "Failed to create directory");
        }
    }*/


}