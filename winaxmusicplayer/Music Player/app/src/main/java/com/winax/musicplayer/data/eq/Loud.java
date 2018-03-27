package com.winax.musicplayer.data.eq;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.util.Log;

import com.winax.musicplayer.utils.Extras;

import static com.winax.musicplayer.utils.Constants.GAIN_MAX;
import static com.winax.musicplayer.utils.Constants.LOUD_BOOST;

public class Loud {

    private static LoudnessEnhancer loudnessEnhancer = null;

    public Loud() {
    }

    /*
     Init LoudnessEnhancer
    */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void initLoudnessEnhancer(int audioID) {
        EndLoudnessEnhancer();
        try {
            loudnessEnhancer = new LoudnessEnhancer(audioID);
            int loud = Extras.getInstance().saveEq().getInt(LOUD_BOOST, 0);
            if (loud > 0) {
                setLoudnessEnhancerGain(loud);
            }else {
                setLoudnessEnhancerGain(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setLoudnessEnhancerGain(int gain) {
        if (loudnessEnhancer != null && gain >= 0) {
            try {
                if (gain <= GAIN_MAX) {
                    loudnessEnhancer.setTargetGain(gain);
                    saveLoudnessEnhancer(gain);
                }
            } catch (IllegalArgumentException e) {
                Log.e("Loud", "Loud effect not supported");
            } catch (IllegalStateException e) {
                Log.e("Loud", "Loud cannot get gain supported");
            } catch (UnsupportedOperationException e) {
                Log.e("Loud", "Loud library not loaded");
            } catch (RuntimeException e) {
                Log.e("Loud", "Loud effect not found");
            }
        }

    }

    public static void EndLoudnessEnhancer() {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.release();
            loudnessEnhancer = null;
        }
    }


    public static void saveLoudnessEnhancer(int Gain) {
        SharedPreferences.Editor editor = Extras.getInstance().saveEq().edit();
        editor.putInt(LOUD_BOOST, Gain);
        editor.apply();
    }

    public static void setEnabled(boolean enabled) {
        if (loudnessEnhancer != null) {
            loudnessEnhancer.setEnabled(enabled);
        }
    }
}