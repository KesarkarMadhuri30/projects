package com.winax.musicplayer.subfragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.winax.musicplayer.R;
import com.winax.musicplayer.dataloaders.FavlistLoader;
import com.winax.musicplayer.dataloaders.FavlistSongLoader;
import com.winax.musicplayer.dataloaders.LastAddedLoader;
import com.winax.musicplayer.dataloaders.PlaylistLoader;
import com.winax.musicplayer.dataloaders.PlaylistSongLoader;
import com.winax.musicplayer.dataloaders.SongLoader;
import com.winax.musicplayer.dataloaders.TopTracksLoader;
import com.winax.musicplayer.models.Favlist;
import com.winax.musicplayer.models.Playlist;
import com.winax.musicplayer.models.Song;
import com.winax.musicplayer.utils.Constants;
import com.winax.musicplayer.utils.NavigationUtils;
import com.winax.musicplayer.utils.TimberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FavlistPagerFragment extends Fragment{

    private static final String ARG_PAGE_NUMBER = "pageNumber";
    int[] foregroundColors = {R.color.pink_transparent, R.color.green_transparent, R.color.blue_transparent, R.color.red_transparent, R.color.purple_transparent};
    private int pageNumber, songCountInt;
    private int foregroundColor;
    private long firstAlbumID = -1;
    private Favlist favlist;
    private TextView favlistname, songcount, favlistnumber, favlisttype;
    private ImageView favlistImage;
    private View foreground;
    private Context mContext;

    public static FavlistPagerFragment newInstance(int pageNumber) {
        FavlistPagerFragment fragment = new FavlistPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favlist_pager, container, false);

        final List<Favlist> favlists = FavlistLoader.getFavlists(getActivity(),true);

        pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
        favlist = favlists.get(pageNumber);

        favlistname = (TextView) rootView.findViewById(R.id.favname);
        favlistnumber = (TextView) rootView.findViewById(R.id.favnumber);
        songcount = (TextView) rootView.findViewById(R.id.favsongcount);
        favlisttype = (TextView) rootView.findViewById(R.id.favlisttype);
        favlistImage = (ImageView) rootView.findViewById(R.id.favlist_image);
        foreground = rootView.findViewById(R.id.fav_foreground);

        favlistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Pair> tranitionViews = new ArrayList<>();
                tranitionViews.add(0, Pair.create((View) favlistname, "transition_playlist_name"));
                tranitionViews.add(1, Pair.create((View) favlistImage, "transition_album_art"));
                tranitionViews.add(2, Pair.create(foreground, "transition_foreground"));
                NavigationUtils.navigateToFavlistDetail(getActivity(), getFavlistType(), firstAlbumID, String.valueOf(favlistname.getText()), foregroundColor, favlist.id, tranitionViews);
            }
        });

        mContext = this.getContext();
        setUpPlaylistDetails();
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedinstancestate) {
        new FavlistPagerFragment.loadPlaylistImage().execute("");
    }

    private void setUpPlaylistDetails() {
        favlistname.setText(favlist.name);

        int number = getArguments().getInt(ARG_PAGE_NUMBER) + 1;
        String playlistnumberstring;

        if (number > 9) {
            playlistnumberstring = String.valueOf(number);
        } else {
            playlistnumberstring = "0" + String.valueOf(number);
        }
        favlistnumber.setText(playlistnumberstring);

        Random random = new Random();
        int rndInt = random.nextInt(foregroundColors.length);

        foregroundColor = foregroundColors[rndInt];
        foreground.setBackgroundColor(foregroundColor);

        if (pageNumber > 2) {
            favlisttype.setVisibility(View.GONE);
        }

    }

    private String getPlaylistType() {
        switch (pageNumber) {
            case 0:
                return Constants.NAVIGATE_FAVLIST_LASTADDED;
           /* case 1:
                return Constants.NAVIGATE_FAVLIST_RECENT;
            case 2:
                return Constants.NAVIGATE_FAVLIST_TOPTRACKS;*/
            default:
                return Constants.NAVIGATE_FAVLIST_USERCREATED;
        }
    }
    private String getFavlistType() {
        switch (pageNumber) {
            case 0:
                return Constants.NAVIGATE_FAVLIST_LASTADDED;
            /*case 1:
                return Constants.NAVIGATE_FAVLIST_RECENT;
            case 2:
                return Constants.NAVIGATE_FAVLIST_TOPTRACKS;*/
            default:
                return Constants.NAVIGATE_FAVLIST_USERCREATED;
        }
    }




    private class loadPlaylistImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
                switch (pageNumber) {
                    case 0:
                        List<Song> lastAddedSongs = LastAddedLoader.getLastAddedSongs(getActivity());
                        songCountInt = lastAddedSongs.size();

                        if (songCountInt != 0) {
                            firstAlbumID = lastAddedSongs.get(0).albumId;
                            return TimberUtils.getAlbumArtUri(firstAlbumID).toString();
                        } else return "nosongs";
                   /* case 1:
                        TopTracksLoader recentloader = new TopTracksLoader(getActivity(), TopTracksLoader.QueryType.RecentSongs);
                        List<Song> recentsongs = SongLoader.getSongsForCursor(TopTracksLoader.getCursor());
                        songCountInt = recentsongs.size();

                        if (songCountInt != 0) {
                            firstAlbumID = recentsongs.get(0).albumId;
                            return TimberUtils.getAlbumArtUri(firstAlbumID).toString();
                        } else return "nosongs";
                    case 2:
                        TopTracksLoader topTracksLoader = new TopTracksLoader(getActivity(), TopTracksLoader.QueryType.TopTracks);
                        List<Song> topsongs = SongLoader.getSongsForCursor(TopTracksLoader.getCursor());
                        songCountInt = topsongs.size();

                        if (songCountInt != 0) {
                            firstAlbumID = topsongs.get(0).albumId;
                            return TimberUtils.getAlbumArtUri(firstAlbumID).toString();
                        } else return "nosongs";*/
                    default:
                        List<Song> favlistsongs = FavlistSongLoader.getSongsInFavlist(getActivity(), favlist.id);
                        songCountInt = favlistsongs.size();

                        if (songCountInt != 0) {
                            firstAlbumID = favlistsongs.get(0).albumId;
                            return TimberUtils.getAlbumArtUri(firstAlbumID).toString();
                        } else return "nosongs";

                }
            } else return "context is null";

        }

        @Override
        protected void onPostExecute(String uri) {
            ImageLoader.getInstance().displayImage(uri, favlistImage,
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .showImageOnFail(R.drawable.icon_music)
                            .resetViewBeforeLoading(true)
                            .build(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        }
                    });
            songcount.setText(" " + String.valueOf(songCountInt) + " " + mContext.getString(R.string.songs));
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
