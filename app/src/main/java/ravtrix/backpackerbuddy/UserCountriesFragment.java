package ravtrix.backpackerbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.adapter.FeedListAdapter;
import ravtrix.backpackerbuddy.RecyclerViewFeed.MainRecyclerView.data.FeedItem;
import ravtrix.backpackerbuddy.ServerRequests.Callbacks.NetworkConnectionCallBack;

/**
 * Created by Ravinder on 3/29/16.
 */
public class UserCountriesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = UserCountriesFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<FeedItem> feedItems;
    private FeedListAdapter feedListAdapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_usercountries, container, false);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout1);
        //spinner = (ProgressBar) v.findViewById(R.id.progress_bar);

        //spinner.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.postRecyclerView1);
        feedItems = new ArrayList<>();

        feedListAdapter = new FeedListAdapter(getActivity(), feedItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(feedListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout.setOnRefreshListener(this);

        //getJsonLive();

        //parseJsonFeed();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_usermain, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.userMainAdd:
                startActivity(new Intent(getActivity(), TravelSelectionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/*
    // Pull JSON directly from the PHP JSON result
    private void getJsonLive() {
        UserLocalStore.visitCounter++;
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_TEST, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }*/

    // Populate FeedItem Array with JSONArray from PHP
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                String name;
                name = feedObj.getString("firstname") + " " + feedObj.getString("lastname");
                item.setId(feedObj.getInt("postID"));
                // item.setName(name);
                //item.setUsername(feedObj.getString("username"));
                //item.setStatus(feedObj.getString("post"));
                //item.setProfilePic(feedObj.getString("userpic"));

                feedItems.add(item);
            }
            // notify data changes to list adapater
            feedListAdapter.notifyDataSetChanged();
            spinner.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                new InternetAccess(getContext(), new NetworkConnectionCallBack() {
                    @Override
                    public void networkConnection(boolean isConnected) {
                        if (isConnected) {
                            //
                        } else {
                            //
                        }
                    }
                }).execute();
            }
        }, 200);
    }

    class InternetAccess extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        NetworkConnectionCallBack networkConnectionCallBack;

        public InternetAccess(Context context, NetworkConnectionCallBack networkConnectionCallBack) {
            this.context = context;
            this.networkConnectionCallBack = networkConnectionCallBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0);
                } catch (IOException e) {
                    Log.e(TAG, "Error checking internet connection", e);
                }
            } else {
                Log.d(TAG, "No network available!");
            }
            return false;
        }
    }
}
