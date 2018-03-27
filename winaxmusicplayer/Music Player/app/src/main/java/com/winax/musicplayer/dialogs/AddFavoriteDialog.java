package com.winax.musicplayer.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.winax.musicplayer.MusicPlayer;
import com.winax.musicplayer.dataloaders.FavlistLoader;
import com.winax.musicplayer.models.Favlist;
import com.winax.musicplayer.models.Song;

import java.util.List;

public class AddFavoriteDialog extends DialogFragment {
    public static AddFavoriteDialog newInstance(Song song) {
        long[] songs = new long[1];
        songs[0] = song.id;
        return newInstance(songs);
    }
    public static AddFavoriteDialog newInstance(long[] songList) {
        AddFavoriteDialog dialog = new AddFavoriteDialog();
        Bundle bundle = new Bundle();
        bundle.putLongArray("songs", songList);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<Favlist> favlists = FavlistLoader.getFavlists(getActivity(),true);
        CharSequence[] chars = new CharSequence[favlists.size() + 1];
        chars[0] = "Create new favorite list";

        for (int i = 0; i < favlists.size(); i++) {
            chars[i + 1] = favlists.get(i).name;
        }
        return new MaterialDialog.Builder(getActivity()).title("Add to Favorite list").items(chars).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                long[] songs = getArguments().getLongArray("songs");
                if (which == 0) {
                    CreateFavlistDialog.newInstance(songs).show(getActivity().getSupportFragmentManager(), "CREATE_FAVORITE_LIST");
                    return;
                }

                MusicPlayer.addToFavlist(getActivity(), songs, favlists.get(which - 1).id);
                dialog.dismiss();

            }
        }).build();
    }
}