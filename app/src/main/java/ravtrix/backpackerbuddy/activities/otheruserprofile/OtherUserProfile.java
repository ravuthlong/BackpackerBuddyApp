package ravtrix.backpackerbuddy.activities.otheruserprofile;

import android.os.Bundle;
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
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;

public class OtherUserProfile extends AppCompatActivity implements IOtherUserProfileView {

    @BindView(R.id.detail1_otherProfile) protected TextView userDetailOne;
    @BindView(R.id.detail2_otherProfile) protected TextView userDetailTwo;
    @BindView(R.id.detail3_otherProfile) protected TextView userDetailThree;
    @BindView(R.id.detail4_otherProfile) protected TextView userDetailFour;
    @BindView(R.id.username_otherProfile) protected TextView username;
    @BindView(R.id.layout_otherProfile) protected LinearLayout relativeLayout;
    @BindView(R.id.spinner_otherProfile) protected ProgressBar progressBar;
    @BindView(R.id.activity_other_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.activity_other_tvLocation) protected TextView tvLocation;
    private OtherUserProfilePresenter otherUserProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        ButterKnife.bind(this);
        relativeLayout.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Helpers.setToolbar(this, toolbar);

        otherUserProfilePresenter = new OtherUserProfilePresenter(this);
        showProgressbar();
        fetchOtherUserProfile();

    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void fetchOtherUserProfile() {
        final Bundle postInfo = getIntent().getExtras();
        otherUserProfilePresenter.fetchOtherUser(postInfo.getInt("userID"));
    }


    @Override
    public void setUsername(String username) {
        this.username.setText(username);
    }

    @Override
    public void setUserDetailOne(String userDetailOne) {
        this.userDetailOne.setText(userDetailOne);
    }

    @Override
    public void setUserDetailTwo(String userDetailTwo) {
        this.userDetailTwo.setText(userDetailTwo);
    }

    @Override
    public void setUserDetailThree(String userDetailThree) {
        this.userDetailThree.setText(userDetailThree);
    }

    @Override
    public void setUserDetailFour(String userDetailFour) {
        this.userDetailFour.setText(userDetailFour);
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