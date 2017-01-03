package ravtrix.backpackerbuddy.activities.mainpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.SettingsActivity;
import ravtrix.backpackerbuddy.drawercustomfont.CustomTypefaceSpan;
import ravtrix.backpackerbuddy.drawercustomfont.FontTypeface;
import ravtrix.backpackerbuddy.fragments.bucketlist.BucketListFrag;
import ravtrix.backpackerbuddy.fragments.discussionroom.DiscussionRoomFragment;
import ravtrix.backpackerbuddy.fragments.findbuddy.FindBuddyTabFragment;
import ravtrix.backpackerbuddy.fragments.managedestination.ManageDestinationTabFragment;
import ravtrix.backpackerbuddy.fragments.message.MessagesFragment;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.CountryTabFragment;
import ravtrix.backpackerbuddy.fragments.userprofile.UserProfileFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.interfacescom.FragActivitySetDrawerInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityUpdateProfilePic;
import ravtrix.backpackerbuddy.models.UserLocalStore;

import static ravtrix.backpackerbuddy.R.id.settingsButton;

/**
 * Created by Ravinder on 3/29/16.
 */
public class UserMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, FragActivitySetDrawerInterface, FragActivityProgressBarInterface,
        FragActivityResetDrawer, FragActivityUpdateProfilePic {

    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private int currentPos;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    private CircleImageView profilePic;
    @BindView(R.id.spinner_main)
    protected ProgressBar progressBar;
    private UserLocalStore userLocalStore;
    private boolean refreshProfilePic = true;
    private boolean userHitHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userLocalStore = new UserLocalStore(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageButton settingsButton = (ImageButton) header.findViewById(R.id.settingsButton);
        profilePic = (CircleImageView) header.findViewById(R.id.profile_image);
        changeTypeface(navigationView);

        setProfilepicture();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        profilePic.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        setSupportActionBar(toolbar);
        setUpFragments();
        screenStartUpState();
        setNavigationDrawerIcons();
        toggleListener();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.navActivity:
                currentPos = 0;
                setTitle("Travel Posts");
                break;
            case R.id.navFindBuddy:
                currentPos = 1;
                setTitle("Find Buddy");
                break;
            case R.id.navInbox:
                currentPos = 2;
                setTitle("Inbox");
                break;
            case R.id.navDestination:
                currentPos = 3;
                setTitle("Manage Posts");
                break;
            case R.id.navDiscussion:
                navigationView.getMenu().getItem(0).setChecked(true);
                currentPos = 5;
                setTitle("Discussion Room");
                break;
            case R.id.navBucketList:
                currentPos = 6;
                setTitle("My Bucket List");
                break;
            default:
        }
        changeFragment(this.currentPos);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case settingsButton:
                startActivityForResult(new Intent(this, SettingsActivity.class), 2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            refreshProfilePic = false;
        }
    }

    // Set up the fragments
    private void setUpFragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new CountryTabFragment());
        fragmentList.add(new FindBuddyTabFragment());
        fragmentList.add(new MessagesFragment());
        fragmentList.add(new ManageDestinationTabFragment());
        fragmentList.add(new UserProfileFragment());
        fragmentList.add(new DiscussionRoomFragment());
        fragmentList.add(new BucketListFrag());
    }

    // Start up state
    //Title Idex, with closed navigation drawer and default fragment 1
    private void screenStartUpState() {
        setTitle("Discussion Room");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentList.get(5)).addToBackStack(null).commit();
    }

    private void changeFragment(final int currentPos) {
        drawerLayout.closeDrawer(GravityCompat.START);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,
                fragmentList.get(currentPos)).commit();
        // Delay to avoid lag when changing between option in navigation drawer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        fragmentList.get(currentPos)).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 250);
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

    private void setNavigationDrawerIcons() {
        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_record_voice_over_black_24dp);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_flight_takeoff_black_24dp);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_person_pin_circle_black_24dp);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_chat_bubble_black_24dp);
        navigationView.getMenu().getItem(4).setIcon(R.drawable.ic_edit_black_24dp);
        navigationView.getMenu().getItem(5).setIcon(R.drawable.ic_assignment_black_24dp);

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

    private void setProfilepicture() {
        if ((userLocalStore.getLoggedInUser().getUserImageURL().equals("0"))) {
            Picasso.with(this)
                    .load("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png")
                    .noFade()
                    .into(profilePic);
        } else {
            Picasso.with(this).load(userLocalStore.getLoggedInUser().getUserImageURL()).noFade().into(profilePic);
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = Helpers.showAlertDialogWithTwoOptions(UserMainPage.this, "Backpacker Buddy",
                "Are you sure you want to exit?", "No");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // If user hit or hold home button, do not refresh profile picture
        this.userHitHome = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfilePic = !userHitHome;

        if ((userLocalStore.getLoggedInUser().getUserImageURL() == null) ||
                (userLocalStore.getLoggedInUser().getUserImageURL().equals("0"))) {
            Picasso.with(this).load("http://i.imgur.com/268p4E0.jpg").noFade().into(profilePic);
        } else {
            if (refreshProfilePic) {
                Picasso.with(this)
                        .load(userLocalStore.getLoggedInUser().getUserImageURL())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .fit()
                        .centerCrop()
                        .into(profilePic);
            } else {
                Picasso.with(this)
                        .load(userLocalStore.getLoggedInUser().getUserImageURL())
                        .fit()
                        .centerCrop()
                        .into(profilePic);
            }
        }
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

    @Override
    public void onUpdateProfilePic() {
        // User hitting back button from changing profile picture in EditPhotoActivity.
        // Invoked to set new profile picture
        this.userHitHome = false;
    }

    private void applyFontToItem(MenuItem item, Typeface font) {
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, 20), 0 ,
                mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }

    private void changeTypeface(NavigationView navigationView){
        FontTypeface fontTypeface = new FontTypeface(this);
        Typeface typeface = fontTypeface.getTypefaceAndroid();

        MenuItem item;

        item = navigationView.getMenu().findItem(R.id.navDiscussion);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.navActivity);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.navFindBuddy);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.navInbox);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.navDestination);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.navBucketList);
        applyFontToItem(item, typeface);
    }


}

