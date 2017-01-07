package ravtrix.backpackerbuddy.activities.discussion;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuSaveBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDiscussionActivity extends OptionMenuSaveBaseActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.relativeEditDiscussion) protected RelativeLayout relativeEdit;
    @BindView(R.id.etEditDiscussion) protected EditText etEditDiscussion;
    private int discussionID;
    private String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discussion);
        ButterKnife.bind(this);
        setTitle("Edit Post");
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, relativeEdit);

        getBundle();
        etEditDiscussion.setText(post);
        etEditDiscussion.setSelection(etEditDiscussion.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitSave:

                if (etEditDiscussion.getText().toString().trim().length() < 10) {
                    Helpers.displayToast(this, "Post is too short...");
                } else {
                    editDiscussionRetrofit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editDiscussionRetrofit() {

        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .updateDiscussion()
                .updateDiscussion(discussionID, etEditDiscussion.getText().toString().trim());

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body().get("status").getAsInt() == 1) {
                    setResult(1);
                    finish();
                } else {
                    Helpers.displayToast(getApplicationContext(), "Error");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helpers.displayToast(getApplicationContext(), "Error");
            }
        });
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            discussionID = bundle.getInt("discussionID");
            post = bundle.getString("post");
        }
    }
}
