package ravtrix.backpackerbuddy.activities.otheruserprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserProfile extends AppCompatActivity {

    @BindView(R.id.detail1_otherProfile) protected TextView userDetailOne;
    @BindView(R.id.detail2_otherProfile) protected TextView userDetailTwo;
    @BindView(R.id.detail3_otherProfile) protected TextView userDetailThree;
    @BindView(R.id.detail4_otherProfile) protected TextView userDetailFour;
    @BindView(R.id.username_otherProfile) protected TextView username;
    @BindView(R.id.layout_otherProfile) protected LinearLayout relativeLayout;
    @BindView(R.id.spinner_otherProfile) protected ProgressBar progressBar;
    @BindView(R.id.activity_other_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.activity_other_tvLocation) protected TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Helpers.setToolbar(this, toolbar);

        relativeLayout.setVisibility(View.INVISIBLE);
        fetchOtherUserProfile();

    }

    private void fetchOtherUserProfile() {

        final Bundle postInfo = getIntent().getExtras();

        Call<JsonObject> returnedInfo = RetrofitUserInfoSingleton.getRetrofitUserInfo().getUserDetails().getUserDetails(postInfo.getInt("userID"));
        returnedInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject responseJSON = response.body();

                // If success
                if (responseJSON.get("success").getAsInt() == 1) {
                    responseJSON.get("firstname").getAsString();
                    responseJSON.get("lastname").getAsString();
                    username.setText(responseJSON.get("username").getAsString());
                    userDetailOne.setText(responseJSON.get("detailOne").getAsString());
                    userDetailTwo.setText(responseJSON.get("detailTwo").getAsString());
                    userDetailThree.setText(responseJSON.get("detailThree").getAsString());
                    userDetailFour.setText(responseJSON.get("detailFour").getAsString());

                    // Set city based on latitude and longitude
                    String latitude = responseJSON.get("latitude").getAsString().trim();
                    String longitude = responseJSON.get("longitude").getAsString().trim();
                    tvLocation.setText(Helpers.cityGeocoder(getApplicationContext(),
                            Double.parseDouble(latitude), Double.parseDouble(longitude)));

                    Picasso.with(getApplicationContext())
                            .load("http://backpackerbuddy.net23.net/profile_pic/" +
                                    postInfo.getInt("userID") + ".JPG")
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

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(OtherUserProfile.this, "Error");
            }
        });
    }
}