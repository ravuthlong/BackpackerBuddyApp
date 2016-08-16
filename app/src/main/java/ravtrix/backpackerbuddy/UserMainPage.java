package ravtrix.backpackerbuddy;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ravtrix.backpackerbuddy.Fragments.Activity;
import ravtrix.backpackerbuddy.Fragments.Destination;
import ravtrix.backpackerbuddy.Fragments.Messages;
import ravtrix.backpackerbuddy.SlidingDrawer.ItemSlideMenu;
import ravtrix.backpackerbuddy.SlidingDrawer.SlidingMenuAdapter;

/**
 * Created by Ravinder on 3/29/16.
 */
public class UserMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static RelativeLayout rLayoutMain;
    private Toolbar toolbarMain;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    public static ListView listView;
    private List<ItemSlideMenu> itemList;
    private List<Fragment> fragmentList;
    private SlidingMenuAdapter adapter;
    private int currentPos;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_slidingdrawer);
        setContentView(R.layout.activity_main);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //fragmentManager = getSupportFragmentManager();
        //rLayoutMain = (RelativeLayout) findViewById(R.id.rLayoutMain);
        //listView = (ListView) findViewById(R.id.listViewMain);


        //setUpDrawerList();
        setUpFragments();
        screenStartUpState();
        //drawerListViewListener();
        toggleListener();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.navActivity:
                currentPos = 0;
                break;
            case R.id.navDestination:
                currentPos = 1;
                break;
            case R.id.navInbox:
                currentPos = 2;
                break;
            case R.id.navSettings:
                currentPos = 3;
                break;
            default:
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,
                fragmentList.get(currentPos)).commit();

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // Add items to the drawer list for navigation drawer
    private void setUpDrawerList() {
        itemList = new ArrayList<>();
        itemList.add(new ItemSlideMenu("Activity"));
        itemList.add(new ItemSlideMenu("My Destination"));
        itemList.add(new ItemSlideMenu("Inbox"));

        adapter = new SlidingMenuAdapter(this, itemList);
        listView.setAdapter(adapter);
    }

    // Set up the fragments
    private void setUpFragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new Activity());
        fragmentList.add(new Destination());
        fragmentList.add(new Messages());
        fragmentList.add(new Messages());

    }

    // Start up state
    //Title Idex, with closed navigation drawer and default fragment 1
    private void screenStartUpState() {
        setTitle("");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentList.get(0)).addToBackStack(null).commit();
        //listView.setItemChecked(0, true);
        //drawerLayout.closeDrawer(listView);
    }

    // Fragments to be displayed based on user selection from navigation drawer
    private void drawerListViewListener() {
        // Handle click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPos = position;

                if (position == 0) {
                    setTitle("");
                } else {
                    //setTitle(itemList.get(position).getTitle());
                }
                drawerLayout.closeDrawer(listView);
                // Set item to selected
                listView.setItemChecked(position, true);

                // Delay to avoid lag between navigation drawer items
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.rLayoutMain,
                                fragmentList.get(currentPos)).commit();
                    }
                }, 270);
            }
        });
    }

    // Listens to when drawer navigation is opened or closed.
    // Disabled menu items on action bar
    private void toggleListener() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawerOpened, R.string.drawerClosed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync it based on if navigation drawer is selected or not.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
