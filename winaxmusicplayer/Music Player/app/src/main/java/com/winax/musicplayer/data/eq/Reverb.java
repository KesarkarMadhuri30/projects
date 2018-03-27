package com.winax.musicplayer.data.eq;


import android.content.SharedPreferences;
import android.media.audiofx.PresetReverb;
import android.util.Log;

import com.winax.musicplayer.services.MediaPlayerSingleton;
import com.winax.musicplayer.utils.Extras;

import static com.winax.musicplayer.utils.Constants.PRESET_BOOST;

public class Reverb {
    private static PresetReverb presetReverb = null;
    private static int audioID = 0;

    public Reverb() {
    }

    public static void initReverb() {
        EndReverb();
        try {
            presetReverb = new PresetReverb(0, audioID);
            MediaPlayerSingleton.getInstance().getMediaPlayer().attachAuxEffect(presetReverb.getId());
            MediaPlayerSingleton.getInstance().getMediaPlayer().setAuxEffectSendLevel(1.0f);
            short str = (short) Extras.getInstance().saveEq().getInt(PRESET_BOOST, 0);
            if (str > 0) {
                setPresetReverbStrength(str);
            }else {
                setPresetReverbStrength((short) 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void EndReverb() {
        if (presetReverb != null) {
            presetReverb.release();
            presetReverb = null;
        }
    }

    public static void setPresetReverbStrength(short strength) {
        if (presetReverb != null && strength >= 0) {
            try {
                if (strength > 0) {
                    presetReverb.setPreset(strength);
                    saveReverb(strength);
                }
            } catch (IllegalArgumentException e) {
                Log.e("Reverb", "Reverb effect not supported");
            } catch (IllegalStateException e) {
                Log.e("Reverb", "Reverb cannot get strength supported");
            } catch (UnsupportedOperationException e) {
                Log.e("Reverb", "Reverb library not loaded");
            } catch (RuntimeException e) {
                Log.e("Reverb", "Reverb effect not found");
            }
        }
    }


    public static void saveReverb(short str) {
        SharedPreferences.Editor editor = Extras.getInstance().saveEq().edit();
        int strength = (int) str;
        editor.putInt(PRESET_BOOST, strength);
        editor.apply();
    }

    public static void setEnabled(boolean enabled) {
        if (presetReverb != null) {
            presetReverb.setEnabled(enabled);
        }
    }
}
