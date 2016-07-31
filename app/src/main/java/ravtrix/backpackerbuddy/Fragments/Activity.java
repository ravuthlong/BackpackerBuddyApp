package ravtrix.backpackerbuddy.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Cache;

import java.util.List;

import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.adapter.FeedListAdapter;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.data.FeedItem;
import ravtrix.backpackerbuddy.UserLocalStore;
import ravtrix.backpackerbuddy.VolleyServerConnections.VolleyMainPosts;

/**
 * Created by Ravinder on 7/29/16.
 */
public class Activity extends Fragment {

    private static final String TAG = Activity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<FeedItem> feedItems;
    private FeedListAdapter feedListAdapter;
    private FloatingActionButton postWidget;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar spinner;
    private Cache.Entry entry;
    private UserLocalStore userLocalStore;
    private String URL_LoggedInUser;
    private Button bMainLog, bMainRoll, bMainInfo;
    private FragmentManager fragmentManager;
    private VolleyMainPosts volleyMainPosts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_usercountries, container, false);

        return v;
    }
}
