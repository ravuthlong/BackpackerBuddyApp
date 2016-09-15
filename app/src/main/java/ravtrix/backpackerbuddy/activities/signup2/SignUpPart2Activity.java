package ravtrix.backpackerbuddy.activities.signup2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.activities.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSendBaseActivity;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends OptionMenuSendBaseActivity {

    @BindView(R.id.etFirstname) protected EditText etFirstname;
    @BindView(R.id.etLastname) protected EditText etLastname;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        LeakCanary.install(getApplication());
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

        userLocalStore = new UserLocalStore(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSend:

                HashMap<String, String> userInfo = new HashMap<>();
                userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                userInfo.put("firstname", etFirstname.getText().toString());
                userInfo.put("lastname", etLastname.getText().toString());

                final ProgressDialog progressDialog = Helpers.showProgressDialog(this, "");

                final Call<JsonObject> signUpPart2 =
                        RetrofitUserInfoSingleton
                                .getRetrofitUserInfo()
                                .signUserUpPart2()
                                .signUserUpPart2(userInfo);
                signUpPart2.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject result = response.body();
                        if (result.get("status").getAsInt() == 1) {
                            // Insert user information into the database successfully
                            startActivity(new Intent(SignUpPart2Activity.this, UserMainPage.class));

                        } else {
                            // Error inserting user information
                            Helpers.showAlertDialog(SignUpPart2Activity.this, "Error");
                        }
                        Helpers.hideProgressDialog(progressDialog);
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Helpers.displayToast(SignUpPart2Activity.this, "Error signing up. Try again.");
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
