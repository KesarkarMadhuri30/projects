package com.winax.musicplayer.utils;


import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.winax.musicplayer.MusicXApplication;

import static com.winax.musicplayer.utils.Constants.BlackTheme;
import static com.winax.musicplayer.utils.Constants.DarkTheme;
import static com.winax.musicplayer.utils.Constants.EQSWITCH;
import static com.winax.musicplayer.utils.Constants.LightTheme;
import static com.winax.musicplayer.utils.Constants.PRESET_POS;
import static com.winax.musicplayer.utils.Constants.TRYPEFACE_PATH;

public class Extras {
    private static Extras instance;
    public Extras() {

    }

    public static Extras init() {
        if (instance == null){
            instance = new Extras();
        }
        return instance;
    }

    public static Extras getInstance(){
        return instance;
    }

    public SharedPreferences getmPreferences() {
        return MusicXApplication.getmPreferences();
    }

    public boolean geteqSwitch() {
        return saveEq().getBoolean(EQSWITCH, false);
    }

    public SharedPreferences saveEq() {
        return MusicXApplication.getEqPref();
    }


    public void eqSwitch(Boolean torf) {
        SharedPreferences.Editor editor = saveEq().edit();
        editor.putBoolean(EQSWITCH, torf);
        editor.commit();
    }

    ///////////////// Typeface path /////////////

    public void saveTypeface(String path){
        SharedPreferences.Editor editor = MusicXApplication.getmPreferences().edit();
        editor.putString(TRYPEFACE_PATH,path);
        editor.commit();
    }

    public String getTypeface(){
        return MusicXApplication.getmPreferences().getString(TRYPEFACE_PATH, Typeface.DEFAULT.toString());
    }

    /////////////////// Theme Pref //////////////////////

    public boolean getDarkTheme() {
        return getmPreferences().getBoolean(DarkTheme, false);
    }

    public boolean getLighTheme() {
        return getmPreferences().getBoolean(LightTheme, false);
    }

    public boolean getBlackTheme() {
        return getmPreferences().getBoolean(BlackTheme, false);
    }

    //////////// Preset Pos ///////////////

    public void savePresetPos(int position){
        SharedPreferences.Editor editor = MusicXApplication.getmPreferences().edit();
        editor.putInt(PRESET_POS, position);
        editor.commit();
    }

    public int getPresetPos(){
        return MusicXApplication.getmPreferences().getInt(PRESET_POS, 0);
    }


}
