package com.winax.musicplayer.data.eq;


import android.content.SharedPreferences;
//import android.media.audiofx.BassBoost;
import android.media.audiofx.BassBoost;
import android.util.Log;

import com.winax.musicplayer.utils.Extras;

import static com.winax.musicplayer.utils.Constants.BASSBOOST_STRENGTH;
import static com.winax.musicplayer.utils.Constants.BASS_BOOST;

public class BassBoosts {
    private static BassBoost bassBoost = null;

    public BassBoosts() {
    }

    public static void initBass(int audioID) {
        EndBass();
        try {
            bassBoost = new BassBoost(0, audioID);
            short savestr = (short) Extras.getInstance().saveEq().getInt(BASS_BOOST, 0);
            if (savestr > 0) {
                setBassBoostStrength(savestr);
            }else {
                setBassBoostStrength((short) 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void EndBass() {
        if (bassBoost != null) {
            bassBoost.release();
            bassBoost = null;
        }
    }

    public static void setBassBoostStrength(short strength) {
        if (bassBoost != null && bassBoost.getStrengthSupported() &&  strength >= 0) {
            try {
                if (strength <= BASSBOOST_STRENGTH) {
                    bassBoost.setStrength(strength);
                    saveBass(strength);
                }
            } catch (IllegalArgumentException e) {
                Log.e("BassBoosts", "Bassboost effect not supported");
            } catch (IllegalStateException e) {
                Log.e("BassBoosts", "Bassboost cannot get strength supported");
            } catch (UnsupportedOperationException e) {
                Log.e("BassBoosts", "Bassboost library not loaded");
            } catch (RuntimeException e) {
                Log.e("BassBoosts", "Bassboost effect not found");
            }
        }
    }


    public static void saveBass(short strength) {
        SharedPreferences.Editor editor = Extras.getInstance().saveEq().edit();
        int str = (int) strength;
        editor.putInt(BASS_BOOST, str);
        editor.commit();
    }

    public static void setEnabled(boolean enabled) {
        if (bassBoost != null) {
            bassBoost.setEnabled(enabled);
        }
    }
}