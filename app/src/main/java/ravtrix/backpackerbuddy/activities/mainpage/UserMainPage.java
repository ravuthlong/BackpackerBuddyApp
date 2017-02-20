package ravtrix.backpackerbuddy.activities.mainpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.AppRater;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.login.LogInActivity;
import ravtrix.backpackerbuddy.activities.settings.SettingsActivity;
import ravtrix.backpackerbuddy.activities.signup1.SignUpPart1Activity;
import ravtrix.backpackerbuddy.application.BaseApplication;
import ravtrix.backpackerbuddy.drawercustomfont.CustomTypefaceSpan;
import ravtrix.backpackerbuddy.drawercustomfont.FontTypeface;
import ravtrix.backpackerbuddy.fragments.bucketlist.BucketListFrag;
import ravtrix.backpackerbuddy.fragments.discussionroom.DiscussionRoomFragment;
import ravtrix.backpackerbuddy.fragments.findbuddy.FindBuddyTabFragment;
import ravtrix.backpackerbuddy.fragments.managedestination.ManageDestinationTabFragment;
import ravtrix.backpackerbuddy.fragments.message.MessagesFragment;
import ravtrix.backpackerbuddy.fragments.perfectphoto.PerfectPhotoFragment;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.CountryTabFragment;
import ravtrix.backpackerbuddy.fragments.userprofile.UserProfileFragment;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.interfacescom.FragActivityProgressBarInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityResetDrawer;
import ravtrix.backpackerbuddy.interfacescom.FragActivitySetDrawerInterface;
import ravtrix.backpackerbuddy.interfacescom.FragActivityUpdateProfilePic;
import ravtrix.backpackerbuddy.models.LocationUpdateSharedPreference;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 3/29/16.
 */
public class UserMainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, FragActivitySetDrawerInterface, FragActivityProgressBarInterface,
        FragActivityResetDrawer, FragActivityUpdateProfilePic, IUserMainView {

    @BindView(R.id.drawer_layout) protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.nav_view) protected NavigationView navigationView;
    @BindView(R.id.spinner_main) protected ProgressBar progressBar;
    @BindView(R.id.navigation_drawer_bottom) NavigationView navigationView_Footer;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private int currentPos;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CircleImageView profilePic;
    private UserLocalStore userLocalStore;
    private boolean refreshProfilePic = true;
    private boolean userHitHome = false;
    private boolean isUserAGuest = false;
    private ImageButton settingsButton;
    private UserMainPresenter userMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        ((BaseApplication) getApplication()).checkForRequiredUpdate(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userLocalStore = new UserLocalStore(this);
        new LocationUpdateSharedPreference(this); // check fr version update
        userMainPresenter = new UserMainPresenter(this);


        AppRater.app_launched(this); // Check the need to show rate dialog
        setUpView();
        navigationView_Footer.setVisibility(View.GONE);
        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            // Update local store upon every opening. This is a way of syncing data
            userMainPresenter.updateLocalstore(userLocalStore.getLoggedInUser().getUserID());
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ((BaseApplication) getApplication()).checkForRequiredUpdate(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.navActivity:
                navigationView.getMenu().getItem(2).setChecked(true);
                currentPos = 0;
                setTitle("Travel Posts");
                break;
            case R.id.navFindBuddy:
                navigationView.getMenu().getItem(3).setChecked(true);
                currentPos = 1;
                setTitle("Find Buddy");
                break;
            case R.id.navInbox:
                navigationView.getMenu().getItem(4).setChecked(true);
                currentPos = 2;
                setTitle("Inbox");
                break;
            case R.id.navDestination:
                navigationView.getMenu().getItem(6).setChecked(true);
                currentPos = 3;
                setTitle("Manage Posts");
                break;
            case R.id.navDiscussion:
                navigationView.getMenu().getItem(0).setChecked(true);
                currentPos = 5;
                setTitle("Discussion Room");
                break;
            case R.id.navBucketList:
                navigationView.getMenu().getItem(5).setChecked(true);
                currentPos = 6;
                setTitle("My Bucket List");
                break;
            case R.id.navPerfectPhoto:
                navigationView.getMenu().getItem(1).setChecked(true);
                currentPos = 7;
                setTitle("The Perfect Photo");
                break;
            case R.id.navLogIn:
                startActivity(new Intent(this, LogInActivity.class));
                finish();
                break;
            case R.id.navSignUp:
                startActivity(new Intent(this, SignUpPart1Activity.class));
                finish();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                // Permission requests calls on this main activity. To communicate with the fragment's
                // onRequest, need to manually call it
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
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
        fragmentList.add(new PerfectPhotoFragment());
    }

    // Start up state
    //Title Index, with closed navigation drawer and default fragment 1
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

        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.syncState();
        }
    }

    private void setNavigationDrawerIcons() {
        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_record_voice_over_black_24dp);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_monochrome_photos_black_24dp);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_flight_takeoff_black_24dp);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_person_pin_circle_black_24dp);
        navigationView.getMenu().getItem(4).setIcon(R.drawable.ic_chat_bubble_black_24dp);
        navigationView.getMenu().getItem(5).setIcon(R.drawable.ic_assignment_black_24dp);
        navigationView.getMenu().getItem(6).setIcon(R.drawable.ic_edit_black_24dp);

    }

    private void resetNavigationDrawer() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    public DrawerLayout getDrawerLayout() {
        return this.drawerLayout;
    }

    private void setProfilepicture() {
        if ((userLocalStore.getLoggedInUser().getUserID() == 0)) {
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfilePic = !userHitHome;

        if (!checkIsUserNotLoggedIn()) {
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

        item = navigationView.getMenu().findItem(R.id.navPerfectPhoto);
        applyFontToItem(item, typeface);
    }

    private void changeTypeFaceFooter(NavigationView navigationView) {
        FontTypeface fontTypeface = new FontTypeface(this);
        Typeface typeface = fontTypeface.getTypefaceAndroid();

        MenuItem item;

        item = navigationView.getMenu().findItem(R.id.navLogIn);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.navSignUp);
        applyFontToItem(item, typeface);
    }

    private void setUpView() {

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        settingsButton = (ImageButton) header.findViewById(R.id.settingsButton);
        profilePic = (CircleImageView) header.findViewById(R.id.profile_image);
        changeTypeface(navigationView);


        setProfilepicture();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        if (userLocalStore.getLoggedInUser().getUserID() != 0) {
            profilePic.setOnClickListener(this);
        }
        settingsButton.setOnClickListener(this);
        setSupportActionBar(toolbar);
        setUpFragments();
        screenStartUpState();
        setNavigationDrawerIcons();
        toggleListener();
    }
    /**
     * Check to see if the user is already logged in. If not, go to log in page
     */
    private boolean checkIsUserNotLoggedIn() {
        return (userLocalStore.getLoggedInUser().getUserID() == 0);
    }

    @Override
    public void updateLocalstore(LoggedInUser updated) {
        userLocalStore.clearUserData();
        userLocalStore.storeUserData(updated);
    }
}

