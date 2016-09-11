package ravtrix.backpackerbuddy.activities;

import android.content.Context;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.Activity;
import ravtrix.backpackerbuddy.fragments.Destination;
import ravtrix.backpackerbuddy.fragments.Messages;
import ravtrix.backpackerbuddy.fragments.UserProfile;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfaces.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfaces.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.interfaces.FragActivitySetDrawerInterface;

/**
 * Created by Ravinder on 3/29/16.
 */
public class UserMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, FragActivitySetDrawerInterface, FragActivityProgressBarInterface,
        FragActivityResetDrawer {

    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private int currentPos;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.drawer_layout) protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.nav_view) protected NavigationView navigationView;
    private ImageButton settingsButton;
    private CircleImageView profilePic;
    private ProgressBar progressBar;
    private RefWatcher refWatcher; // Leakcanary memory leak watcher for fragments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LeakCanary.install(getApplication());
        ButterKnife.bind(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        progressBar = (ProgressBar) findViewById(R.id.spinner_main);

        settingsButton = (ImageButton) header.findViewById(R.id.settingsButton);
        profilePic = (CircleImageView) header.findViewById(R.id.profile_image);
        profilePic.setOnClickListener(this);

        Picasso.with(this).load("http://i.imgur.com/268p4E0.jpg").noFade().into(profilePic);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        settingsButton.setOnClickListener(this);
        setSupportActionBar(toolbar);
        setUpFragments();
        screenStartUpState();
        setNavigationDrawerIcons();
        //drawerListViewListener();
        toggleListener();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.navActivity:
                navigationView.getMenu().getItem(0).setChecked(true);
                currentPos = 0;
                setTitle("Activity");
                break;
            case R.id.navFindBuddy:
                currentPos = 1;
                setTitle("Find Buddy");
                break;
            case R.id.navDestination:
                currentPos = 2;
                setTitle("Manage Destination");
                break;
            case R.id.navInbox:
                currentPos = 3;
                setTitle("Inbox");
                break;
            default:
        }

        changeFragment(this.currentPos);

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.settingsButton:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.profile_image:
                currentPos = 4;
                setTitle("User Profile");
                changeFragment(this.currentPos);

                // Removed all checked items from navigation because profile is not on it
                resetNavigationDrawer();
                break;
            default:

        }
    }

    // Set up the fragments
    private void setUpFragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new Activity());
        fragmentList.add(new Messages());
        fragmentList.add(new Destination());
        fragmentList.add(new Messages());
        fragmentList.add(new UserProfile());
    }

    // Start up state
    //Title Idex, with closed navigation drawer and default fragment 1
    private void screenStartUpState() {
        setTitle("Activity");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentList.get(0)).addToBackStack(null).commit();
    }

    private void changeFragment(final int currentPos) {

        // Delay to avoid lag when changing between option in navigation drawer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        fragmentList.get(currentPos)).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 150);
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync it based on if navigation drawer is selected or not.
        actionBarDrawerToggle.syncState();
    }

    public static RefWatcher getRefWatcher(Context context) {
        UserMainPage userMainPage = (UserMainPage) context.getApplicationContext();
        return userMainPage.refWatcher; // Can access private data. Fragments share the same context
    }

    private void setNavigationDrawerIcons() {
        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_record_voice_over_black_24dp);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_person_pin_circle_black_24dp);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_assignment_black_24dp);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_chat_bubble_black_24dp);
    }

    private void resetNavigationDrawer() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
    public List<Fragment> getFragList() {
        return this.fragmentList;
    }

    public DrawerLayout getDrawerLayout() {
        return this.drawerLayout;
    }

    @Override
    public void onBackPressed() {
        Helpers.showAlertDialogWithTwoOptions(UserMainPage.this, this, "Are you sure you want to exit?", "Yes", "No");
    }

    // Below override methods are called from fragment child to access activity
    @Override
    public void setDrawerSelected(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }
    @Override
    public void onResetDrawer() {
        resetNavigationDrawer();
        setTitle("User Profile");
    }
    @Override
    public void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void setProgressBarInvisible() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
