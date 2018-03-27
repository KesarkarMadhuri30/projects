package com.winax.musicplayer.dataloaders;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.winax.musicplayer.activities.FavlistDetailActivity;
import com.winax.musicplayer.models.Favlist;
import com.winax.musicplayer.utils.TimberUtils;

import java.util.ArrayList;
import java.util.List;

public class FavlistLoader {
    static ArrayList<Favlist> mFavList;
    private static Cursor mCursor;

    public static List<Favlist> getFavlists(Context context,boolean defaultIncluded) {

        mFavList = new ArrayList<>();

        if (defaultIncluded)
            makeDefaultFavlists(context);

        mCursor = makeFavlistCursor(context);

        if (mCursor != null && mCursor.moveToFirst()) {
            do {

                final long id = mCursor.getLong(0);

                final String name = mCursor.getString(1);

                final int songCount = TimberUtils.getSongCountForFavlist(context, id);

                final Favlist favlist = new Favlist(id, name, songCount);

                mFavList.add(favlist);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mFavList;
    }

    private static void makeDefaultFavlists(Context context) {
        final Resources resources = context.getResources();

        /* Last added list */
        final Favlist lastAdded = new Favlist(TimberUtils.PlaylistType.LastAdded.mId,
                resources.getString(TimberUtils.PlaylistType.LastAdded.mTitleId), -1);
        mFavList.add(lastAdded);

        /* Recently Played */
     /*   final Favlist recentlyPlayed = new Favlist(TimberUtils.PlaylistType.RecentlyPlayed.mId,
                resources.getString(TimberUtils.PlaylistType.RecentlyPlayed.mTitleId), -1);
        mFavList.add(recentlyPlayed);*/

        /* Top Tracks */
        /*final Favlist topTracks = new Favlist(TimberUtils.PlaylistType.TopTracks.mId,
                resources.getString(TimberUtils.PlaylistType.TopTracks.mTitleId), -1);
        mFavList.add(topTracks);*/
    }


    public static final Cursor makeFavlistCursor(final Context context) {
        return context.getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.PlaylistsColumns.NAME
                }, null, null, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);
    }

    public static void deleteFavlists(Context context, long playlistId) {
        Uri localUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("_id IN (");
        localStringBuilder.append((playlistId));
        localStringBuilder.append(")");
        context.getContentResolver().delete(localUri, localStringBuilder.toString(), null);
    }
}
