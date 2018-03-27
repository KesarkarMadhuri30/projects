/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.winax.musicplayer.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.audiofx.AudioEffect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.winax.musicplayer.R;
import com.winax.musicplayer.activities.BaseActivity;
import com.winax.musicplayer.activities.EqualizerActivity;
import com.winax.musicplayer.activities.FavlistDetailActivity;
import com.winax.musicplayer.activities.MainActivity;
import com.winax.musicplayer.activities.NowPlayingActivity;
import com.winax.musicplayer.activities.PlaylistDetailActivity;
import com.winax.musicplayer.activities.SearchActivity;
import com.winax.musicplayer.activities.SettingsActivity;
import com.winax.musicplayer.fragments.AlbumDetailFragment;
import com.winax.musicplayer.fragments.ArtistDetailFragment;
import com.winax.musicplayer.nowplaying.Timber1;
import com.winax.musicplayer.nowplaying.Timber2;
import com.winax.musicplayer.nowplaying.Timber3;
import com.winax.musicplayer.nowplaying.Timber4;

import java.util.ArrayList;
import java.util.List;

import static com.winax.musicplayer.utils.Constants.EQ;

public class NavigationUtils {

    @TargetApi(21)
    public static void navigateToAlbum(Activity context, long albumID, Pair<View, String> transitionViews) {

        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (TimberUtils.isLollipop() && transitionViews != null && PreferencesUtility.getInstance(context).getAnimations()) {
            Transition changeImage = TransitionInflater.from(context).inflateTransition(R.transition.image_transform);
            transaction.addSharedElement(transitionViews.first, transitionViews.second);
            fragment = AlbumDetailFragment.newInstance(albumID, true, transitionViews.second);
            fragment.setSharedElementEnterTransition(changeImage);
        } else {
            transaction.setCustomAnimations(R.anim.activity_fade_in,
                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = AlbumDetailFragment.newInstance(albumID, false, null);
        }
        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null).commit();

    }

    @TargetApi(21)
    public static void navigateToArtist(Activity context, long artistID, Pair<View, String> transitionViews) {

        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (TimberUtils.isLollipop() && transitionViews != null && PreferencesUtility.getInstance(context).getAnimations()) {
            Transition changeImage = TransitionInflater.from(context).inflateTransition(R.transition.image_transform);
            transaction.addSharedElement(transitionViews.first, transitionViews.second);
            fragment = ArtistDetailFragment.newInstance(artistID, true, transitionViews.second);
            fragment.setSharedElementEnterTransition(changeImage);
        } else {
            transaction.setCustomAnimations(R.anim.activity_fade_in,
                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = ArtistDetailFragment.newInstance(artistID, false, null);
        }
        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null).commit();

    }

    public static void goToArtist(Context context, long artistId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_ARTIST);
        intent.putExtra(Constants.ARTIST_ID, artistId);
        context.startActivity(intent);
    }

    public static void goToAlbum(Context context, long albumId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_ALBUM);
        intent.putExtra(Constants.ALBUM_ID, albumId);
        context.startActivity(intent);
    }

    public static void goToLyrics(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_LYRICS);
        context.startActivity(intent);
    }

    public static void navigateToNowplaying(Activity context, boolean withAnimations) {

        final Intent intent = new Intent(context, NowPlayingActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        context.startActivity(intent);
    }

    public static void navigateToEqualizer(Activity context, boolean withAnimations) {

        final Intent intent = new Intent(context, EqualizerActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        context.startActivity(intent);
    }

    public static Intent getNowPlayingIntent(Context context) {

        final Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_NOWPLAYING);
        return intent;
    }

    public static void navigateToSettings(Activity context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.NAVIGATE_SETTINGS);
        context.startActivity(intent);
    }

    public static void navigateToEqualizer(Activity context) {
        final Intent intent = new Intent(context, EqualizerActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.NAVIGATE_EQUALIZER);
        context.startActivity(intent);
       /* Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        if (intent.getAction() != null && NavigationUtils.isActivityPresent(, intent)){
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, musicService.audioSession());
            startActivityForResult(intent, EQ);
        }else {
            Toast.makeText(this, "No app found to handle equalizer", Toast.LENGTH_SHORT).show();
        }*/
    }

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(Constants.NAVIGATE_SEARCH);
        context.startActivity(intent);
    }

    @TargetApi(21)
    public static void navigateToPlaylistDetail(Activity context, String action, long firstAlbumID, String playlistName, int foregroundcolor, long playlistID, ArrayList<Pair> transitionViews) {
        final Intent intent = new Intent(context, PlaylistDetailActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(action);
        intent.putExtra(Constants.PLAYLIST_ID, playlistID);
        intent.putExtra(Constants.PLAYLIST_FOREGROUND_COLOR, foregroundcolor);
        intent.putExtra(Constants.ALBUM_ID, firstAlbumID);
        intent.putExtra(Constants.PLAYLIST_NAME, playlistName);

        if (TimberUtils.isLollipop() && PreferencesUtility.getInstance(context).getAnimations()) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.getInstance(), transitionViews.get(0), transitionViews.get(1), transitionViews.get(2));
            context.startActivityForResult(intent,Constants.ACTION_DELETE_PLAYLIST, options.toBundle());
        } else {
            context.startActivityForResult(intent,Constants.ACTION_DELETE_PLAYLIST);
        }
    }



    public static Intent getNavigateToStyleSelectorIntent(Activity context, String what) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.SETTINGS_STYLE_SELECTOR);
        intent.putExtra(Constants.SETTINGS_STYLE_SELECTOR_WHAT, what);
        return intent;
    }

    public static Fragment getFragmentForNowplayingID(String fragmentID) {
        switch (fragmentID) {
            case Constants.TIMBER1:
                return new Timber1();
            case Constants.TIMBER2:
                return new Timber2();
            case Constants.TIMBER3:
                return new Timber3();
            case Constants.TIMBER4:
                return new Timber4();
            default:
                return new Timber1();
        }
    }

    public static boolean isActivityPresent(Context context, Intent intent){
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }



        @TargetApi(21)
        public static void navigateToFavlistDetail(Activity context, String action, long firstAlbumID, String favlistName, int foregroundcolor, long favlistID, ArrayList<Pair> transitionViews) {
            final Intent intent = new Intent(context, FavlistDetailActivity.class);
            if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
            intent.setAction(action);
            intent.putExtra(Constants.PLAYLIST_ID, favlistID);
            intent.putExtra(Constants.PLAYLIST_FOREGROUND_COLOR, foregroundcolor);
            intent.putExtra(Constants.ALBUM_ID, firstAlbumID);
            intent.putExtra(Constants.PLAYLIST_NAME, favlistName);

            if (TimberUtils.isLollipop() && PreferencesUtility.getInstance(context).getAnimations()) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.getInstance(), transitionViews.get(0), transitionViews.get(1), transitionViews.get(2));
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }


        }

}
