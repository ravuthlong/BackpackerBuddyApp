package ravtrix.backpackerbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.baseActivitiesAndFragments.OptionMenuPostBaseActivity;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionPostActivity extends OptionMenuPostBaseActivity {
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.etDiscussion) protected EditText etDiscussion;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_post);

        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        this.setTitle("New Discussion");

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.submitPost:
                HashMap<String, String> newDiscussion = new HashMap<>();
                newDiscussion.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                newDiscussion.put("post", etDiscussion.getText().toString());
                newDiscussion.put("time", Long.toString(System.currentTimeMillis()));

                Call<JsonObject> retrofitCall =  RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                        .insertDiscussion()
                        .insertDiscussion(newDiscussion);

                retrofitCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.body().get("status").getAsInt() == 1) { //success
                            startActivity(new Intent(DiscussionPostActivity.this, UserMainPage.class));
                        } else { //error
                            Toast.makeText(DiscussionPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(DiscussionPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
