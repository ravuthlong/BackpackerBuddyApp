package ravtrix.backpackerbuddy.activities.otheruserprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.detail4_otherProfile) protected TextView userDetailfour;
    @BindView(R.id.username_otherProfile) protected TextView username;
    @BindView(R.id.layout_otherProfile) protected RelativeLayout relativeLayout;
    @BindView(R.id.spinner_otherProfile) protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        relativeLayout.setVisibility(View.INVISIBLE);
        Bundle postInfo = getIntent().getExtras();

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
                    userDetailfour.setText(responseJSON.get("detailFour").getAsString());
                }
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(OtherUserProfile.this, "Error");
            }
        });
    }
}