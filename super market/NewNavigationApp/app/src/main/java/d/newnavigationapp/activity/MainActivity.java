package d.newnavigationapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import d.newnavigationapp.Fragment.CartFragment;
import d.newnavigationapp.Fragment.OrderFragment;
import d.newnavigationapp.Fragment.SearchFragment;
import d.newnavigationapp.adapter.CustomExpandableListAdapter;
import d.newnavigationapp.count.Utils;
import d.newnavigationapp.notification.NotificationCountSetClass;
import d.newnavigationapp.other.ExpandableListDataSource;
import d.newnavigationapp.NavigationManager;
import d.newnavigationapp.R;

public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] items;
    public  static int mNotificationsCount = 0;

    private String email,name;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private NavigationManager mNavigationManager;
    private Map<String, List<String>> mExpandableListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);
        mExpandableListView.addHeaderView(listHeaderView);
        initItems();

        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i=getIntent();
        Bundle b=i.getExtras();
        email=b.getString("Email");
        name=b.getString("Name");



    }

    private void initItems() {
        items = getResources().getStringArray(R.array.MainMenu);
    }


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.Big_Bazaar);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.Big_Bazaar);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
            }
        });

       mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                getSupportActionBar().setTitle(R.string.Big_Bazaar);
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                getSupportActionBar().setTitle(selectedItem);

                System.out.println("item:"+selectedItem);

                if (items[0].equals(mExpandableListTitle.get(groupPosition))) {
                    Intent intent=new Intent(MainActivity.this,OpenActivity.class);
                    intent.putExtra("name",selectedItem);
                    intent.putExtra("nm",name);
                    intent.putExtra("email",email);
                    startActivity(intent);
                } else if (items[1].equals(mExpandableListTitle.get(groupPosition))) {
                    Intent intent=new Intent(MainActivity.this,OpenActivity.class);
                    intent.putExtra("name",selectedItem);
                    intent.putExtra("nm",name);
                    intent.putExtra("email",email);

                    startActivity(intent);
                } else if (items[2].equals(mExpandableListTitle.get(groupPosition))) {
                    Intent intent=new Intent(MainActivity.this,OpenActivity.class);
                    intent.putExtra("name",selectedItem);

                    startActivity(intent);
                } else if (items[3].equals(mExpandableListTitle.get(groupPosition))) {
                    Intent intent=new Intent(MainActivity.this,OpenActivity.class);
                    intent.putExtra("name",selectedItem);
                    intent.putExtra("nm",name);
                    intent.putExtra("email",email);
                    startActivity(intent);
                } else if (items[4].equals(mExpandableListTitle.get(groupPosition))) {
                    Intent intent=new Intent(MainActivity.this,OpenActivity.class);
                    intent.putExtra("name",selectedItem);
                    intent.putExtra("nm",name);
                    intent.putExtra("email",email);
                    startActivity(intent);
                } else {
                    throw new IllegalArgumentException("Not supported fragment type");
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment(), "Search");
        adapter.addFragment(new CartFragment(), "Cart");
        adapter.addFragment(new OrderFragment(), "Order");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    SearchFragment searchFragment = new SearchFragment();
                    Bundle intent = new Bundle();
                    intent.putString("email",email);
                    intent.putString("name",name);
                    searchFragment.setArguments(intent);
                    return searchFragment;

                case 1:

                    CartFragment cartFragment = new CartFragment();
                    Bundle intent1 = new Bundle();
                    intent1.putString("email", email);
                    intent1.putString("name",name);
                    intent1.putInt("countcart",mNotificationsCount);
                    cartFragment.setArguments(intent1);
                    return cartFragment;




                case 2:

                    OrderFragment orderFragment = new OrderFragment();
                    Bundle intent2 = new Bundle();
                    intent2.putString("email", email);
                    intent2.putString("name",name);
                    orderFragment.setArguments(intent2);
                    return orderFragment;


                default:
                    return new SearchFragment();
            }

             //return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

       // MenuItem item = menu.findItem(R.id.action_notifications);
        //LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
       // Utils.setBadgeCount(this, icon, mNotificationsCount);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item,mNotificationsCount);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
