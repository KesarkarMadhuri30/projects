package com.winax.musicplayer.activities;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.winax.musicplayer.R;
import com.winax.musicplayer.fragments.EqualizerFragment;

public class EqualizerActivity extends BaseActivity implements ATEActivityThemeCustomizer{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        EqualizerFragment equalizerFragment=new EqualizerFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.equalizerContainer, equalizerFragment).commit();
    }

    @Override
    public int getActivityTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ? R.style.AppTheme_FullScreen_Dark : R.style.AppTheme_FullScreen_Light;
    }

}
