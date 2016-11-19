package ravtrix.backpackerbuddy.activities.otheruserprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.ConversationActivity;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;

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
    @BindView(R.id.activity_other_noDetails) protected TextView noDetails;
    @BindView(R.id.activity_other_noSix) protected TextView noSix;
    @BindView(R.id.activity_other_noPersonality) protected TextView noPersonality;
    @BindView(R.id.activity_other_noIdeal) protected TextView noIdeal;

    private OtherUserProfilePresenter otherUserProfilePresenter;
    private int otherUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        ButterKnife.bind(this);
        otherUserProfilePresenter = new OtherUserProfilePresenter(this);
        messageButton.setOnClickListener(this);
        relativeLayout.setVisibility(View.INVISIBLE);
        setToolbar();
        this.setTitle("User Profile");
        showProgressbar();
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
                Intent convoIntent = new Intent(this, ConversationActivity.class);
                convoIntent.putExtra("otherUserID", Integer.toString(otherUserID));
                startActivity(convoIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
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
    public void setUserLocation(String latitude, String longitude) {
        tvLocation.setText(Helpers.cityGeocoder(getApplicationContext(),
                Double.parseDouble(latitude), Double.parseDouble(longitude)));
    }

    @Override
    public void loadProfileImage(String imageURL) {
        Picasso.with(getApplicationContext())
                .load(imageURL)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
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
        Helpers.displayToast(OtherUserProfile.this, "Error");
    }
}