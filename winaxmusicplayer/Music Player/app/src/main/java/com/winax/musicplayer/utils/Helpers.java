package com.winax.musicplayer.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winax.musicplayer.R;
import com.winax.musicplayer.activities.BaseActivity;

import java.util.HashMap;
import java.util.List;

public class Helpers {
    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    private Context context;

    public Helpers(Context context) {
        this.context = context;
    }

    public static void showAbout(AppCompatActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new AboutDialog().show(ft, "dialog_about");
    }

    public static String getATEKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false) ?
                "dark_theme" : "light_theme";
    }

    public static Typeface getFont(Context context) {
        String path = Extras.getInstance().getTypeface();
        return getTypeface(context, path);
    }

    public static Typeface getTypeface(Context context, String customFont) {
        Typeface tf = fontCache.get(customFont);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), customFont);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(customFont, tf);
        }
        return tf;
    }


    public static class AboutDialog extends DialogFragment {

        public AboutDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout aboutBodyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_about_dialog, null);

            TextView appversion = (TextView) aboutBodyView.findViewById(R.id.app_version_name);


            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                String version = pInfo.versionName;
                int versionCode = pInfo.versionCode;
                appversion.setText("Winax Player " + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return new AlertDialog.Builder(getActivity())
                    .setView(aboutBodyView)
                    .create();
        }

    }

}
