package ravtrix.backpackerbuddy.activities.discussion.discussionlove;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.activities.discussion.discussionlove.recyclerview.adapter.DiscussionLoveAdapter;
import ravtrix.backpackerbuddy.activities.discussion.discussionlove.recyclerview.model.DiscussionLove;
import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 4/8/17.
 */

public class DiscussionLoveActivity extends AppCompatActivity {

    @BindView(R.id.activity_discussion_love_recyclerView) protected RecyclerView recyclerView;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private DiscussionLoveAdapter discussionLoveAdapter;
    private List<DiscussionLove> discussionLoveList;
    private UserLocalStore userLocalStore;
    private int discussionID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_love);
        ButterKnife.bind(this);
        setTitle("Likes");
        Helpers.setToolbar(this, toolbar);

        userLocalStore = new UserLocalStore(this);

        getBundle();
        fetchDiscussionLoves();
    }

    private void fetchDiscussionLoves() {

        Call<List<DiscussionLove>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getADiscussionLoves()
                .getADiscussionLoves(discussionID);

        retrofit.enqueue(new Callback<List<DiscussionLove>>() {
            @Override
            public void onResponse(Call<List<DiscussionLove>> call, Response<List<DiscussionLove>> response) {
                discussionLoveList = response.body();

                if (discussionLoveList.get(0).getSuccess() == 1) setAdapter(discussionLoveList);
            }

            @Override
            public void onFailure(Call<List<DiscussionLove>> call, Throwable t) {
                Helpers.displayErrorToast(DiscussionLoveActivity.this);
            }
        });
    }

    private void setAdapter(List<DiscussionLove> discussionLovesList) {

        discussionLoveAdapter = new DiscussionLoveAdapter(this, discussionLovesList, userLocalStore);
        recyclerView.setAdapter(discussionLoveAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            discussionID = bundle.getInt("discussionID");
        }
    }
}
