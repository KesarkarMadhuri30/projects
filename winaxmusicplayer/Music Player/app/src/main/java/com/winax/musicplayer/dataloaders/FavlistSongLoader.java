package com.winax.musicplayer.dataloaders;


import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.MediaStore;

import com.winax.musicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

public class FavlistSongLoader {
    private static Cursor mCursor;

    private static long mFavlistID;
    private static Context context;


    public static List<Song> getSongsInFavlist(Context mContext, long favlistID) {
        ArrayList<Song> mSongList = new ArrayList<>();

        context = mContext;
        mFavlistID = favlistID;

        final int favlistCount = countfavlist(context, mFavlistID);

        mCursor = makePlaylistSongCursor(context, mFavlistID);

        if (mCursor != null) {
            boolean runCleanup = false;
            if (mCursor.getCount() != favlistCount) {
                runCleanup = true;
            }

            if (!runCleanup && mCursor.moveToFirst()) {
                final int playOrderCol = mCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.PLAY_ORDER);

                int lastPlayOrder = -1;
                do {
                    int playOrder = mCursor.getInt(playOrderCol);
                    if (playOrder == lastPlayOrder) {
                        runCleanup = true;
                        break;
                    }
                    lastPlayOrder = playOrder;
                } while (mCursor.moveToNext());
            }

            if (runCleanup) {

                cleanupFavlist(context, mFavlistID, mCursor);

                mCursor.close();
                mCursor = makePlaylistSongCursor(context, mFavlistID);
                if (mCursor != null) {
                }
            }
        }

        if (mCursor != null && mCursor.moveToFirst()) {
            do {

                final long id = mCursor.getLong(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID));

                final String songName = mCursor.getString(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE));

                final String artist = mCursor.getString(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST));

                final long albumId = mCursor.getLong(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));

                final long artistId = mCursor.getLong(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID));

                final String album = mCursor.getString(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));

                final long duration = mCursor.getLong(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION));

                final int durationInSecs = (int) duration / 1000;

                final int tracknumber = mCursor.getInt(mCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK));

                final Song song = new Song(id, albumId, artistId, songName, artist, album, durationInSecs, tracknumber);

                mSongList.add(song);
            } while (mCursor.moveToNext());
        }
        // Close the cursor
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mSongList;
    }

    private static void cleanupFavlist(final Context context, final long playlistId,
                                        final Cursor cursor) {
        final int idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID);
        final Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newDelete(uri).build());

        final int YIELD_FREQUENCY = 100;

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                final ContentProviderOperation.Builder builder =
                        ContentProviderOperation.newInsert(uri)
                                .withValue(MediaStore.Audio.Playlists.Members.PLAY_ORDER, cursor.getPosition())
                                .withValue(MediaStore.Audio.Playlists.Members.AUDIO_ID, cursor.getLong(idCol));

                if ((cursor.getPosition() + 1) % YIELD_FREQUENCY == 0) {
                    builder.withYieldAllowed(true);
                }
                ops.add(builder.build());
            } while (cursor.moveToNext());
        }

        try {
            context.getContentResolver().applyBatch(MediaStore.AUTHORITY, ops);
        } catch (RemoteException e) {
        } catch (OperationApplicationException e) {
        }
    }


    private static int countfavlist(final Context context, final long playlistId) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(
                    MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                    new String[]{
                            MediaStore.Audio.Playlists.Members.AUDIO_ID,
                    }, null, null,
                    MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER);

            if (c != null) {
                return c.getCount();
            }
        } finally {
            if (c != null) {
                c.close();
                c = null;
            }
        }

        return 0;
    }


    public static final Cursor makePlaylistSongCursor(final Context context, final Long playlistID) {
        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        return context.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID),
                new String[]{
                        MediaStore.Audio.Playlists.Members._ID,
                        MediaStore.Audio.Playlists.Members.AUDIO_ID,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.ARTIST_ID,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.DURATION,
                        MediaStore.Audio.AudioColumns.TRACK,
                        MediaStore.Audio.Playlists.Members.PLAY_ORDER,
                }, mSelection.toString(), null,
                MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER);
    }
}
