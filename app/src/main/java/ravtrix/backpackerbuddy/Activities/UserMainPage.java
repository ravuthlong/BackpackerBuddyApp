package ravtrix.backpackerbuddy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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
import ravtrix.backpackerbuddy.helper.Helper;
import ravtrix.backpackerbuddy.interfaces.ActivityFragmentInterface;

/**
 * Created by Ravinder on 3/29/16.
 */
public class UserMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, ActivityFragmentInterface {

    private FragmentManager fragmentManager;
    public static ListView listView;
    private List<Fragment> fragmentList;
    private int currentPos;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.drawer_layout) protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.nav_view) protected NavigationView navigationView;
    private ImageButton settingsButton;
    private CircleImageView profilePic;
    public static ProgressBar progressBar;
    private RefWatcher refWatcher; // Leakcanary memory leak watcher for fragments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LeakCanary.install(getApplication());
        ButterKnife.bind(this);

        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                System.out.println("LOWWWW");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                System.out.println("MEDUIUM");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                System.out.println("HIGHHHH");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                System.out.println("XHIGHHHH");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                System.out.println("XXHIGHHHH");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                System.out.println("XXXHIGHHHH");
                break;
            default:
                System.out.println("HELPPPPPPP");

        }

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
        //drawerListViewListener();
        toggleListener();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.navActivity:
                currentPos = 0;
                setTitle("Activity");
                break;
            case R.id.navDestination:
                currentPos = 1;
                setTitle("Destination");
                break;
            case R.id.navInbox:
                currentPos = 2;
                setTitle("Inbox");
                break;
            case R.id.navSettings:
                currentPos = 3;
                setTitle("Settings Activity");
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
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                break;
            default:

        }
    }

    // Set up the fragments
    private void setUpFragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new Activity());
        fragmentList.add(new Destination());
        fragmentList.add(new Messages());
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

    private void changeFragment(int currentPos) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,
                fragmentList.get(currentPos)).commit();

        drawerLayout.closeDrawer(GravityCompat.START);
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

    @Override
    public void onBackPressed() {
        Helper.showAlertDialogWithTwoOptions(UserMainPage.this, this, "Are you sure you want to exit?", "Yes", "No");
    }

    @Override
    public void setDrawerSelected(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    public static RefWatcher getRefWatcher(Context context) {
        UserMainPage userMainPage = (UserMainPage) context.getApplicationContext();
        return userMainPage.refWatcher; // Can access private data. Fragments share the same context
    }
}
