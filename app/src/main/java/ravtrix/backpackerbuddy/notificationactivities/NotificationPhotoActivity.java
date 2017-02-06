package ravtrix.backpackerbuddy.notificationactivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.mainpage.UserMainPage;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitPerfectPhotoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentphotorecyclerview.PhotoCommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview.PerfectPhotoModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator.DividerDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_notification_photo_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.activity_notification_photo_progressBar) protected ProgressBar progressBar;
    @BindView(R.id.activity_notification_photo_etComment) protected EditText etComment;
    @BindView(R.id.activity_notification_photo_submitButton) protected Button bSubmit;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private NotificationPhotoAdapter notificationPhotoAdapter;
    private UserLocalStore userLocalStore;
    private List<PhotoCommentModel> photoCommentModels;
    private PerfectPhotoModel perfectPhotoModel;
    private int photoID;
    private int ownerID;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_photo);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        setTitle("Perfect Photo");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            photoID = bundle.getInt("photoID");
            ownerID = bundle.getInt("ownerID");
        }

        this.progressBar.setVisibility(View.VISIBLE);
        this.recyclerView.setVisibility(View.GONE);

        this.userLocalStore = new UserLocalStore(this);
        this.perfectPhotoModel = new PerfectPhotoModel();
        this.photoCommentModels = new ArrayList<>();

        RecyclerView.ItemDecoration dividerDecorator = new DividerDecoration(this, R.drawable.line_divider_inbox);
        this.recyclerView.addItemDecoration(dividerDecorator);
        fetchParentPhoto(userLocalStore.getLoggedInUser().getUserID(), photoID);

        bSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // Prevents double clicking
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch(view.getId()) {
            case R.id.activity_notification_photo_submitButton:

                if (etComment.getText().toString().trim().isEmpty()) {
                    Helpers.displayToast(this, "Empty comment...");
                } else if (etComment.getText().toString().trim().length() >= 500) {
                    Helpers.displayToast(this, "Exceeded max character count (500)");
                } else {
                    // Submit comment
                    insertPhotoCommentRetrofit();
                }

                break;
            default:
                break;
        }
    }

    private void insertPhotoCommentRetrofit() {
        final HashMap<String, String> photoHash = new HashMap<>();
        photoHash.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
        photoHash.put("photoID", Integer.toString(photoID));
        photoHash.put("comment", etComment.getText().toString().trim());
        photoHash.put("time", Long.toString(System.currentTimeMillis()));

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .insertPhotoComment()
                .insertPhotoComment(photoHash);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 0) {
                    Toast.makeText(NotificationPhotoActivity.this, "Error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Hide keyboard
                    Helpers.hideKeyboard(NotificationPhotoActivity.this);
                    etComment.getText().clear();
                    // Success
                    incrementTotalComment(photoID);
                    fetchPhotoCommentsRefresh(photoID); // refresh page

                    // scroll recycler view to last position to see new comment
                    recyclerView.scrollToPosition(photoCommentModels.size());

                    // Don't notify if user comments on their own post..
                    if (userLocalStore.getLoggedInUser().getUserID() != ownerID) {
                        notifyTheOwner(ownerID, photoHash.get("comment"), photoID);
                    }
                    // Notify all other users that has commented in this post that is NOT the current poster
                    notifyOtherUsers(userLocalStore.getLoggedInUser().getUserID(), ownerID, photoHash.get("comment"), photoID);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    private void fetchParentPhoto(int userID, final int photoID) {

        Call<List<PerfectPhotoModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getOnePerfectPhoto()
                .getOnePerfectPhoto(userID, photoID);

        retrofit.enqueue(new Callback<List<PerfectPhotoModel>>() {
            @Override
            public void onResponse(Call<List<PerfectPhotoModel>> call, Response<List<PerfectPhotoModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {
                    perfectPhotoModel = response.body().get(0);
                    fetchPhotoComments(photoID);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<PerfectPhotoModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Helpers.displayErrorToast(NotificationPhotoActivity.this);
            }
        });
    }

    private void fetchParentPhotoRefresh(int userID, final int photoID) {

        Call<List<PerfectPhotoModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getOnePerfectPhoto()
                .getOnePerfectPhoto(userID, photoID);

        retrofit.enqueue(new Callback<List<PerfectPhotoModel>>() {
            @Override
            public void onResponse(Call<List<PerfectPhotoModel>> call, Response<List<PerfectPhotoModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {
                    perfectPhotoModel = response.body().get(0);
                    notificationPhotoAdapter.swapParentPhoto(perfectPhotoModel);
                }
            }

            @Override
            public void onFailure(Call<List<PerfectPhotoModel>> call, Throwable t) {
                Helpers.displayErrorToast(NotificationPhotoActivity.this);
            }
        });
    }


    private void fetchPhotoComments(int photoID) {

        Call<List<PhotoCommentModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getPhotoComments()
                .getPhotoComments(photoID);

        retrofit.enqueue(new Callback<List<PhotoCommentModel>>() {
            @Override
            public void onResponse(Call<List<PhotoCommentModel>> call, Response<List<PhotoCommentModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {

                    photoCommentModels = response.body();

                    notificationPhotoAdapter = new NotificationPhotoAdapter(NotificationPhotoActivity.this,
                            photoCommentModels, perfectPhotoModel, userLocalStore.getLoggedInUser().getUserID());
                    // set adapter to recycler view
                    recyclerView.setAdapter(notificationPhotoAdapter);
                    // set layout
                    recyclerView.setLayoutManager(new LinearLayoutManager(NotificationPhotoActivity.this));

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoCommentModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Helpers.displayErrorToast(NotificationPhotoActivity.this);
            }
        });
    }

    private void fetchPhotoCommentsRefresh(int photoID) {

        Call<List<PhotoCommentModel>> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .getPhotoComments()
                .getPhotoComments(photoID);

        retrofit.enqueue(new Callback<List<PhotoCommentModel>>() {
            @Override
            public void onResponse(Call<List<PhotoCommentModel>> call, Response<List<PhotoCommentModel>> response) {

                if (response.body().get(0).getSuccess() == 1) {
                    photoCommentModels = response.body();
                    notificationPhotoAdapter.swapComments(photoCommentModels);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoCommentModel>> call, Throwable t) {
                Helpers.displayErrorToast(NotificationPhotoActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                fetchParentPhotoRefresh(userLocalStore.getLoggedInUser().getUserID(), photoID);
                break;
            case 1:
                fetchPhotoCommentsRefresh(photoID);
                break;
            default:
                break;
        }
    }


    public void notifyTheOwner(int userID, String comment, int photoID) {
        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .sendNotificationOwner()
                .sendNotificationOwner(userID, comment, photoID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void notifyOtherUsers(int userID, int ownerID, String comment, int photoID) {
        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .sendNotificationAllOther()
                .sendNotificationOthers(userID, ownerID, comment, photoID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    private void incrementTotalComment(int photoID) {

        Call<JsonObject> retrofit = RetrofitPerfectPhotoSingleton.getRetrofitPerfectPhoto()
                .incrementPhotoCommentCount()
                .incrementPhotoCommentCount(Integer.toString(photoID));
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, UserMainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
