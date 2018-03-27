package com.winax.musicplayer.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.winax.musicplayer.MusicPlayer;
import com.winax.musicplayer.fragments.FavoritesFragment;
import com.winax.musicplayer.models.Song;

public class CreateFavlistDialog extends DialogFragment {

    public static CreateFavlistDialog newInstance() {
        return newInstance((Song) null);
    }

    public static CreateFavlistDialog newInstance(Song song) {
        long[] songs;
        if (song == null) {
            songs = new long[0];
        } else {
            songs = new long[1];
            songs[0] = song.id;
        }
        return newInstance(songs);
    }

    public static CreateFavlistDialog newInstance(long[] songList) {
        CreateFavlistDialog dialog = new CreateFavlistDialog();
        Bundle bundle = new Bundle();
        bundle.putLongArray("songs", songList);
        dialog.setArguments(bundle);
        return dialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity()).positiveText("Create").negativeText("Cancel").input("Enter Favorite list name", "", false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                long[] songs = getArguments().getLongArray("songs");
                long favistId = MusicPlayer.createFavlist(getActivity(), input.toString());

                if (favistId != -1) {
                    if (songs != null && songs.length != 0)
                        MusicPlayer.addToFavlist(getActivity(), songs, favistId);
                    else
                        Toast.makeText(getActivity(), "Created favorite list", Toast.LENGTH_SHORT).show();
                    if (getParentFragment() instanceof FavoritesFragment) {
                        ((FavoritesFragment) getParentFragment()).updateFavlists(favistId);
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to create favorite list", Toast.LENGTH_SHORT).show();
                }

            }
        }).build();
    }



}
