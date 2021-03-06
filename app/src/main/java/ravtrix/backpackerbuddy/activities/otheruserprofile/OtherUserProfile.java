package ravtrix.backpackerbuddy.activities.otheruserprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.chat.ConversationActivity;
import ravtrix.backpackerbuddy.activities.otheruserbucket.OtherUserBucketListActivity;
import ravtrix.backpackerbuddy.activities.usermap.UserMapActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.models.UserLocalStore;

public class OtherUserProfile extends AppCompatActivity implements IOtherUserProfileView,
        View.OnClickListener {

    @BindView(R.id.detail1_otherProfile) protected TextView userDetailOne;
    @BindView(R.id.detail2_otherProfile) protected TextView userDetailTwo;
    @BindView(R.id.detail3_otherProfile) protected TextView userDetailThree;
    @BindView(R.id.detail4_otherProfile) protected TextView userDetailFour;
    @BindView(R.id.username_otherProfile) protected TextView username;
    @BindView(R.id.layout_otherProfile) protected LinearLayout relativeLayout;
    @BindView(R.id.spinner_otherProfile) protected ProgressBar progressBar;
    @BindView(R.id.activity_other_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.activity_other_tvLocation) protected TextView tvLocation;
    @BindView(R.id.activity_other_user_bFloatingButton) protected FloatingActionButton messageButton;
    @BindView(R.id.activity_other_user_bFloatingBucket) protected FloatingActionButton bucketButton;
    @BindView(R.id.activity_other_noDetails) protected TextView noDetails;
    @BindView(R.id.activity_other_noSix) protected TextView noSix;
    @BindView(R.id.activity_other_noPersonality) protected TextView noPersonality;
    @BindView(R.id.activity_other_noIdeal) protected TextView noIdeal;
    @BindView(R.id.imgTravel) protected ImageView imgTravel;
    @BindView(R.id.imgNotTravel) protected ImageView imgNotTravel;
    @BindView(R.id.txtTravel) protected TextView txtTravel;
    @BindView(R.id.txtNotTravel) protected TextView txtNotTravel;
    @BindView(R.id.linearOtherProfile) protected LinearLayout linearLayout;
    @BindView(R.id.activity_other_user_bFloatingMap) protected FloatingActionButton mapButton;
    private OtherUserProfilePresenter otherUserProfilePresenter;
    private int otherUserID;
    private String otherUserImage = "";
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting views
        setContentView(R.layout.activity_other_user_profile);
        ButterKnife.bind(this);
        Helpers.overrideFonts(this, linearLayout);
        this.setTitle("User Profile");
        relativeLayout.setVisibility(View.INVISIBLE);
        setToolbar();
        showProgressbar();

        userLocalStore = new UserLocalStore(this);
        otherUserProfilePresenter = new OtherUserProfilePresenter(this);

        mapButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        bucketButton.setOnClickListener(this);

        // Getting data
        fetchOtherUserProfile();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Helpers.setToolbar(this, toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_other_user_bFloatingButton:

                if (userLocalStore.getLoggedInUser().getUserID() != 0) {
                    Intent convoIntent = new Intent(this, ConversationActivity.class);
                    convoIntent.putExtra("otherUserID", Integer.toString(otherUserID));
                    convoIntent.putExtra("otherUserImage", otherUserImage);
                    startActivity(convoIntent);
                } else {
                    Helpers.displayToast(this, "Become a member to chat");
                }
                break;
            case R.id.activity_other_user_bFloatingMap:
                otherUserProfilePresenter.hasMap(otherUserID);
                break;
            case R.id.activity_other_user_bFloatingBucket:
                otherUserProfilePresenter.hasBucket(otherUserID);
                break;
            default:
                break;
        }
    }

    @Override
    public void startMapActivity() {
        Intent mapIntent = new Intent(OtherUserProfile.this, UserMapActivity.class);
        mapIntent.putExtra("isOtherUser", true);
        mapIntent.putExtra("otherUserID", otherUserID);
        startActivity(mapIntent);
    }

    @Override
    public void startBucketActivity() {
        Intent bucketIntent = new Intent(this, OtherUserBucketListActivity.class);
        bucketIntent.putExtra("otherUserID", Integer.toString(otherUserID));
        startActivity(bucketIntent);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void fetchOtherUserProfile() {
        final Bundle postInfo = getIntent().getExtras();
        this.otherUserID = postInfo.getInt("userID");
        otherUserProfilePresenter.fetchOtherUser(otherUserID);
    }

    @Override
    public void setUsername(String username) {
        this.username.setText(username);
    }

    @Override
    public void setUserDetailOne(String userDetailOne) {
        if (!userDetailOne.equals("")) {
            this.userDetailOne.setText(userDetailOne);
            this.noDetails.setVisibility(View.GONE);
        } else {
            this.userDetailOne.setVisibility(View.GONE);
            this.noDetails.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserDetailTwo(String userDetailTwo) {
        if (!userDetailTwo.equals("")) {
            this.userDetailTwo.setText(userDetailTwo);
            this.noSix.setVisibility(View.GONE);
        } else {
            this.userDetailTwo.setVisibility(View.GONE);
            this.noSix.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserDetailThree(String userDetailThree) {
        if (!userDetailThree.equals("")) {
            this.userDetailThree.setText(userDetailThree);
            this.noPersonality.setVisibility(View.GONE);
        } else {
            this.userDetailThree.setVisibility(View.GONE);
            this.noPersonality.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserDetailFour(String userDetailFour) {
        if (!userDetailFour.equals("")) {
            this.userDetailFour.setText(userDetailFour);
            this.noIdeal.setVisibility(View.GONE);
        } else {
            this.userDetailFour.setVisibility(View.GONE);
            this.noIdeal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserLocation(String latitude, String longitude, String country) {
        tvLocation.setText(country);
    }

    @Override
    public void loadProfileImage(String imageURL) {

        this.otherUserImage = imageURL;
        Picasso.with(getApplicationContext())
                .load(imageURL)
                .fit()
                .centerCrop()
                .into(profileImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void displayErrorToast() {
        Helpers.displayErrorToast(this);
    }

    @Override
    public void showImgTravel() {
        this.imgTravel.setVisibility(View.VISIBLE);
    }

    @Override
    public void showImgNotTravel() {
        this.imgNotTravel.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTxtTravel() {
        this.txtTravel.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTxtNotTravel() {
        this.txtNotTravel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImgTravel() {
        this.imgTravel.setVisibility(View.GONE);
    }

    @Override
    public void hideImgNotTravel() {
        this.imgNotTravel.setVisibility(View.GONE);
    }

    @Override
    public void hideTxtTravel() {
        this.txtTravel.setVisibility(View.GONE);
    }

    @Override
    public void hideTxtNotTravel() {
        this.txtNotTravel.setVisibility(View.GONE);
    }

    @Override
    public void showFloatingButtonBucket() {
        bucketButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFloatingButtonMessage() {
        messageButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoBucket() {
        Helpers.displayToast(this, "User has no item on bucket list");
    }

    @Override
    public void showNoMap() {
        Helpers.displayToast(this, "User has no item on their map");
    }
}