package ravtrix.backpackerbuddy.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.editTitle) protected TextView editTitle;
    @BindView(R.id.editHint) protected EditText editText;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private String detailType;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        setEditText();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    onBackPressed();
                }
            });
        }

        Intent intent = getIntent();
        editTitle.setText(intent.getStringExtra("title"));
        if (intent.getBooleanExtra("isHint", true)) {
            editText.setHint(intent.getStringExtra("detail"));
        } else {
            editText.setText(intent.getStringExtra("detail"));
        }
        detailType = intent.getStringExtra("detailType");
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        TextView tv = new TextView(this);
        tv.setId(R.id.btn_save);
        tv.setText(R.string.saveToolbar);
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(this);
        tv.setPadding(5, 0, 20, 0);
        //tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(18);
        menu.add(0, 1, 100, "Save").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_save:
                if (!editText.getText().toString().isEmpty()) {

                    // User information and detail to update in the database
                    HashMap<String, String> userDetail = new HashMap<>();
                    userDetail.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                    userDetail.put("detail", editText.getText().toString());
                    userDetail.put("detailType", detailType);

                    Call<JsonObject> updateDetail = RetrofitUserInfoSingleton.getRetrofitUserInfo().updateUserDetail().updateUserDetail(userDetail);
                    updateDetail.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject returnedJSON = response.body();

                            // Success update. Return user to user profile activity
                            if (returnedJSON.get("status").getAsInt() == 1) {
                                startActivity(new Intent(EditInfoActivity.this, UserMainPage.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Helpers.displayToast(EditInfoActivity.this, "Error");
                        }
                    });
                }
                break;
            default:
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void setEditText() {
        editText.setFocusable(false);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setFocusableInTouchMode(true);
                return false;
            }
        });
    }
}
