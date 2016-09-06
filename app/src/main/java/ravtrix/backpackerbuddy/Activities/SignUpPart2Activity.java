package ravtrix.backpackerbuddy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helper.Helper;
import ravtrix.backpackerbuddy.helper.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 3/28/16.
 */
public class SignUpPart2Activity extends AppCompatActivity {

    @BindView(R.id.etFirstname) protected EditText etFirstname;
    @BindView(R.id.etLastname) protected EditText etLastname;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_signup2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitSignup2:

                HashMap<String, String> userInfo = new HashMap<>();
                userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                userInfo.put("firstname", etFirstname.getText().toString());
                userInfo.put("lastname", etLastname.getText().toString());

                final ProgressDialog progressDialog = Helper.showProgressDialog(this, "");

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
                            Helper.showAlertDialog(SignUpPart2Activity.this, "Error");
                        }
                        Helper.hideProgressDialog(progressDialog);
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
