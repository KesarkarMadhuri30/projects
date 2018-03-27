package com.winax.musicplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.appthemeengine.ATE;
import com.winax.musicplayer.R;
import com.winax.musicplayer.dataloaders.FavlistLoader;
import com.winax.musicplayer.dialogs.CreateFavlistDialog;
import com.winax.musicplayer.models.Favlist;
import com.winax.musicplayer.subfragments.FavlistPagerFragment;
import com.winax.musicplayer.subfragments.PlaylistPagerFragment;
import com.winax.musicplayer.utils.PreferencesUtility;
import com.winax.musicplayer.widgets.MultiViewPager;

import java.util.List;


public class FavoritesFragment extends Fragment {
    int favlistcount;
    FragmentStatePagerAdapter adapter;
    MultiViewPager pager;
    private boolean showAuto;
    private PreferencesUtility mPreferences;

  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(getActivity());
        showAuto = mPreferences.showAutoPlaylist();

    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_favorites, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fav_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.favorites);

        final List<Favlist> favlists = FavlistLoader.getFavlists(getActivity(),true);
        favlistcount = favlists.size();

        pager = (MultiViewPager) rootView.findViewById(R.id.favlistpager);

        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return favlistcount;
            }

            @Override
            public Fragment getItem(int position) {
                return FavlistPagerFragment.newInstance(position);
            }
        };
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("dark_theme", false)) {
            ATE.apply(this, "dark_theme");
        } else {
            ATE.apply(this, "light_theme");
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favlist, menu);

    }

   /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (showAuto) {
            menu.findItem(R.id.action_view_auto_favlists).setTitle("Hide auto favlists");
        } else menu.findItem(R.id.action_view_auto_favlists).setTitle("Show auto favlists");
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_favlist:
                CreateFavlistDialog.newInstance().show(getChildFragmentManager(), "CREATE_FAVORITE_LIST");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateFavlists(final long id) {
        final List<Favlist> favlists = FavlistLoader.getFavlists(getActivity(),true);
        favlistcount = favlists.size();
        adapter.notifyDataSetChanged();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < favlists.size(); i++) {
                    long favlistid = favlists.get(i).id;
                    if (favlistid == id) {
                        pager.setCurrentItem(i);
                        break;
                    }
                }
            }
        }, 200);

    }

}
